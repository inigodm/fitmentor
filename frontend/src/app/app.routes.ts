// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth-guard';

export const routes: Routes = [
{ path: 'login', component: LoginComponent },
{ path: '', component: HomeComponent, canActivate: [authGuard] }, // ruta protegida
{ path: '**', redirectTo: '' } // redirección para rutas desconocidas
];
