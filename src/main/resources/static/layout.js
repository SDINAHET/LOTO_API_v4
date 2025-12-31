/* layout.js
   Injecte le header + footer dans les pages qui contiennent :
   <div id="appHeader"></div>
   <div id="appFooter"></div>
*/

(function () {
  const API_BASE =
    (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1")
      ? "http://localhost:8082"
      : "https://stephanedinahet.fr";

  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
  }

  // Header (Topbar) – version légère inspirée de ton index.html
  function renderHeader() {
    return `
      <header class="topbar">
        <a class="brand" href="index.html" aria-label="Retour à l'accueil">
          <img src="loto_tracker.png" alt="Loto Tracker" class="brand-logo">
          <div>
            <div class="brand-title">Tracker du Loto Français</div>
            <div class="brand-sub" id="currentTime">Heure de Paris : --:--:--</div>
          </div>
        </a>

        <div class="topbar-actions">
          <a href="login.html" class="btn-ghost" id="authActionBtn">
            <i class="fa-solid fa-user"></i>
            <span id="authActionText">Se connecter</span>
          </a>


          <div class="chip" id="userChip" style="display:none;">
            <span>Bienvenue, <b id="userEmail">—</b></span>
            <button class="btn-danger-soft" id="logoutBtn" type="button">
              <i class="fa-solid fa-right-from-bracket"></i>
              Déconnexion
            </button>
          </div>
        </div>
      </header>
    `;
  }

  function renderFooter() {
    return `
      <footer class="footer">
        <a href="mentions_legales.html">Mentions légales</a>
        <a href="conditions_utilisation.html">Conditions</a>
        <a href="politique_confidentialite.html">Confidentialité</a>
        <span>© 2025 SDINAHET</span>
      </footer>
    `;
  }

  function updateAuthActionByPage() {
    const btn = document.getElementById("authActionBtn");
    const text = document.getElementById("authActionText");
    if (!btn || !text) return;

    const path = window.location.pathname.toLowerCase();

    if (path.endsWith("login.html")) {
      // On est sur la page login → proposer inscription
      btn.href = "register.html";
      text.textContent = "Créer un compte";
      btn.style.display = "inline-flex";
    }
    else if (path.endsWith("register.html")) {
      // On est sur la page register → proposer login
      btn.href = "login.html";
      text.textContent = "Se connecter";
      btn.style.display = "inline-flex";
    }
  }

  function updateParisTime() {
    // Sans moment.js : simple Intl
    const el = document.getElementById("currentTime");
    if (!el) return;
    const fmt = new Intl.DateTimeFormat("fr-FR", {
      timeZone: "Europe/Paris",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit"
    });
    el.textContent = "Heure de Paris : " + fmt.format(new Date());
  }

  async function checkUserAuthUI() {
    const loginBtn = document.getElementById("loginButton");
    const userChip = document.getElementById("userChip");
    const userEmail = document.getElementById("userEmail");

    if (!loginBtn || !userChip || !userEmail) return;

    // Si cookie/localStorage existe, on tente /userinfo (si tu l'as)
    const tokenCookie = getCookie("jwtToken");
    const tokenLS = localStorage.getItem("jwtToken");

    if (!tokenCookie && !tokenLS) {
      loginBtn.style.display = "inline-flex";
      userChip.style.display = "none";
      return;
    }

    try {
      const res = await fetch(`${API_BASE}/api/protected/userinfo`, {
        method: "GET",
        credentials: "include"
      });

      if (!res.ok) throw new Error("Not authenticated");

      const data = await res.json();
      const shown = data.username || data.email || "Utilisateur";

      loginBtn.style.display = "none";
      userChip.style.display = "inline-flex";
      userEmail.textContent = shown;
    } catch {
      loginBtn.style.display = "inline-flex";
      userChip.style.display = "none";
    }
  }

  async function logout() {
    try {
      const res = await fetch(`${API_BASE}/api/auth/logout`, {
        method: "POST",
        credentials: "include"
      });
      if (res.ok) {
        // optionnel: on nettoie localStorage
        localStorage.removeItem("jwtToken");
        window.location.href = "index.html";
      }
    } catch {
      // silence (tu peux afficher un message si tu veux)
    }
  }

  function bindLogout() {
    const btn = document.getElementById("logoutBtn");
    if (!btn) return;
    btn.addEventListener("click", logout);
  }

  function injectLayout() {
    const headerMount = document.getElementById("appHeader");
    const footerMount = document.getElementById("appFooter");
    if (headerMount) headerMount.innerHTML = renderHeader();
    if (footerMount) footerMount.innerHTML = renderFooter();

    updateParisTime();
    setInterval(updateParisTime, 1000);
    
    updateAuthActionByPage();
    checkUserAuthUI().then(bindLogout);
  }

  document.addEventListener("DOMContentLoaded", injectLayout);
})();

