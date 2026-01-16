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

//     // ⚠️ adapte ce chemin à ton fichier de log réel
//     private static final Path LOG_PATH = Paths.get("logs/loto-api-dev.log");

//     @GetMapping(value = "/dashboard", produces = MediaType.TEXT_HTML_VALUE)
//     public String adminDashboard() {

//         return """
// <!DOCTYPE html>
// <html lang="fr">
// <head>
//     <meta charset="UTF-8">
//     <title>Dashboard admin - Loto Tracker</title>
//     <link rel="icon" href="http://localhost:5500/favicon-admin.ico">
//     <link rel="icon" href="https://stephanedinahet.fr/favicon-admin.ico">
//     <meta name="viewport" content="width=device-width, initial-scale=1.0">
//     <style>
//         /* Style de base du menu */
//         .nav-item {
//             width: 100%;
//             display: flex;
//             align-items: center;
//             gap: 8px;
//             padding: 8px 10px;
//             border-radius: 10px;
//             border: none;
//             cursor: pointer;
//             text-align: left;

//             background: transparent;
//             color: #9ca3af;
//             border-left: 3px solid transparent;

//             font-size: 0.85rem;
//             transition:
//                 background 0.18s ease,
//                 color 0.18s ease,
//                 border-color 0.18s ease,
//                 transform 0.10s ease;
//         }

//         /* Hover bien plus intense */
//         .nav-item:hover {
//             background: #1e293b;          /* fond plus visible */
//             color: #e5e7eb;               /* texte plus clair */
//             border-left-color: #22c55e;   /* liseré vert */
//             transform: translateX(2px);   /* petit slide à droite */
//         }

//         /* Onglet sélectionné (actif) */
//         .nav-item.active {
//             background: #334155;          /* encore plus contrasté */
//             color: #f9fafb;
//             font-weight: 600;
//             border-left-color: #22c55e;   /* liseré vert permanent */
//         }
//     </style>

// </head>
// <body style="background:#020617;color:#e5e7eb;font-family:system-ui, sans-serif;margin:0;">
// <div style="min-height:100vh;display:flex;">

//     <!-- SIDEBAR GAUCHE -->
//     <aside style="width:240px;background:#020617;border-right:1px solid #111827;
//                   padding:18px 14px;box-sizing:border-box;display:flex;flex-direction:column;gap:18px;
//                   position:sticky;top:0;align-self:flex-start;height:100vh;">
//         <!-- Logo / titre -->
//         <div>
//             <div style="font-size:0.75rem;color:#6b7280;text-transform:uppercase;letter-spacing:.08em;">
//                 Loto Tracker API
//             </div>
//             <div style="margin-top:4px;font-size:1.1rem;font-weight:600;">
//                 Admin console
//             </div>
//         </div>

//         <!-- Menu -->
//         <nav style="display:flex;flex-direction:column;gap:6px;font-size:0.85rem;">
//             <button class="nav-item active" data-section="swagger">
//                 <span>*</span>
//                 <span>Swagger / API</span>
//             </button>
//             <button class="nav-item" data-section="logs">
//                 <span>*</span>
//                 <span>Logs serveur</span>
//             </button>
//             <button class="nav-item" data-section="db">
//                 <span>*</span>
//                 <span>Base de données (CRUD)</span>
//             </button>
//             <button class="nav-item" data-section="stats">
//                 <span>*</span>
//                 <span>Stats joueurs & tickets</span>
//             </button>
//         </nav>

//         <div style="margin-top:auto;font-size:0.75rem;color:#6b7280;line-height:1.4;">
//             Connecté en tant qu'<span style="color:#e5e7eb;">administrateur</span>.<br>
//             Profil Spring : <span style="color:#22c55e;">dev</span>
//         </div>
//     </aside>

//     <!-- COLONNE CONTENU -->
//     <div style="flex:1;display:flex;flex-direction:column;min-width:0;">

//         <!-- HEADER -->
//         <header style="padding:14px 20px;border-bottom:1px solid #111827;
//                        display:flex;justify-content:space-between;align-items:center;">
//             <div>
//                 <h1 id="pageTitle" style="margin:0;font-size:1.3rem;">Tableau de bord administrateur</h1>
//                 <p id="pageSubtitle" style="margin:2px 0 0;font-size:0.8rem;color:#9ca3af;">
//                     Vue générale : Swagger, logs serveur, base de données et statistiques.
//                 </p>
//             </div>
//             <button id="btnLogout" type="button"
//                     style="padding:6px 14px;border-radius:999px;border:1px solid #4b5563;
//                            background:#020617;color:#e5e7eb;font-size:0.8rem;cursor:pointer;">
//                 Déconnexion
//             </button>
//         </header>

//         <!-- CONTENU -->
//         <main style="flex:1;padding:16px 20px;display:flex;flex-direction:column;gap:14px;min-height:0;">

//             <!-- SECTION SWAGGER -->
//             <section id="section-swagger" class="section-panel"
//                      style="flex:1;display:flex;flex-direction:column;gap:8px;min-height:0;">
//                 <p style="margin:0 0 4px;font-size:0.85rem;color:#9ca3af;">
//                     Documentation Swagger de l'API Loto Tracker.
//                 </p>
//                 <div style="display:flex;gap:8px;align-items:center;flex-wrap:wrap;">
//                     <button id="btnOpenSwagger" type="button"
//                             style="padding:6px 12px;border-radius:999px;border:none;background:#22c55e;
//                                    color:#020617;font-weight:600;cursor:pointer;font-size:0.8rem;">
//                         Ouvrir Swagger dans un nouvel onglet
//                     </button>
//                     <span style="font-size:0.8rem;color:#6b7280;">
//                         ou utilisez la vue intégrée ci-dessous.
//                     </span>
//                 </div>
//                 <div style="flex:1;min-height:0;margin-top:8px;">
//                     <iframe id="swaggerFrame"
//                             src="/swagger-ui/index.html"
//                             style="width:100%;height:100%;border-radius:12px;border:1px solid #111827;
//                                    background:#020617;"></iframe>
//                 </div>
//             </section>

//             <!-- SECTION LOGS -->
//             <section id="section-logs" class="section-panel"
//                      style="flex:1;display:none;flex-direction:column;gap:8px;min-height:0;">
//                 <div style="display:flex;flex-wrap:wrap;gap:10px;align-items:center;font-size:0.8rem;">
//                     <span style="color:#9ca3af;">Logs du backend (lecture seule).</span>
//                     <button id="btnRefreshLogs" type="button"
//                             style="padding:5px 10px;border-radius:999px;border:none;background:#22c55e;
//                                    color:#020617;font-weight:600;cursor:pointer;">
//                         🔄 Rafraîchir
//                     </button>
//                     <label style="color:#9ca3af;">
//                         Lignes :
//                         <select id="logLineCount"
//                                 style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                        border:1px solid #4b5563;padding:3px 8px;">
//                             <option value="200">200</option>
//                             <option value="400" selected>400</option>
//                             <option value="800">800</option>
//                         </select>
//                     </label>
//                     <label style="display:flex;align-items:center;gap:4px;color:#9ca3af;">
//                         <input type="checkbox" id="autoScrollLogs" checked>
//                         Auto-scroll
//                     </label>
//                     <label style="display:flex;align-items:center;gap:4px;color:#9ca3af;">
//                         <input type="checkbox" id="autoRefreshLogs" checked>
//                         Auto-refresh (3s)
//                     </label>
//                     <span id="logsLastRefresh" style="color:#6b7280;">Dernier refresh : -</span>
//                 </div>

//                 <div id="logsContainer"
//                      style="flex:1;min-height:0;overflow-y:auto;border-radius:12px;border:1px solid #111827;
//                             background:#020617;padding:10px;font-family:monospace;font-size:11px;
//                             white-space:pre-wrap;">
//                     Chargement des logs...
//                 </div>
//             </section>

//             <!-- SECTION BASE DE DONNÉES / CRUD -->
//             <section id="section-db" class="section-panel"
//                      style="flex:1;display:none;flex-direction:column;gap:10px;min-height:0;font-size:0.85rem;">
//                 <div style="display:flex;flex-wrap:wrap;gap:8px;align-items:center;">
//                     <span style="color:#9ca3af;">Vue base de données (CRUD simplifié).</span>
//                     <label style="color:#9ca3af;">
//                         Table :
//                         <select id="dbResourceSelect"
//                                 style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                        border:1px solid #4b5563;padding:3px 8px;">
//                             <option value="tickets">public.tickets</option>
//                             <option value="users">public.users</option>
//                             <option value="ticket_gains">public.ticket_gains</option>
//                             <option value="refresh_tokens">public.refresh_tokens</option>
//                         </select>
//                     </label>
//                     <button id="btnLoadData" type="button"
//                             style="padding:5px 10px;border-radius:999px;border:none;background:#22c55e;
//                                    color:#020617;font-weight:600;cursor:pointer;">
//                         Charger
//                     </button>
//                     <button id="btnNewRow" type="button"
//                             style="padding:5px 10px;border-radius:999px;border:1px solid #4b5563;
//                                    background:#020617;color:#e5e7eb;cursor:pointer;">
//                         ➕ Nouvelle ligne
//                     </button>
//                     <span id="dbInfoText" style="color:#6b7280;font-size:0.8rem;">
//                         Lecture / édition / suppression. L’ID et les dates sont protégés par défaut.
//                     </span>
//                 </div>

//                 <!-- Zone recherche & pagination client -->
//                 <div style="display:flex;flex-wrap:wrap;gap:8px;align-items:center;font-size:0.8rem;">
//                     <label style="color:#9ca3af;">
//                         Recherche :
//                         <input id="dbSearch" type="text" placeholder="Texte à filtrer..."
//                                style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                       border:1px solid #4b5563;padding:4px 10px;font-size:0.8rem;">
//                     </label>
//                     <label style="color:#9ca3af;">
//                         Lignes / page :
//                         <select id="dbPageSize"
//                                 style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                        border:1px solid #4b5563;padding:3px 8px;">
//                             <option value="20" selected>20</option>
//                             <option value="50">50</option>
//                             <option value="100">100</option>
//                         </select>
//                     </label>
//                     <span id="dbPagingInfo" style="color:#6b7280;">Page 1 / 1</span>
//                     <div style="display:flex;gap:4px;">
//                         <button id="dbPrevPage" type="button"
//                                 style="padding:3px 8px;border-radius:999px;border:1px solid #4b5563;
//                                        background:#020617;color:#e5e7eb;font-size:0.75rem;cursor:pointer;">
//                             ◀
//                         </button>
//                         <button id="dbNextPage" type="button"
//                                 style="padding:3px 8px;border-radius:999px;border:1px solid #4b5563;
//                                        background:#020617;color:#e5e7eb;font-size:0.75rem;cursor:pointer;">
//                             ▶
//                         </button>
//                     </div>
//                 </div>

//                 <div id="dbTableWrapper"
//                      style="flex:1;min-height:0;overflow:auto;border-radius:12px;border:1px solid #111827;
//                             background:#020617;">
//                     <table id="dbTable" style="border-collapse:collapse;width:100%;font-size:0.8rem;">
//                         <thead id="dbTableHead"></thead>
//                         <tbody id="dbTableBody"></tbody>
//                     </table>
//                 </div>

//                 <!-- Modal création / édition -->
//                 <div id="dbModalOverlay"
//                      style="display:none;position:fixed;inset:0;background:rgba(15,23,42,0.7);
//                             align-items:center;justify-content:center;z-index:40;">
//                     <div style="background:#020617;border-radius:16px;border:1px solid #111827;
//                                 padding:16px 18px;width:420px;max-width:95%;">
//                         <h2 id="dbModalTitle" style="margin:0 0 8px;font-size:1rem;">Nouvelle ligne</h2>
//                         <form id="dbModalForm" style="display:flex;flex-direction:column;gap:8px;max-height:60vh;overflow:auto;">
//                             <!-- champs dynamiques ici -->
//                         </form>
//                         <div style="margin-top:10px;display:flex;justify-content:flex-end;gap:8px;">
//                             <button id="dbModalCancel" type="button"
//                                     style="padding:5px 10px;border-radius:999px;border:1px solid #4b5563;
//                                            background:#020617;color:#e5e7eb;font-size:0.8rem;cursor:pointer;">
//                                 Annuler
//                             </button>
//                             <button id="dbModalSave" type="button"
//                                     style="padding:5px 12px;border-radius:999px;border:none;background:#22c55e;
//                                            color:#020617;font-weight:600;font-size:0.8rem;cursor:pointer;">
//                                 Enregistrer
//                             </button>
//                         </div>
//                     </div>
//                 </div>
//             </section>

//             <!-- SECTION STATS JOUEURS / TICKETS -->
//             <section id="section-stats" class="section-panel"
//                      style="flex:1;display:none;flex-direction:column;gap:10px;min-height:0;font-size:0.85rem;">
//                 <div style="display:flex;flex-wrap:wrap;gap:8px;align-items:center;">
//                     <span style="color:#9ca3af;">Analyse des joueurs et de leurs tickets.</span>
//                     <button id="btnLoadStats" type="button"
//                             style="padding:5px 10px;border-radius:999px;border:none;background:#22c55e;
//                                    color:#020617;font-weight:600;cursor:pointer;">
//                         Recharger les stats
//                     </button>
//                     <label style="color:#9ca3af;">
//                         Tri :
//                         <select id="statsSort"
//                                 style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                        border:1px solid #4b5563;padding:3px 8px;">
//                             <option value="ticketsDesc">Nb tickets ↓</option>
//                             <option value="gainDesc">Gain total ↓</option>
//                             <option value="lastTicketDesc">Dernier ticket ↓</option>
//                         </select>
//                     </label>
//                     <label style="color:#9ca3af;">
//                         Filtrer par email :
//                         <input id="statsSearch" type="text" placeholder="user@hbnb.com"
//                                style="background:#020617;color:#e5e7eb;border-radius:999px;
//                                       border:1px solid #4b5563;padding:4px 10px;font-size:0.8rem;">
//                     </label>
//                 </div>

//                 <div id="statsGrid"
//                      style="flex:1;min-height:0;overflow:auto;display:grid;
//                             grid-template-columns:repeat(auto-fill,minmax(260px,1fr));
//                             gap:10px;padding-top:4px;">
//                     <!-- cards dynamiques -->
//                 </div>
//             </section>

//         </main>
//     </div>
// </div>

// <script>
//     const API_BASE = window.location.origin;

//     // -------- JWT HEADER --------
//     // ⚠️ adapte "jwtToken" au nom réellement utilisé dans ton front
//     function getAuthHeaders() {
//         const token = localStorage.getItem("jwtToken");
//         if (!token) return {};
//         return { "Authorization": "Bearer " + token };
//     }

//     // ---------- NAVIGATION / SECTIONS ----------
//     const navItems = document.querySelectorAll(".nav-item");
//     const sections = {
//         swagger: document.getElementById("section-swagger"),
//         logs: document.getElementById("section-logs"),
//         db: document.getElementById("section-db"),
//         stats: document.getElementById("section-stats")
//     };
//     const pageTitle = document.getElementById("pageTitle");
//     const pageSubtitle = document.getElementById("pageSubtitle");

//     const subtitles = {
//         swagger: "Documentation Swagger et tests de l’API.",
//         logs: "Suivi en temps réel des logs Spring Boot.",
//         db: "Exploration et modification des tables principales (lecture / écriture).",
//         stats: "Vue synthétique par joueur : tickets, gains, dernier tirage, etc."
//     };

//     function showSection(key) {
//         Object.keys(sections).forEach(k => {
//             sections[k].style.display = (k === key) ? "flex" : "none";
//         });
//         navItems.forEach(btn => {
//             if (btn.getAttribute("data-section") === key) {
//                 btn.classList.add("active");
//                 btn.style.background = "#111827";
//                 btn.style.color = "#e5e7eb";
//             } else {
//                 btn.classList.remove("active");
//                 btn.style.background = "transparent";
//                 btn.style.color = "#9ca3af";
//             }
//         });
//         pageTitle.textContent = "Tableau de bord administrateur";
//         pageSubtitle.textContent = subtitles[key] || "";
//     }

//     navItems.forEach(btn => {
//         btn.addEventListener("click", () => {
//             const section = btn.getAttribute("data-section");
//             showSection(section);
//         });
//     });

//     // ---------- SWAGGER ----------
//     document.getElementById("btnOpenSwagger").addEventListener("click", () => {
//         window.open("/swagger-ui/index.html", "_blank");
//     });

//     // ---------- LOGS ----------
//     const logsContainer = document.getElementById("logsContainer");
//     const btnRefreshLogs = document.getElementById("btnRefreshLogs");
//     const logLineCountSelect = document.getElementById("logLineCount");
//     const autoScrollLogsCheckbox = document.getElementById("autoScrollLogs");
//     const autoRefreshLogsCheckbox = document.getElementById("autoRefreshLogs");
//     const logsLastRefresh = document.getElementById("logsLastRefresh");

//     async function refreshLogs() {
//         const lines = logLineCountSelect.value;
//         try {
//             const res = await fetch(`/admin/logs?lines=${lines}`, {
//                 method: "GET",
//                 headers: {
//                     ...getAuthHeaders()
//                 },
//                 credentials: "include"
//             });
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

//     // ---------- BASE DE DONNÉES / CRUD ----------
//     const dbResourceSelect = document.getElementById("dbResourceSelect");
//     const btnLoadData = document.getElementById("btnLoadData");
//     const btnNewRow = document.getElementById("btnNewRow");
//     const dbTableHead = document.getElementById("dbTableHead");
//     const dbTableBody = document.getElementById("dbTableBody");
//     const dbSearch = document.getElementById("dbSearch");
//     const dbPageSize = document.getElementById("dbPageSize");
//     const dbPagingInfo = document.getElementById("dbPagingInfo");
//     const dbPrevPage = document.getElementById("dbPrevPage");
//     const dbNextPage = document.getElementById("dbNextPage");
//     const dbModalOverlay = document.getElementById("dbModalOverlay");
//     const dbModalTitle = document.getElementById("dbModalTitle");
//     const dbModalForm = document.getElementById("dbModalForm");
//     const dbModalCancel = document.getElementById("dbModalCancel");
//     const dbModalSave = document.getElementById("dbModalSave");

//     // const DB_ENDPOINTS = {
//     //     users: "/api/users",
//     //     tickets: "/api/admin/tickets",
//     //     ticket_gains: "/api/admin/ticket-gains"
//     // };
//     // ➜ endpoints réellement utilisés par le mini-HeidiSQL
//     const DB_ENDPOINTS = {
//         // on utilise les routes normales /api/users/** (JWT + rôle ADMIN déjà OK)
//         users: "/api/users",

//         // pour tickets & ticket_gains, on reste sur l'admin CRUD
//         tickets: "/api/admin/tickets",
//         ticket_gains: "/api/admin/ticket-gains"
//         // refresh_tokens: "/api/admin/refresh-tokens"
//     };


//     let dbCurrentResource = "users";
//     let dbRawData = [];
//     let dbFilteredData = [];
//     let dbCurrentPage = 0;
//     let dbEditingRow = null; // {resource, id, mode:'create'|'edit'}

//     function applyDbFilter() {
//         const q = dbSearch.value.toLowerCase().trim();
//         if (!q) {
//             dbFilteredData = dbRawData.slice();
//         } else {
//             dbFilteredData = dbRawData.filter(row =>
//                 JSON.stringify(row).toLowerCase().includes(q)
//             );
//         }
//         dbCurrentPage = 0;
//         renderDbTable();
//     }

//     function renderDbTable() {
//         dbTableHead.innerHTML = "";
//         dbTableBody.innerHTML = "";

//         if (!dbFilteredData || dbFilteredData.length === 0) {
//             dbTableBody.innerHTML =
//                 "<tr><td style='padding:8px;color:#9ca3af;'>Aucune donnée.</td></tr>";
//             dbPagingInfo.textContent = "Page 1 / 1";
//             return;
//         }

//         const pageSize = parseInt(dbPageSize.value, 10) || 20;
//         const totalPages = Math.max(1, Math.ceil(dbFilteredData.length / pageSize));
//         if (dbCurrentPage >= totalPages) dbCurrentPage = totalPages - 1;

//         const start = dbCurrentPage * pageSize;
//         const end = Math.min(start + pageSize, dbFilteredData.length);
//         const pageData = dbFilteredData.slice(start, end);

//         const keys = Object.keys(pageData[0]);

//         // head
//         const headerRow = document.createElement("tr");
//         keys.forEach(k => {
//             const th = document.createElement("th");
//             th.textContent = k;
//             th.style.padding = "6px 8px";
//             th.style.borderBottom = "1px solid #111827";
//             th.style.textAlign = "left";
//             headerRow.appendChild(th);
//         });
//         const thActions = document.createElement("th");
//         thActions.textContent = "Actions";
//         thActions.style.padding = "6px 8px";
//         thActions.style.borderBottom = "1px solid #111827";
//         headerRow.appendChild(thActions);
//         dbTableHead.appendChild(headerRow);

//         // body
//         pageData.forEach(row => {
//             const tr = document.createElement("tr");
//             tr.style.borderBottom = "1px solid #020617";

//             keys.forEach(k => {
//                 const td = document.createElement("td");
//                 let value = row[k];
//                 if (value === null || value === undefined) value = "";
//                 if (typeof value === "object") value = JSON.stringify(value);
//                 td.textContent = String(value);
//                 td.style.padding = "6px 8px";
//                 td.style.verticalAlign = "top";
//                 td.style.maxWidth = "260px";
//                 td.style.wordBreak = "break-word";
//                 tr.appendChild(td);
//             });

//             const tdActions = document.createElement("td");
//             tdActions.style.padding = "6px 8px";
//             tdActions.style.whiteSpace = "nowrap";

//             const id = row.id || row._id;

//             const btnEdit = document.createElement("button");
//             btnEdit.textContent = "Modifier";
//             btnEdit.style.padding = "3px 8px";
//             btnEdit.style.borderRadius = "999px";
//             btnEdit.style.border = "none";
//             btnEdit.style.fontSize = "0.75rem";
//             btnEdit.style.background = "#3b82f6";
//             btnEdit.style.color = "#020617";
//             btnEdit.style.cursor = "pointer";
//             btnEdit.style.marginRight = "4px";
//             btnEdit.addEventListener("click", () => openDbModal("edit", row));

//             const btnDelete = document.createElement("button");
//             btnDelete.textContent = "Supprimer";
//             btnDelete.style.padding = "3px 8px";
//             btnDelete.style.borderRadius = "999px";
//             btnDelete.style.border = "none";
//             btnDelete.style.fontSize = "0.75rem";
//             btnDelete.style.background = "#ef4444";
//             btnDelete.style.color = "#020617";
//             btnDelete.style.cursor = "pointer";

//             btnDelete.addEventListener("click", async () => {
//                 if (!id) { alert("ID introuvable."); return; }
//                 if (!confirm("Supprimer " + dbCurrentResource + " #" + id + " ?")) return;
//                 const url = DB_ENDPOINTS[dbCurrentResource] + "/" + id;
//                 try {
//                     const delRes = await fetch(url, {
//                         method: "DELETE",
//                         headers: {
//                             ...getAuthHeaders()
//                         },
//                         credentials: "include"
//                     });
//                     if (!delRes.ok) {
//                         alert("Erreur " + delRes.status + " lors de la suppression.");
//                         return;
//                     }
//                     dbRawData = dbRawData.filter(r => (r.id || r._id) !== id);
//                     applyDbFilter();
//                 } catch (e) {
//                     console.error(e);
//                     alert("Erreur réseau lors de la suppression.");
//                 }
//             });

//             tdActions.appendChild(btnEdit);
//             tdActions.appendChild(btnDelete);
//             tr.appendChild(tdActions);
//             dbTableBody.appendChild(tr);
//         });

//         dbPagingInfo.textContent = "Page " + (dbCurrentPage + 1) + " / " + totalPages;
//     }

//     async function loadDbData() {
//         dbCurrentResource = dbResourceSelect.value;
//         const url = DB_ENDPOINTS[dbCurrentResource];

//         dbTableHead.innerHTML = "";
//         dbTableBody.innerHTML =
//             "<tr><td style='padding:8px;color:#9ca3af;'>Chargement...</td></tr>";

//         if (!url) {
//             dbTableBody.innerHTML =
//                 "<tr><td style='padding:8px;color:#f97373;'>Endpoint non configuré.</td></tr>";
//             return;
//         }

//         try {
//             const res = await fetch(url, {
//                 method: "GET",
//                 headers: {
//                     ...getAuthHeaders()
//                 },
//                 credentials: "include"
//             });
//             if (!res.ok) {
//                 dbTableBody.innerHTML =
//                     "<tr><td style='padding:8px;color:#f97373;'>Erreur " + res.status + ".</td></tr>";
//                 return;
//             }
//             const data = await res.json();
//             if (!Array.isArray(data) || data.length === 0) {
//                 dbRawData = [];
//                 applyDbFilter();
//                 return;
//             }
//             dbRawData = data;
//             applyDbFilter();
//         } catch (e) {
//             console.error(e);
//             dbTableBody.innerHTML =
//                 "<tr><td style='padding:8px;color:#f97373;'>Erreur réseau.</td></tr>";
//         }
//     }

//     btnLoadData.addEventListener("click", loadDbData);
//     dbSearch.addEventListener("input", () => applyDbFilter());
//     dbPageSize.addEventListener("change", () => { dbCurrentPage = 0; renderDbTable(); });
//     dbPrevPage.addEventListener("click", () => {
//         if (dbCurrentPage > 0) { dbCurrentPage--; renderDbTable(); }
//     });
//     dbNextPage.addEventListener("click", () => {
//         const pageSize = parseInt(dbPageSize.value, 10) || 20;
//         const totalPages = Math.max(1, Math.ceil(dbFilteredData.length / pageSize));
//         if (dbCurrentPage < totalPages - 1) { dbCurrentPage++; renderDbTable(); }
//     });

//     function openDbModal(mode, row) {
//         dbEditingRow = { resource: dbCurrentResource, mode, row: row || null };
//         dbModalForm.innerHTML = "";

//         const baseRow = row || (dbRawData[0] || {});
//         const keys = Object.keys(baseRow);

//         const protectedFields = ["id", "_id", "created_at", "updated_at", "createdAt", "updatedAt"];

//         keys.forEach(k => {
//             const wrapper = document.createElement("div");
//             wrapper.style.display = "flex";
//             wrapper.style.flexDirection = "column";
//             wrapper.style.gap = "2px";

//             const label = document.createElement("label");
//             label.textContent = k;
//             label.style.fontSize = "0.8rem";
//             label.style.color = "#9ca3af";

//             const input = document.createElement("input");
//             input.name = k;
//             input.value = row ? (row[k] ?? "") : "";
//             input.style.background = "#020617";
//             input.style.color = "#e5e7eb";
//             input.style.borderRadius = "8px";
//             input.style.border = "1px solid #4b5563";
//             input.style.padding = "6px 8px";
//             input.style.fontSize = "0.8rem";

//             if (protectedFields.includes(k)) {
//                 input.disabled = true;
//                 input.style.opacity = "0.6";
//             }

//             wrapper.appendChild(label);
//             wrapper.appendChild(input);
//             dbModalForm.appendChild(wrapper);
//         });

//         dbModalTitle.textContent =
//             mode === "edit" ? "Modifier la ligne #" + (row.id || row._id) : "Nouvelle ligne";
//         dbModalOverlay.style.display = "flex";
//     }

//     function closeDbModal() {
//         dbModalOverlay.style.display = "none";
//         dbEditingRow = null;
//     }

//     dbModalCancel.addEventListener("click", closeDbModal);

//     dbModalSave.addEventListener("click", async () => {
//         if (!dbEditingRow) return;
//         const formData = new FormData(dbModalForm);
//         const obj = {};
//         formData.forEach((v, k) => {
//             const input = dbModalForm.querySelector('input[name="' + k + '"]');
//             if (input && input.disabled) return;
//             obj[k] = v === "" ? null : v;
//         });

//         const resource = dbEditingRow.resource;
//         const baseUrl = DB_ENDPOINTS[resource];
//         let method = "POST";
//         let url = baseUrl;

//         if (dbEditingRow.mode === "edit") {
//             const id = dbEditingRow.row.id || dbEditingRow.row._id;
//             method = "PUT";
//             url = baseUrl + "/" + id;
//         }

//         try {
//             const res = await fetch(url, {
//                 method,
//                 headers: {
//                     "Content-Type": "application/json",
//                     ...getAuthHeaders()
//                 },
//                 credentials: "include",
//                 body: JSON.stringify(obj)
//             });
//             if (!res.ok) {
//                 alert("Erreur " + res.status + " lors de l’enregistrement.");
//                 return;
//             }
//             closeDbModal();
//             await loadDbData();
//         } catch (e) {
//             console.error(e);
//             alert("Erreur réseau lors de l’enregistrement.");
//         }
//     });

//     btnNewRow.addEventListener("click", () => openDbModal("create", null));

//     // ---------- STATS JOUEURS / TICKETS ----------
//     const btnLoadStats = document.getElementById("btnLoadStats");
//     const statsGrid = document.getElementById("statsGrid");
//     const statsSort = document.getElementById("statsSort");
//     const statsSearch = document.getElementById("statsSearch");

//     const STATS_ENDPOINT = "/api/admin/users-stats";

//     let statsRaw = [];
//     let statsFiltered = [];

//     function applyStatsFilter() {
//         const q = statsSearch.value.toLowerCase().trim();
//         if (!q) statsFiltered = statsRaw.slice();
//         else {
//             statsFiltered = statsRaw.filter(u =>
//                 (u.email || "").toLowerCase().includes(q) ||
//                 (u.firstName || "").toLowerCase().includes(q) ||
//                 (u.lastName || "").toLowerCase().includes(q)
//             );
//         }

//         const sortKey = statsSort.value;
//         statsFiltered.sort((a, b) => {
//             const tA = a.ticketsCount || 0;
//             const tB = b.ticketsCount || 0;
//             const gA = a.totalGain || 0;
//             const gB = b.totalGain || 0;
//             const dA = a.lastTicketDate ? new Date(a.lastTicketDate).getTime() : 0;
//             const dB = b.lastTicketDate ? new Date(b.lastTicketDate).getTime() : 0;

//             if (sortKey === "ticketsDesc") return tB - tA;
//             if (sortKey === "gainDesc") return gB - gA;
//             if (sortKey === "lastTicketDesc") return dB - dA;
//             return 0;
//         });

//         renderStats();
//     }

//     function renderStats() {
//         statsGrid.innerHTML = "";
//         if (!statsFiltered || statsFiltered.length === 0) {
//             statsGrid.innerHTML =
//                 "<div style='color:#9ca3af;font-size:0.85rem;'>Aucune statistique disponible.</div>";
//             return;
//         }

//         statsFiltered.forEach(u => {
//             const card = document.createElement("article");
//             card.style.borderRadius = "14px";
//             card.style.border = "1px solid #111827";
//             card.style.background = "linear-gradient(135deg,#020617,#020617,#020617)";
//             card.style.padding = "10px 12px";
//             card.style.display = "flex";
//             card.style.flexDirection = "column";
//             card.style.gap = "6px";

//             const header = document.createElement("div");
//             header.style.display = "flex";
//             header.style.justifyContent = "space-between";
//             header.style.alignItems = "center";

//             const title = document.createElement("div");
//             title.innerHTML =
//                 "<div style='font-size:0.9rem;font-weight:600;'>" +
//                 (u.firstName || "") + " " + (u.lastName || "") + "</div>" +
//                 "<div style='font-size:0.78rem;color:#9ca3af;'>" + (u.email || "") + "</div>";

//             const badge = document.createElement("span");
//             badge.textContent = u.admin ? "ADMIN" : "USER";
//             badge.style.fontSize = "0.7rem";
//             badge.style.padding = "2px 8px";
//             badge.style.borderRadius = "999px";
//             badge.style.border = "1px solid " + (u.admin ? "#22c55e" : "#4b5563");
//             badge.style.color = u.admin ? "#22c55e" : "#9ca3af";

//             header.appendChild(title);
//             header.appendChild(badge);

//             const statsRow = document.createElement("div");
//             statsRow.style.display = "grid";
//             statsRow.style.gridTemplateColumns = "repeat(3,minmax(0,1fr))";
//             statsRow.style.gap = "6px";
//             statsRow.style.marginTop = "4px";

//             const blocks = [
//                 { label: "Tickets", value: u.ticketsCount || 0 },
//                 { label: "Gain total", value: (u.totalGain || 0) + " €" },
//                 { label: "Meilleur gain", value: (u.bestGain || 0) + " €" }
//             ];
//             blocks.forEach(b => {
//                 const div = document.createElement("div");
//                 div.style.fontSize = "0.75rem";
//                 div.style.color = "#9ca3af";
//                 div.innerHTML =
//                     "<div>" + b.label + "</div>" +
//                     "<div style='color:#e5e7eb;font-weight:600;font-size:0.86rem;'>" + b.value + "</div>";
//                 statsRow.appendChild(div);
//             });

//             const footer = document.createElement("div");
//             footer.style.marginTop = "4px";
//             footer.style.fontSize = "0.75rem";
//             footer.style.color = "#6b7280";
//             footer.textContent =
//                 "Dernier ticket : " + (u.lastTicketDate || "—") +
//                 " | ID utilisateur : " + (u.id || u._id || "—");

//             card.appendChild(header);
//             card.appendChild(statsRow);
//             card.appendChild(footer);
//             statsGrid.appendChild(card);
//         });
//     }

//     async function loadStats() {
//         statsGrid.innerHTML =
//             "<div style='color:#9ca3af;font-size:0.85rem;'>Chargement des statistiques...</div>";
//         try {
//             const res = await fetch(STATS_ENDPOINT, {
//                 method: "GET",
//                 headers: {
//                     ...getAuthHeaders()
//                 },
//                 credentials: "include"
//             });
//             if (!res.ok) {
//                 statsGrid.innerHTML =
//                     "<div style='color:#f97373;font-size:0.85rem;'>Erreur " + res.status + ".</div>";
//                 return;
//             }
//             const data = await res.json();
//             if (!Array.isArray(data)) {
//                 statsGrid.innerHTML =
//                     "<div style='color:#f97373;font-size:0.85rem;'>Réponse invalide du serveur.</div>";
//                 return;
//             }
//             statsRaw = data;
//             applyStatsFilter();
//         } catch (e) {
//             console.error(e);
//             statsGrid.innerHTML =
//                 "<div style='color:#f97373;font-size:0.85rem;'>Erreur réseau.</div>";
//         }
//     }

//     btnLoadStats.addEventListener("click", loadStats);
//     statsSort.addEventListener("change", applyStatsFilter);
//     statsSearch.addEventListener("input", applyStatsFilter);

//     // ---------- LOGOUT ----------
//     document.getElementById("btnLogout").addEventListener("click", async () => {
//         try {
//             await fetch("/api/auth/logout", { method: "POST", credentials: "include" });
//         } catch (e) {
//             console.error(e);
//         }
//         window.location.href = "/admin-login.html";
//     });

//     // ---------- INIT ----------
//     showSection("swagger");
//     refreshLogs();      // premier chargement
//     loadDbData();       // charge users par défaut
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
