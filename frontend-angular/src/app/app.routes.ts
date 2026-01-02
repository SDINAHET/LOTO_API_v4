import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) },

  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent) },

  { path: 'profil', loadComponent: () => import('./pages/profil/profil.component').then(m => m.ProfilComponent) },

  { path: 'tickets', loadComponent: () => import('./pages/tickets/tickets.component').then(m => m.TicketsComponent) },
  { path: 'tickets/new', loadComponent: () => import('./pages/ticket-create/ticket-create.component').then(m => m.TicketCreateComponent) },
  { path: 'tickets/:id/edit', loadComponent: () => import('./pages/ticket-edit/ticket-edit.component').then(m => m.TicketEditComponent) },

  { path: 'statistiques', loadComponent: () => import('./pages/statistiques/statistiques.component').then(m => m.StatistiquesComponent) },
  { path: 'euromillions', loadComponent: () => import('./pages/euromillions/euromillions.component').then(m => m.EuromillionsComponent) },

  { path: 'admin', loadComponent: () => import('./pages/admin-login/admin-login.component').then(m => m.AdminLoginComponent) },
  { path: 'ai', loadComponent: () => import('./pages/ai/ai.component').then(m => m.AiComponent) },

  { path: 'mentions-legales', loadComponent: () => import('./pages/mentions-legales/mentions-legales.component').then(m => m.MentionsLegalesComponent) },
  { path: 'politique-confidentialite', loadComponent: () => import('./pages/politique-confidentialite/politique-confidentialite.component').then(m => m.PolitiqueConfidentialiteComponent) },
  { path: 'conditions-utilisation', loadComponent: () => import('./pages/conditions-utilisation/conditions-utilisation.component').then(m => m.ConditionsUtilisationComponent) },

  { path: 'loading', loadComponent: () => import('./pages/loading/loading.component').then(m => m.LoadingComponent) },

  { path: '**', loadComponent: () => import('./pages/error-404/error-404.component').then(m => m.Error404Component) },
];
