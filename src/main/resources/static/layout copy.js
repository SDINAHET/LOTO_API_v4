/* layout.js
   Injecte header + footer + CSS du layout (style identique à login)
   Requis dans les pages qui contiennent :
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

      /* évite que le footer fixed recouvre le contenu */
      body.has-fixed-footer{
        padding-bottom: var(--footer-h);
      }

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

      .brand-logo{
        width: 44px;
        height: 44px;
        object-fit: contain;
      }

      .brand-title{
        font-weight: 900;
        letter-spacing:.2px;
        line-height: 1.1;
        white-space: nowrap;
      }

      .brand-sub{
        font-size: .9rem;
        color: var(--muted);
        margin-top: 2px;
      }

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
      .btn-ghost:hover{
        transform: translateY(-1px);
        background: rgba(255,255,255,.08);
      }

      .btn-ghost .btn-ico{
        width: 18px;
        height: 18px;
        fill: none;
        stroke: var(--text);
        stroke-width: 2;
        stroke-linecap: round;
        stroke-linejoin: round;
        opacity:.95;
      }

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
      .btn-danger-soft:hover{
        background: rgba(239,68,68,.25);
      }

      .footer{
        position: fixed;
        left: 0;
        right: 0;
        bottom: 0;
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
      .footer a{
        color: var(--muted);
        text-decoration:none;
      }
      .footer a:hover{
        color: var(--text);
      }
    `;
    document.head.appendChild(style);
  }

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
            <!-- ✅ petit logo user inline (pas besoin de FontAwesome) -->
            <svg class="btn-ico" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M20 21a8 8 0 0 0-16 0"/>
              <circle cx="12" cy="8" r="4"/>
            </svg>
            <span id="authActionText">Se connecter</span>
          </a>

          <div class="chip" id="userChip" style="display:none;">
            <span>Bienvenue, <b id="userEmail">—</b></span>
            <button class="btn-danger-soft" id="logoutBtn" type="button" title="Déconnexion">
              <svg class="btn-ico" viewBox="0 0 24 24" aria-hidden="true" style="stroke:white">
                <path d="M10 17l5-5-5-5"/>
                <path d="M15 12H3"/>
                <path d="M21 3v18"/>
              </svg>
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
      btn.href = "register.html";
      text.textContent = "Créer un compte";
      btn.style.display = "inline-flex";
    } else if (path.endsWith("register.html")) {
      btn.href = "login.html";
      text.textContent = "Se connecter";
      btn.style.display = "inline-flex";
    } else {
      btn.href = "login.html";
      text.textContent = "Se connecter";
      btn.style.display = "inline-flex";
    }
  }

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

  async function checkUserAuthUI() {
    const authBtn = document.getElementById("authActionBtn");
    const userChip = document.getElementById("userChip");
    const userEmail = document.getElementById("userEmail");

    if (!authBtn || !userChip || !userEmail) return;

    const tokenCookie = getCookie("jwtToken");
    const tokenLS = localStorage.getItem("jwtToken");

    if (!tokenCookie && !tokenLS) {
      userChip.style.display = "none";
      authBtn.style.display = "inline-flex";
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

      authBtn.style.display = "none";
      userChip.style.display = "inline-flex";
      userEmail.textContent = shown;
    } catch {
      userChip.style.display = "none";
      authBtn.style.display = "inline-flex";
    }
  }

  async function logout() {
    try {
      const res = await fetch(`${API_BASE}/api/auth/logout`, {
        method: "POST",
        credentials: "include"
      });
      if (res.ok) {
        localStorage.removeItem("jwtToken");
        window.location.href = "index.html";
      }
    } catch {
      // silence
    }
  }

  function bindLogout() {
    const btn = document.getElementById("logoutBtn");
    if (!btn) return;
    btn.addEventListener("click", logout);
  }

  function injectLayout() {
    ensureLayoutStyles();
    document.body.classList.add("has-fixed-footer");

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
