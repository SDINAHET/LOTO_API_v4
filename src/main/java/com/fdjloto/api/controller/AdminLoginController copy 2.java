// // package com.fdjloto.api.controller;

// // import org.springframework.http.MediaType;
// // import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.RestController;

// // @RestController
// // public class AdminLoginController {

// //     @GetMapping(value = "/admin-login.html", produces = MediaType.TEXT_HTML_VALUE)
// //     public String adminLoginPage() {

// //         return """
// // <!DOCTYPE html>
// // <html lang="fr">
// // <head>
// //     <meta charset="UTF-8">
// //     <title>Login admin - Swagger</title>
// //     <meta name="viewport" content="width=device-width, initial-scale=1.0">
// // </head>
// // <body style="background:#020617;color:#e5e7eb;font-family:system-ui, sans-serif;display:flex;align-items:center;justify-content:center;min-height:100vh;margin:0">
// // <div style="background:#0f172a;border-radius:16px;padding:24px 28px;max-width:380px;width:100%;box-shadow:0 20px 60px rgba(15,23,42,0.9)">
// //     <h1 style="margin-top:0;margin-bottom:12px;font-size:1.4rem;">Connexion admin</h1>
// //     <p style="margin:0 0 18px;font-size:0.9rem;color:#9ca3af;">
// //         Accès réservé à l'administrateur pour consulter la documentation Swagger.
// //     </p>

// //     <form id="adminLoginForm">
// //         <div style="margin-bottom:10px;">
// //             <label for="email" style="display:block;font-size:0.85rem;margin-bottom:4px;">Email admin</label>
// //             <input id="email" type="email" required
// //                    style="width:100%;padding:8px 10px;border-radius:8px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;">
// //         </div>

// //         <div style="margin-bottom:14px;">
// //             <label for="password" style="display:block;font-size:0.85rem;margin-bottom:4px;">Mot de passe</label>
// //             <input id="password" type="password" required
// //                    style="width:100%;padding:8px 10px;border-radius:8px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;">
// //         </div>

// //         <button id="submitBtn" type="submit"
// //                 style="width:100%;padding:9px 12px;border-radius:999px;border:none;background:#22c55e;color:#020617;font-weight:600;cursor:pointer;">
// //             Se connecter
// //         </button>
// //     </form>

// //     <p id="errorMsg" style="margin-top:10px;font-size:0.85rem;color:#f97373;display:none;"></p>

// //     <p style="margin-top:14px;font-size:0.8rem;color:#6b7280;">
// //         Accès bloqué après 1 tentative.
// //     </p>
// //     <p style="margin-top:4px;font-size:0.8rem;color:#6b7280;">
// //         Après connexion réussie, vous serez redirigé automatiquement vers Swagger UI.
// //     </p>
// // </div>

// // <script>
// //     // 🔧 BASE URL dynamique : local vs prod
// //     const API_BASE =
// //         window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
// //             ? "http://localhost:8082"
// //             : "https://stephanedinahet.fr";

// //     const form = document.getElementById("adminLoginForm");
// //     const errorMsg = document.getElementById("errorMsg");
// //     const submitBtn = document.getElementById("submitBtn");

// //     form.addEventListener("submit", async function (e) {
// //         e.preventDefault();
// //         const email = document.getElementById("email").value.trim();
// //         const password = document.getElementById("password").value.trim();
// //         errorMsg.style.display = "none";

// //         try {
// //             const res = await fetch(`${API_BASE}/api/auth/login-swagger`, {
// //                 method: "POST",
// //                 headers: { "Content-Type": "application/json" },
// //                 credentials: "include",
// //                 body: JSON.stringify({ email, password })
// //             });

// //             // 🔒 Cas 403 : trop de tentatives → on bloque l'UI
// //             if (res.status === 403) {
// //                 let data = {};
// //                 try { data = await res.json(); } catch(e) {}
// //                 errorMsg.textContent = data.message || "Accès bloqué après 1 tentative. Contactez l'administrateur.";
// //                 errorMsg.style.display = "block";

// //                 // Désactivation du formulaire
// //                 submitBtn.disabled = true;
// //                 submitBtn.style.opacity = "0.5";
// //                 submitBtn.style.cursor = "not-allowed";
// //                 document.getElementById("email").disabled = true;
// //                 document.getElementById("password").disabled = true;

// //                 // 👉 si tu préfères rediriger vers ta page 403 :
// //                 // window.location.href = API_BASE + "/403";
// //                 return;
// //             }

// //             // ❌ Autres erreurs (401…) → on affiche le message + tentatives restantes
// //             if (!res.ok) {
// //                 let data = {};
// //                 try { data = await res.json(); } catch(e) {}
// //                 if (data.tentativesRestantes !== undefined) {
// //                     errorMsg.textContent =
// //                         (data.message || "Identifiants invalides.") +
// //                         " Tentatives restantes : " + data.tentativesRestantes;
// //                 } else {
// //                     errorMsg.textContent = data.message || "Identifiants invalides ou droits insuffisants.";
// //                 }
// //                 errorMsg.style.display = "block";
// //                 return;
// //             }

// //             // ✅ Login OK → on renvoie vers Swagger UI
// //             window.location.href = API_BASE + "/swagger-ui/index.html";

// //         } catch (err) {
// //             console.error(err);
// //             errorMsg.textContent = "Erreur réseau, réessayez plus tard.";
// //             errorMsg.style.display = "block";
// //         }
// //     });
// // </script>
// // </body>
// // </html>
// // """;
// //     }
// // }

// package com.fdjloto.api.controller;

// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class AdminLoginController {

//     @GetMapping(value = "/admin-login.html", produces = MediaType.TEXT_HTML_VALUE)
//     public String adminLoginPage() {

//         return """
// <!DOCTYPE html>
// <html lang="fr">
// <head>
//     <meta charset="UTF-8">
//     <title>Login admin - Swagger</title>
//     <meta name="viewport" content="width=device-width, initial-scale=1.0">
// </head>
// <body style="background:#020617;color:#e5e7eb;font-family:system-ui, sans-serif;display:flex;align-items:center;justify-content:center;min-height:100vh;margin:0">
// <div style="background:#0f172a;border-radius:16px;padding:24px 28px;max-width:380px;width:100%;box-shadow:0 20px 60px rgba(15,23,42,0.9)">
//     <h1 style="margin-top:0;margin-bottom:12px;font-size:1.4rem;">Connexion admin</h1>
//     <p style="margin:0 0 18px;font-size:0.9rem;color:#9ca3af;">
//         Accès réservé à l'administrateur pour consulter la documentation Swagger.
//     </p>

//     <form id="adminLoginForm">
//         <div style="margin-bottom:10px;">
//             <label for="email" style="display:block;font-size:0.85rem;margin-bottom:4px;">Email admin</label>
//             <input id="email" type="email" required
//                    style="width:100%;padding:8px 10px;border-radius:8px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;">
//         </div>

//         <div style="margin-bottom:14px;">
//             <label for="password" style="display:block;font-size:0.85rem;margin-bottom:4px;">Mot de passe</label>
//             <input id="password" type="password" required
//                    style="width:100%;padding:8px 10px;border-radius:8px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;">
//         </div>

//         <!-- 🔐 CAPTCHA avancé -->
//         <div style="margin-bottom:14px;">
//             <label style="display:block;font-size:0.85rem;margin-bottom:6px;">
//                 Vérification anti-robot
//             </label>

//             <div style="display:flex;align-items:center;gap:10px;margin-bottom:8px;">
//                 <canvas id="captchaCanvas" width="140" height="45"
//                         style="border-radius:8px;border:1px solid #4b5563;background:#020617;cursor:not-allowed;"></canvas>

//                 <button type="button" id="refreshCaptcha"
//                         style="padding:6px 10px;border-radius:999px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;cursor:pointer;">
//                     ↻
//                 </button>
//             </div>

//             <input id="captchaAnswer" type="text" required
//                    placeholder="Recopiez le code"
//                    autocomplete="off" spellcheck="false"
//                    style="width:100%;padding:8px 10px;border-radius:8px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;">
//         </div>

//         <button id="submitBtn" type="submit"
//                 style="width:100%;padding:9px 12px;border-radius:999px;border:none;background:#22c55e;color:#020617;font-weight:600;cursor:pointer;">
//             Se connecter
//         </button>
//     </form>

//     <p id="errorMsg" style="margin-top:10px;font-size:0.85rem;color:#f97373;display:none;"></p>

//     <p style="margin-top:14px;font-size:0.8rem;color:#6b7280;">
//         Accès bloqué après 3 tentative.
//     </p>
//     <p style="margin-top:4px;font-size:0.8rem;color:#6b7280;">
//         Après connexion réussie, vous serez redirigé automatiquement vers Swagger UI.
//     </p>
// </div>

// <script>
//     // 🔧 BASE URL dynamique : local vs prod
//     const API_BASE =
//         window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
//             ? "http://localhost:8082"
//             : "https://stephanedinahet.fr";

//     const form = document.getElementById("adminLoginForm");
//     const errorMsg = document.getElementById("errorMsg");
//     const submitBtn = document.getElementById("submitBtn");

//     // 🔐 CAPTCHA avancé (canvas + code alphanumérique)
//     const captchaCanvas = document.getElementById("captchaCanvas");
//     const captchaCtx = captchaCanvas.getContext("2d");
//     const captchaInput = document.getElementById("captchaAnswer");
//     const refreshCaptchaBtn = document.getElementById("refreshCaptcha");

//     let captchaCode = "";

//     function randomCaptchaChar() {
//         const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // pas de 0,1,O,I
//         return chars.charAt(Math.floor(Math.random() * chars.length));
//     }

//     function generateCaptcha() {
//         captchaCode = "";
//         captchaCtx.clearRect(0, 0, captchaCanvas.width, captchaCanvas.height);
//         captchaCtx.fillStyle = "#020617";
//         captchaCtx.fillRect(0, 0, captchaCanvas.width, captchaCanvas.height);

//         // Bruit de fond (points)
//         for (let i = 0; i < 40; i++) {
//             captchaCtx.fillStyle = `rgba(255,255,255,${Math.random() * 0.2})`;
//             captchaCtx.beginPath();
//             captchaCtx.arc(Math.random() * captchaCanvas.width, Math.random() * captchaCanvas.height, 1.2, 0, Math.PI * 2);
//             captchaCtx.fill();
//         }

//         // Lignes parasites
//         for (let i = 0; i < 5; i++) {
//             captchaCtx.strokeStyle = `rgba(148,163,184,${0.3 + Math.random() * 0.3})`;
//             captchaCtx.beginPath();
//             captchaCtx.moveTo(Math.random() * captchaCanvas.width, Math.random() * captchaCanvas.height);
//             captchaCtx.lineTo(Math.random() * captchaCanvas.width, Math.random() * captchaCanvas.height);
//             captchaCtx.stroke();
//         }

//         // Caractères déformés
//         for (let i = 0; i < 6; i++) {
//             const char = randomCaptchaChar();
//             captchaCode += char;

//             const x = 15 + i * 20;
//             const y = 30 + (Math.random() * 6 - 3); // léger décalage vertical
//             const angle = (Math.random() - 0.5) * 0.6; // rotation

//             captchaCtx.save();
//             captchaCtx.translate(x, y);
//             captchaCtx.rotate(angle);
//             captchaCtx.font = "bold 22px Arial";
//             captchaCtx.fillStyle = "#e5e7eb";
//             captchaCtx.fillText(char, 0, 0);
//             captchaCtx.restore();
//         }

//         captchaInput.value = "";
//     }

//     refreshCaptchaBtn.addEventListener("click", function () {
//         generateCaptcha();
//         errorMsg.style.display = "none";
//     });

//     // Génère un premier captcha au chargement
//     generateCaptcha();

//     form.addEventListener("submit", async function (e) {
//         e.preventDefault();
//         const email = document.getElementById("email").value.trim();
//         const password = document.getElementById("password").value.trim();
//         const captchaValue = captchaInput.value.trim().toUpperCase();
//         errorMsg.style.display = "none";

//         // 🛑 Vérification CAPTCHA avant l'appel API
//         if (!captchaValue) {
//             errorMsg.textContent = "Veuillez recopier le code de sécurité.";
//             errorMsg.style.display = "block";
//             return;
//         }

//         if (captchaValue !== captchaCode) {
//             errorMsg.textContent = "Captcha invalide. Veuillez réessayer.";
//             errorMsg.style.display = "block";
//             generateCaptcha();
//             return;
//         }

//         try {
//             const res = await fetch(`${API_BASE}/api/auth/login-swagger`, {
//                 method: "POST",
//                 headers: { "Content-Type": "application/json" },
//                 credentials: "include",
//                 body: JSON.stringify({ email, password })
//             });

//             // 🔒 Cas 403 : trop de tentatives → on bloque l'UI
//             if (res.status === 403) {
//                 let data = {};
//                 try { data = await res.json(); } catch(e) {}
//                 errorMsg.textContent = data.message || "Accès bloqué après 3 tentative. Contactez l'administrateur.";
//                 errorMsg.style.display = "block";

//                 // Désactivation du formulaire + captcha
//                 submitBtn.disabled = true;
//                 submitBtn.style.opacity = "0.5";
//                 submitBtn.style.cursor = "not-allowed";
//                 document.getElementById("email").disabled = true;
//                 document.getElementById("password").disabled = true;
//                 captchaInput.disabled = true;
//                 refreshCaptchaBtn.disabled = true;
//                 refreshCaptchaBtn.style.opacity = "0.5";
//                 refreshCaptchaBtn.style.cursor = "not-allowed";

//                 // 👉 si tu préfères rediriger vers ta page 403 :
//                 // window.location.href = API_BASE + "/errors/403.html";
//                 return;
//             }

//             // ❌ Autres erreurs (401…) → on affiche le message + tentatives restantes
//             if (!res.ok) {
//                 let data = {};
//                 try { data = await res.json(); } catch(e) {}
//                 if (data.tentativesRestantes !== undefined) {
//                     errorMsg.textContent =
//                         (data.message || "Identifiants invalides.") +
//                         " Tentatives restantes : " + data.tentativesRestantes;
//                 } else {
//                     errorMsg.textContent = data.message || "Identifiants invalides ou droits insuffisants.";
//                 }
//                 errorMsg.style.display = "block";
//                 generateCaptcha();
//                 return;
//             }

//             // ✅ Login OK → on renvoie vers Swagger UI
//             window.location.href = API_BASE + "/swagger-ui/index.html";

//         } catch (err) {
//             console.error(err);
//             errorMsg.textContent = "Erreur réseau, réessayez plus tard.";
//             errorMsg.style.display = "block";
//         }
//     });
// </script>
// </body>
// </html>
// """;
//     }
// }
