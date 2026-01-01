/* layout.js
   Injecte header + footer + CSS du layout
   Compatible cookie HttpOnly (JWT invisible côté JS)
   Requis dans les pages qui contiennent :
   <div id="appHeader"></div>
   <div id="appFooter"></div>
*/
(function () {
  const HOST = window.location.hostname;

  // ✅ En local : on vise le même host que le front (localhost OU 127.0.0.1)
  const API_BASE_PRIMARY =
    (HOST === "localhost" || HOST === "127.0.0.1")
      ? `http://${HOST}:8082`
      : "https://stephanedinahet.fr";

  // ✅ Fallback en local : on tente aussi l’autre host
  const API_BASE_FALLBACK =
    (HOST === "localhost") ? "http://127.0.0.1:8082"
    : (HOST === "127.0.0.1") ? "http://localhost:8082"
    : null;

  const USERINFO_PATH = "/api/protected/userinfo";
  const LOGOUT_PATH = "/api/auth/logout";

  /* =========================================================
     Styles globaux du layout (header + footer)
  ========================================================= */
  function ensureLayoutStyles() {
    if (document.getElementById("layoutStyles")) return;

    const style = document.createElement("style");
    style.id = "layoutStyles";
    style.textContent = `
      :root{
        --topbar-h: 72px;
        --footer-h: 56px;
        --stroke: rgba(255,255,255,.12);
        --text: rgba(255,255,255,.92);
        --muted: rgba(255,255,255,.65);
      }

      body.has-fixed-footer{ padding-bottom: var(--footer-h); }

      .topbar{
        position: sticky;
        top: 0;
        z-index: 50;
        height: var(--topbar-h);
        display:flex;
        align-items:center;
        justify-content:space-between;
        gap: 14px;
        padding: 12px 16px;
        background: rgba(10,16,28,.82);
        backdrop-filter: blur(12px);
        border-bottom: 1px solid var(--stroke);
      }

      .brand{
        display:flex;
        align-items:center;
        gap: 12px;
        text-decoration:none;
        color: var(--text);
        min-width: 0;
      }
      .brand-logo{ width: 44px; height: 44px; object-fit: contain; }
      .brand-title{
        font-weight: 900;
        letter-spacing:.2px;
        line-height: 1.1;
        white-space: nowrap;
      }
      .brand-sub{ font-size: .9rem; color: var(--muted); margin-top: 2px; }

      .topbar-actions{
        display:flex;
        align-items:center;
        gap: 10px;
        margin-left:auto;
      }

      .btn-ghost{
        display:inline-flex;
        align-items:center;
        gap: 8px;
        padding:10px 12px;
        border-radius: 12px;
        border: 1px solid var(--stroke);
        background: rgba(255,255,255,.06);
        color: var(--text);
        text-decoration:none;
        font-weight: 800;
        white-space: nowrap;
        transition: transform .15s ease, background .15s ease;
      }
      .btn-ghost:hover{ transform: translateY(-1px); background: rgba(255,255,255,.08); }

      .btn-ico{
        width: 18px;
        height: 18px;
        fill: none;
        stroke: var(--text);
        stroke-width: 2;
        stroke-linecap: round;
        stroke-linejoin: round;
        opacity:.95;
      }

      .btn-burger{
        display:none;
        align-items:center;
        justify-content:center;
        width: 44px;
        height: 44px;
        padding: 0;
        border-radius: 12px;
      }
      @media (max-width: 991px){ .btn-burger{ display:inline-flex; } }

      .chip{
        display:inline-flex;
        align-items:center;
        gap: 10px;
        padding:10px 12px;
        border-radius: 12px;
        border:1px solid var(--stroke);
        background: rgba(255,255,255,.06);
        color: var(--text);
        font-weight: 800;
      }

      .btn-danger-soft{
        border: 1px solid rgba(239,68,68,.35);
        background: rgba(239,68,68,.18);
        color: #fff;
        padding:10px 12px;
        border-radius: 12px;
        cursor:pointer;
        font-weight: 900;
      }
      .btn-danger-soft:hover{ background: rgba(239,68,68,.25); }

      .footer{
        position: fixed;
        left: 0; right: 0; bottom: 0;
        height: var(--footer-h);
        display:flex;
        align-items:center;
        justify-content:center;
        gap: 14px;
        flex-wrap: wrap;
        padding: 10px 16px 14px;
        background: rgba(10,16,28,.55);
        backdrop-filter: blur(10px);
        border-top: 1px solid var(--stroke);
        color: var(--muted);
        font-weight: 800;
        font-size: .9rem;
        z-index: 40;
      }
      .footer a{ color: var(--muted); text-decoration:none; }
      .footer a:hover{ color: var(--text); }

      .api-status{
        display:flex;
        align-items:center;
        gap:8px;
        font-weight:900;
      }
      .api-dot{
        width:10px;
        height:10px;
        border-radius:50%;
        box-shadow: 0 0 6px currentColor;
      }
      .api-online{
        background:#22c55e;
        color:#22c55e;
      }
      .api-offline{
        background:#ef4444;
        color:#ef4444;
      }
    `;
    document.head.appendChild(style);
  }

  /* =========================================================
     Burger (menu) - delegation globale
     ✅ Fonctionne même si sidebar injectée après
     ✅ Evite double bind
  ========================================================= */
  function bindBurgerGlobal() {
    if (document.documentElement.dataset.burgerDelegation === "1") return;
    document.documentElement.dataset.burgerDelegation = "1";

    document.addEventListener("click", (e) => {
      const burger = e.target.closest("#burgerBtn");
      if (!burger) return;

      const sidebar = document.getElementById("sidebar");
      const overlay = document.getElementById("overlay");
      if (!sidebar || !overlay) return;

      const open = sidebar.classList.toggle("open");
      overlay.classList.toggle("show", open);
      document.body.classList.toggle("no-scroll", open);
    });

    document.addEventListener("click", (e) => {
      if (e.target.id !== "overlay") return;

      const sidebar = document.getElementById("sidebar");
      const overlay = document.getElementById("overlay");
      if (!sidebar || !overlay) return;

      sidebar.classList.remove("open");
      overlay.classList.remove("show");
      document.body.classList.remove("no-scroll");
    });

    document.addEventListener("keydown", (e) => {
      if (e.key !== "Escape") return;

      const sidebar = document.getElementById("sidebar");
      const overlay = document.getElementById("overlay");
      if (!sidebar || !overlay) return;

      sidebar.classList.remove("open");
      overlay.classList.remove("show");
      document.body.classList.remove("no-scroll");
    });
  }

  /* =========================================================
     HTML HEADER
  ========================================================= */
  function renderHeader() {
    return `
      <header class="topbar">
        <button class="btn-ghost btn-burger" id="burgerBtn" type="button" aria-label="Ouvrir le menu">
          <svg class="btn-ico" viewBox="0 0 24 24" aria-hidden="true">
            <path d="M4 6h16"></path>
            <path d="M4 12h16"></path>
            <path d="M4 18h16"></path>
          </svg>
        </button>

        <a class="brand" href="index.html" aria-label="Retour à l'accueil">
          <img src="loto_tracker.png" alt="Loto Tracker" class="brand-logo">
          <div>
            <div class="brand-title">Tracker du Loto Français</div>
            <div class="brand-sub" id="currentTime">Heure de Paris : --:--:--</div>
          </div>
        </a>

        <div class="topbar-actions">
          <a href="login.html" class="btn-ghost" id="authActionBtn">
            <svg class="btn-ico" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M20 21a8 8 0 0 0-16 0"></path>
              <circle cx="12" cy="8" r="4"></circle>
            </svg>
            <span id="authActionText">Se connecter</span>
          </a>

          <div class="chip" id="userChip" style="display:none;">
            <span>Bienvenue, <b id="userEmail">—</b></span>
            <button class="btn-danger-soft" id="logoutBtn" type="button" title="Déconnexion">
              <svg class="btn-ico" viewBox="0 0 24 24" aria-hidden="true" style="stroke:white">
                <path d="M10 17l5-5-5-5"></path>
                <path d="M15 12H3"></path>
                <path d="M21 3v18"></path>
              </svg>
              Déconnexion
            </button>
          </div>
        </div>
      </header>
    `;
  }

  /* =========================================================
     HTML FOOTER
  ========================================================= */
  function renderFooter() {
    return `
      <footer class="footer">
        <a href="mentions_legales.html">Mentions légales</a>
        <a href="conditions_utilisation.html">Conditions</a>
        <a href="politique_confidentialite.html">Confidentialité</a>
        <span>© 2025 SDINAHET</span>

        <span class="api-status" title="État de l'API">
          <span id="apiDot" class="api-dot api-offline"></span>
          API
        </span>
      </footer>
    `;
  }

  /* =========================================================
     Helpers fetch (userinfo) => AUTH UNIQUEMENT
  ========================================================= */
  async function fetchUserInfo(baseUrl) {
    const res = await fetch(`${baseUrl}${USERINFO_PATH}`, {
      method: "GET",
      credentials: "include",
      cache: "no-store"
    });

    if (!res.ok) throw new Error(`userinfo ${res.status}`);
    return await res.json();
  }

  function setAuthUI({ logged, label }) {
    const authBtn = document.getElementById("authActionBtn");
    const userChip = document.getElementById("userChip");
    const userEmail = document.getElementById("userEmail");

    if (!authBtn || !userChip || !userEmail) return;

    if (logged) {
      authBtn.style.display = "none";
      userChip.style.display = "inline-flex";
      userEmail.textContent = label || "Utilisateur";
    } else {
      userChip.style.display = "none";
      authBtn.style.display = "inline-flex";
      userEmail.textContent = "—";
    }
  }

  /* =========================================================
     Auth UI (SOURCE DE VÉRITÉ = /userinfo)
     ✅ essaie PRIMARY puis FALLBACK (localhost/127)
  ========================================================= */
  async function checkUserAuthUI() {
    try {
      const data = await fetchUserInfo(API_BASE_PRIMARY);
      const shown = data.username || data.email || "Utilisateur";
      setAuthUI({ logged: true, label: shown });
      window.__API_BASE_ACTIVE__ = API_BASE_PRIMARY;
      return;
    } catch {
      if (API_BASE_FALLBACK) {
        try {
          const data2 = await fetchUserInfo(API_BASE_FALLBACK);
          const shown2 = data2.username || data2.email || "Utilisateur";
          setAuthUI({ logged: true, label: shown2 });
          window.__API_BASE_ACTIVE__ = API_BASE_FALLBACK;
          return;
        } catch {
          setAuthUI({ logged: false });
          window.__API_BASE_ACTIVE__ = API_BASE_PRIMARY;
          return;
        }
      }

      setAuthUI({ logged: false });
      window.__API_BASE_ACTIVE__ = API_BASE_PRIMARY;
    }
  }

  /* =========================================================
     Ping API (alive/down) => 200 OU 401 = API vivante
  ========================================================= */
  async function pingApi(baseUrl) {
    const res = await fetch(`${baseUrl}${USERINFO_PATH}`, {
      method: "GET",
      credentials: "include",
      cache: "no-store"
    });

    return (res.status === 200 || res.status === 401);
  }

  async function checkApiAlive() {
    const dot = document.getElementById("apiDot");
    if (!dot) return;

    const basePrimary = window.__API_BASE_ACTIVE__ || API_BASE_PRIMARY;

    try {
      const okPrimary = await pingApi(basePrimary);
      if (okPrimary) {
        dot.classList.remove("api-offline");
        dot.classList.add("api-online");
        return;
      }

      if (API_BASE_FALLBACK) {
        const okFallback = await pingApi(API_BASE_FALLBACK);
        if (okFallback) {
          window.__API_BASE_ACTIVE__ = API_BASE_FALLBACK;
          dot.classList.remove("api-offline");
          dot.classList.add("api-online");
          return;
        }
      }

      throw new Error("API down");
    } catch {
      dot.classList.remove("api-online");
      dot.classList.add("api-offline");
    }
  }

  /* =========================================================
     Logout (toujours retour index.html)
  ========================================================= */
  async function logout() {
    const base = window.__API_BASE_ACTIVE__ || API_BASE_PRIMARY;

    try {
      await fetch(`${base}${LOGOUT_PATH}`, {
        method: "POST",
        credentials: "include"
      });
    } catch {
      // même si l'API échoue, on force la redirection
    } finally {
      localStorage.removeItem("jwtToken");
      window.location.href = "index.html";
    }
  }

  function bindLogout() {
    const btn = document.getElementById("logoutBtn");
    if (!btn) return;
    btn.addEventListener("click", logout);
  }

  /* =========================================================
     Heure de Paris
  ========================================================= */
  function updateParisTime() {
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

  /* =========================================================
     Init layout
  ========================================================= */
  function injectLayout() {
    ensureLayoutStyles();
    bindBurgerGlobal(); // ✅ IMPORTANT : bind une fois pour toutes

    document.body.classList.add("has-fixed-footer");

    const headerMount = document.getElementById("appHeader");
    const footerMount = document.getElementById("appFooter");

    if (headerMount) headerMount.innerHTML = renderHeader();
    if (footerMount) footerMount.innerHTML = renderFooter();

    updateParisTime();
    setInterval(updateParisTime, 1000);

    // ✅ D'abord auth (détermine parfois la base active), ensuite logout + ping API
    checkUserAuthUI().finally(() => {
      bindLogout();

      checkApiAlive();
      setInterval(checkApiAlive, 30000);
    });

    document.dispatchEvent(new CustomEvent("layout:ready"));
  }

  document.addEventListener("DOMContentLoaded", injectLayout);
})();
