import unittest
import requests
import uuid
import json


def request_with_fallback(session: requests.Session, url: str, json_body: dict, prefer="POST"):
    """
    Tente d'abord prefer (POST), puis si la réponse indique un mauvais verbe (Allow: PUT ou 405/403),
    retente automatiquement en PUT.
    Retourne (resp, method_used).
    """
    methods_try = [prefer, "PUT" if prefer == "POST" else "POST"]
    last_resp = None
    last_method = None

    for m in methods_try:
        if m == "POST":
            resp = session.post(url, json=json_body)
        else:
            resp = session.put(url, json=json_body)

        last_resp, last_method = resp, m

        # si OK -> stop
        if resp.status_code in (200, 201):
            return resp, m

        # si le serveur suggère un autre verbe (Allow: PUT) ou 405 Method Not Allowed,
        # on essaie l'autre méthode
        allow = resp.headers.get("Allow", "")
        if resp.status_code in (403, 405) and "PUT" in allow and m != "PUT":
            continue
        if resp.status_code in (403, 405) and "POST" in allow and m != "POST":
            continue

        # sinon on s'arrête (pas une erreur de verbe)
        break

    return last_resp, last_method


class TestAuthEndpoints(unittest.TestCase):
    BASES = [
        "http://127.0.0.1:8082",
        "https://stephanedinahet.fr",
    ]

    @staticmethod
    def _debug(resp, label=""):
        try:
            body = resp.json()
            body_str = json.dumps(body)[:800]
        except Exception:
            body_str = resp.text[:800]
        print(f"[DEBUG {label}] {resp.request.method} {resp.request.url} -> {resp.status_code}")
        print(f"[DEBUG Allow] {resp.headers.get('Allow')}")
        print(f"[DEBUG RespHeaders] {dict(resp.headers)}")
        print(f"[DEBUG Body] {body_str}")

    def setUp(self):
        self.rand_email = f"unittest_{uuid.uuid4().hex[:8]}@ex.com"
        self.rand_pwd = "Azerty123!"
        self.register_payload = {
            "firstName": "Jean",
            "lastName": "Dupont",
            "email": self.rand_email,
            "password": self.rand_pwd,
        }

        self.fixed_email = "test4@hbnb.com"
        self.fixed_pwd = "user1234"

    def test_login_existing_user(self):
        """Login + /me avec l’utilisateur existant test4@hbnb.com/user1234 (POST puis fallback PUT si nécessaire)"""
        for base in self.BASES:
            with self.subTest(base=base):
                s = requests.Session()

                # --- Login (POST -> fallback PUT si Allow: PUT / 405/403) ---
                login_url = f"{base}/api/auth/login"
                r, used = request_with_fallback(
                    s, login_url, {"email": self.fixed_email, "password": self.fixed_pwd}, prefer="POST"
                )
                if r.status_code != 200:
                    self._debug(r, f"login existing on {base} (used {used})")
                self.assertEqual(r.status_code, 200, f"Login existing user failed on {base} (used {used})")

                token = r.json().get("token")
                self.assertIsNotNone(token, f"No token returned for existing user on {base}")

                # --- /me ---
                headers = {"Authorization": f"Bearer {token}"}
                r = s.get(f"{base}/api/auth/me", headers=headers)
                if r.status_code != 200:
                    self._debug(r, f"me existing on {base}")
                self.assertEqual(r.status_code, 200, f"/me existing user failed on {base}")
                self.assertEqual(r.json().get("email"), self.fixed_email)

    def test_register_and_login_new_user(self):
        """Register + login + /me pour un user aléatoire (POST puis fallback PUT si nécessaire)"""
        for base in self.BASES:
            with self.subTest(base=base):
                s = requests.Session()

                # --- Register ---
                reg_url = f"{base}/api/auth/register"
                r, used = request_with_fallback(s, reg_url, self.register_payload, prefer="POST")
                if r.status_code not in (200, 201):
                    self._debug(r, f"register on {base} (used {used})")
                self.assertIn(r.status_code, (200, 201), f"Register failed on {base} (used {used})")

                # --- Login ---
                login_url = f"{base}/api/auth/login"
                r, used = request_with_fallback(
                    s, login_url, {"email": self.rand_email, "password": self.rand_pwd}, prefer="POST"
                )
                if r.status_code != 200:
                    self._debug(r, f"login new user on {base} (used {used})")
                self.assertEqual(r.status_code, 200, f"Login failed on {base} (used {used})")

                token = r.json().get("token")
                self.assertIsNotNone(token, f"No token returned on {base}")

                # --- /me ---
                headers = {"Authorization": f"Bearer {token}"}
                r = s.get(f"{base}/api/auth/me", headers=headers)
                if r.status_code != 200:
                    self._debug(r, f"me new user on {base}")
                self.assertEqual(r.status_code, 200, f"/me failed on {base}")
                self.assertEqual(r.json().get("email"), self.rand_email)


if __name__ == "__main__":
    unittest.main(verbosity=2)
