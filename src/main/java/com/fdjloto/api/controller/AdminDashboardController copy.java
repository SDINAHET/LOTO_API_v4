// package com.fdjloto.api.controller;

// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.io.IOException;
// import java.nio.file.*;
// import java.util.List;

// @RestController
// @RequestMapping("/admin")
// public class AdminDashboardController {

//     // ⚠️ adapte ce chemin à ton fichier de logs réel
//     private static final Path LOG_PATH = Paths.get("logs/loto-api-dev.log");

//     @GetMapping(value = "/dashboard", produces = MediaType.TEXT_HTML_VALUE)
//     public String adminDashboard() {
//         return """
// <!DOCTYPE html>
// <html lang="fr">
// <head>
//     <meta charset="UTF-8">
//     <title>Dashboard admin - Loto Tracker</title>
//     <meta name="viewport" content="width=device-width, initial-scale=1.0">
// </head>
// <body style="background:#020617;color:#e5e7eb;font-family:system-ui, sans-serif;margin:0;">
// <div style="min-height:100vh;display:flex;flex-direction:column;">

//     <!-- HEADER -->
//     <header style="padding:16px 20px;border-bottom:1px solid #111827;display:flex;justify-content:space-between;align-items:center;">
//         <div>
//             <h1 style="margin:0;font-size:1.4rem;">Tableau de bord administrateur</h1>
//             <p style="margin:2px 0 0;font-size:0.8rem;color:#9ca3af;">
//                 Vous êtes connecté en tant qu'administrateur.
//             </p>
//         </div>
//         <button id="btnLogout" type="button"
//                 style="padding:6px 14px;border-radius:999px;border:1px solid #4b5563;background:#020617;color:#e5e7eb;font-size:0.8rem;cursor:pointer;">
//             Déconnexion
//         </button>
//     </header>

//     <!-- TABS -->
//     <nav style="padding:10px 20px;border-bottom:1px solid #111827;display:flex;gap:10px;font-size:0.85rem;">
//         <button class="tab-btn active" data-tab="swagger"
//                 style="padding:6px 12px;border-radius:999px;border:1px solid #4b5563;background:#1f2937;color:#e5e7eb;cursor:pointer;">
//             📚 Swagger / API
//         </button>
//         <button class="tab-btn" data-tab="logs"
//                 style="padding:6px 12px;border-radius:999px;border:1px solid #4b5563;background:#020617;color:#9ca3af;cursor:pointer;">
//             📜 Logs serveur
//         </button>
//         <button class="tab-btn" data-tab="db"
//                 style="padding:6px 12px;border-radius:999px;border:1px solid #4b5563;background:#020617;color:#9ca3af;cursor:pointer;">
//             🗄 Base de données
//         </button>
//     </nav>

//     <!-- CONTENU -->
//     <main style="flex:1;padding:16px 20px;display:flex;flex-direction:column;gap:14px;">

//         <!-- SWAGGER -->
//         <section id="tab-swagger" class="tab-panel" style="flex:1;display:flex;flex-direction:column;gap:8px;">
//             <p style="margin:0 0 4px;font-size:0.85rem;color:#9ca3af;">
//                 Documentation Swagger de l'API Loto Tracker.
//             </p>
//             <div style="display:flex;gap:8px;align-items:center;">
//                 <button id="btnOpenSwagger" type="button"
//                         style="padding:6px 12px;border-radius:999px;border:none;background:#22c55e;color:#020617;font-weight:600;cursor:pointer;font-size:0.8rem;">
//                     Ouvrir Swagger dans un nouvel onglet
//                 </button>
//                 <span style="font-size:0.8rem;color:#6b7280;">ou utilisez la vue intégrée ci-dessous.</span>
//             </div>
//             <div style="flex:1;min-height:0;margin-top:8px;">
//                 <iframe id="swaggerFrame"
//                         src="/swagger-ui/index.html"
//                         style="width:100%;height:100%;border-radius:12px;border:1px solid #111827;background:#020617;"></iframe>
//             </div>
//         </section>

//         <!-- LOGS -->
//         <section id="tab-logs" class="tab-panel" style="flex:1;display:none;flex-direction:column;gap:8px;">
//             <div style="display:flex;flex-wrap:wrap;gap:10px;align-items:center;font-size:0.8rem;">
//                 <span style="color:#9ca3af;">Logs du backend (lecture seule).</span>
//                 <button id="btnRefreshLogs" type="button"
//                         style="padding:5px 10px;border-radius:999px;border:none;background:#22c55e;color:#020617;font-weight:600;cursor:pointer;">
//                     🔄 Rafraîchir
//                 </button>
//                 <label style="color:#9ca3af;">
//                     Lignes :
//                     <select id="logLineCount"
//                             style="background:#020617;color:#e5e7eb;border-radius:999px;border:1px solid #4b5563;padding:3px 8px;">
//                         <option value="200">200</option>
//                         <option value="400" selected>400</option>
//                         <option value="800">800</option>
//                     </select>
//                 </label>
//                 <label style="display:flex;align-items:center;gap:4px;color:#9ca3af;">
//                     <input type="checkbox" id="autoScrollLogs" checked>
//                     Auto-scroll
//                 </label>
//                 <label style="display:flex;align-items:center;gap:4px;color:#9ca3af;">
//                     <input type="checkbox" id="autoRefreshLogs" checked>
//                     Auto-refresh (3s)
//                 </label>
//                 <span id="logsLastRefresh" style="color:#6b7280;">Dernier refresh : -</span>
//             </div>

//             <div id="logsContainer"
//                  style="flex:1;min-height:0;overflow-y:auto;border-radius:12px;border:1px solid #111827;background:#020617;padding:10px;font-family:monospace;font-size:11px;white-space:pre-wrap;">
//                 Chargement des logs...
//             </div>
//         </section>

//         <!-- BDD -->
//         <section id="tab-db" class="tab-panel" style="flex:1;display:none;flex-direction:column;gap:8px;font-size:0.85rem;">
//             <div style="display:flex;flex-wrap:wrap;gap:8px;align-items:center;">
//                 <span style="color:#9ca3af;">Vue rapide des données principales (lecture / suppression).</span>
//                 <label style="color:#9ca3af;">
//                     Ressource :
//                     <select id="dbResourceSelect"
//                             style="background:#020617;color:#e5e7eb;border-radius:999px;border:1px solid #4b5563;padding:3px 8px;">
//                         <option value="users">Utilisateurs</option>
//                         <option value="tickets">Tickets</option>
//                         <option value="predictions">Prédictions</option>
//                     </select>
//                 </label>
//                 <button id="btnLoadData" type="button"
//                         style="padding:5px 10px;border-radius:999px;border:none;background:#22c55e;color:#020617;font-weight:600;cursor:pointer;">
//                     Charger
//                 </button>
//             </div>

//             <div style="color:#6b7280;font-size:0.8rem;">
//                 ⚠️ Interface simplifiée. Tu pourras l'étendre (édition, création, filtres…) plus tard.
//             </div>

//             <div id="dbTableWrapper"
//                  style="flex:1;min-height:0;overflow:auto;border-radius:12px;border:1px solid #111827;background:#020617;">
//                 <table id="dbTable" style="border-collapse:collapse;width:100%;font-size:0.8rem;">
//                     <thead id="dbTableHead"></thead>
//                     <tbody id="dbTableBody">
//                     </tbody>
//                 </table>
//             </div>
//         </section>

//     </main>
// </div>

// <script>
//     const API_BASE = window.location.origin;

//     // ------------ TABS ------------
//     const tabButtons = document.querySelectorAll(".tab-btn");
//     const tabPanels = {
//         swagger: document.getElementById("tab-swagger"),
//         logs: document.getElementById("tab-logs"),
//         db: document.getElementById("tab-db")
//     };

//     tabButtons.forEach(btn => {
//         btn.addEventListener("click", () => {
//             const tab = btn.getAttribute("data-tab");
//             tabButtons.forEach(b => {
//                 b.classList.remove("active");
//                 b.style.background = "#020617";
//                 b.style.color = "#9ca3af";
//             });
//             btn.classList.add("active");
//             btn.style.background = "#1f2937";
//             btn.style.color = "#e5e7eb";

//             Object.keys(tabPanels).forEach(key => {
//                 tabPanels[key].style.display = (key === tab) ? "flex" : "none";
//             });
//         });
//     });

//     // ------------ SWAGGER ------------
//     document.getElementById("btnOpenSwagger").addEventListener("click", () => {
//         window.open("/swagger-ui/index.html", "_blank");
//     });

//     // ------------ LOGS ------------
//     const logsContainer = document.getElementById("logsContainer");
//     const btnRefreshLogs = document.getElementById("btnRefreshLogs");
//     const logLineCountSelect = document.getElementById("logLineCount");
//     const autoScrollLogsCheckbox = document.getElementById("autoScrollLogs");
//     const autoRefreshLogsCheckbox = document.getElementById("autoRefreshLogs");
//     const logsLastRefresh = document.getElementById("logsLastRefresh");

//     async function refreshLogs() {
//         const lines = logLineCountSelect.value;
//         try {
//             const res = await fetch(`/admin/logs?lines=${lines}`);
//             const text = await res.text();
//             logsContainer.textContent = text || "(Aucun log pour le moment)";
//             const now = new Date();
//             logsLastRefresh.textContent = "Dernier refresh : " + now.toLocaleTimeString("fr-FR");
//             if (autoScrollLogsCheckbox.checked) {
//                 logsContainer.scrollTop = logsContainer.scrollHeight;
//             }
//         } catch (e) {
//             console.error(e);
//             logsContainer.textContent += "\\n[ERREUR] Impossible de charger les logs.";
//         }
//     }

//     btnRefreshLogs.addEventListener("click", refreshLogs);
//     logLineCountSelect.addEventListener("change", refreshLogs);
//     setInterval(() => {
//         if (autoRefreshLogsCheckbox.checked) {
//             refreshLogs();
//         }
//     }, 3000);

//     // ------------ BDD ------------
//     const dbResourceSelect = document.getElementById("dbResourceSelect");
//     const btnLoadData = document.getElementById("btnLoadData");
//     const dbTableHead = document.getElementById("dbTableHead");
//     const dbTableBody = document.getElementById("dbTableBody");

//     // adapte ces endpoints à ton API réelle
//     const DB_ENDPOINTS = {
//         users: "/api/admin/users",
//         tickets: "/api/admin/tickets",
//         predictions: "/api/admin/predictions"
//     };

//     async function loadDbData() {
//         const resource = dbResourceSelect.value;
//         const url = DB_ENDPOINTS[resource];

//         dbTableHead.innerHTML = "";
//         dbTableBody.innerHTML = "<tr><td style='padding:8px;color:#9ca3af;'>Chargement...</td></tr>";

//         if (!url) {
//             dbTableBody.innerHTML = "<tr><td style='padding:8px;color:#f97373;'>Endpoint non configuré.</td></tr>";
//             return;
//         }

//         try {
//             const res = await fetch(url, { credentials: "include" });
//             if (!res.ok) {
//                 dbTableBody.innerHTML = "<tr><td style='padding:8px;color:#f97373;'>Erreur " + res.status + ".</td></tr>";
//                 return;
//             }
//             const data = await res.json();
//             if (!Array.isArray(data) || data.length === 0) {
//                 dbTableBody.innerHTML = "<tr><td style='padding:8px;color:#9ca3af;'>Aucun enregistrement.</td></tr>";
//                 return;
//             }

//             const keys = Object.keys(data[0]);

//             const headerRow = document.createElement("tr");
//             keys.forEach(k => {
//                 const th = document.createElement("th");
//                 th.textContent = k;
//                 th.style.padding = "6px 8px";
//                 th.style.borderBottom = "1px solid #111827";
//                 th.style.textAlign = "left";
//                 headerRow.appendChild(th);
//             });
//             const thActions = document.createElement("th");
//             thActions.textContent = "Actions";
//             thActions.style.padding = "6px 8px";
//             thActions.style.borderBottom = "1px solid #111827";
//             headerRow.appendChild(thActions);
//             dbTableHead.innerHTML = "";
//             dbTableHead.appendChild(headerRow);

//             dbTableBody.innerHTML = "";
//             data.forEach(row => {
//                 const tr = document.createElement("tr");
//                 tr.style.borderBottom = "1px solid #020617";

//                 keys.forEach(k => {
//                     const td = document.createElement("td");
//                     let value = row[k];
//                     if (value === null || value === undefined) value = "";
//                     if (typeof value === "object") value = JSON.stringify(value);
//                     td.textContent = String(value);
//                     td.style.padding = "6px 8px";
//                     td.style.verticalAlign = "top";
//                     td.style.maxWidth = "260px";
//                     td.style.wordBreak = "break-word";
//                     tr.appendChild(td);
//                 });

//                 const tdActions = document.createElement("td");
//                 tdActions.style.padding = "6px 8px";
//                 const btnDelete = document.createElement("button");
//                 btnDelete.textContent = "Supprimer";
//                 btnDelete.style.padding = "3px 8px";
//                 btnDelete.style.borderRadius = "999px";
//                 btnDelete.style.border = "none";
//                 btnDelete.style.fontSize = "0.75rem";
//                 btnDelete.style.background = "#ef4444";
//                 btnDelete.style.color = "#020617";
//                 btnDelete.style.cursor = "pointer";

//                 btnDelete.addEventListener("click", async () => {
//                     const id = row.id || row._id;
//                     if (!id) { alert("ID introuvable."); return; }
//                     if (!confirm("Supprimer " + resource + " #" + id + " ?")) return;

//                     try {
//                         const delRes = await fetch(url + "/" + id, {
//                             method: "DELETE",
//                             credentials: "include"
//                         });
//                         if (!delRes.ok) {
//                             alert("Erreur " + delRes.status + " lors de la suppression.");
//                             return;
//                         }
//                         tr.remove();
//                     } catch (e) {
//                         console.error(e);
//                         alert("Erreur réseau lors de la suppression.");
//                     }
//                 });

//                 tdActions.appendChild(btnDelete);
//                 tr.appendChild(tdActions);
//                 dbTableBody.appendChild(tr);
//             });

//         } catch (e) {
//             console.error(e);
//             dbTableBody.innerHTML = "<tr><td style='padding:8px;color:#f97373;'>Erreur réseau.</td></tr>";
//         }
//     }

//     btnLoadData.addEventListener("click", loadDbData);

//     // ------------ LOGOUT ------------
//     document.getElementById("btnLogout").addEventListener("click", async () => {
//         try {
//             await fetch("/api/auth/logout", { method: "POST", credentials: "include" });
//         } catch (e) {
//             console.error(e);
//         }
//         window.location.href = "/admin-login.html";
//     });

//     // premier chargement : on rafraîchit les logs
//     refreshLogs();
// </script>
// </body>
// </html>
// """;
//     }

//     // Endpoint pour lire les logs (appelé par JS)
//     @GetMapping(value = "/logs", produces = MediaType.TEXT_PLAIN_VALUE)
//     public ResponseEntity<String> getLogs(@RequestParam(defaultValue = "400") int lines) throws IOException {
//         if (!Files.exists(LOG_PATH)) {
//             return ResponseEntity.ok("Fichier de log introuvable : " + LOG_PATH.toAbsolutePath());
//         }
//         List<String> all = Files.readAllLines(LOG_PATH);
//         int fromIndex = Math.max(0, all.size() - lines);
//         return ResponseEntity.ok(String.join("\n", all.subList(fromIndex, all.size())));
//     }
// }
