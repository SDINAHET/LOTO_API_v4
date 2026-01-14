// csrf.js (global)
(function () {
  // Règles standards Spring + Axios
  axios.defaults.withCredentials = true;
  axios.defaults.xsrfCookieName = "XSRF-TOKEN";
  axios.defaults.xsrfHeaderName = "X-XSRF-TOKEN";

  async function ensureCsrfCookie(API_BASE) {
    // Si pas de cookie CSRF, on force le backend à le poser
    if (!document.cookie.includes("XSRF-TOKEN=")) {
      await axios.get(`${API_BASE}/api/csrf`, { withCredentials: true });
    }
  }

  // expose globalement
  window.__csrf = { ensureCsrfCookie };
})();
