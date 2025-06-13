// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth-guard';
import { CreateUser } from './modify-user/modify-user';
import { Startpage } from './start-page/start-page';

export const routes: Routes = [
{ path: 'login', component: LoginComponent },
{ path: 'user/create/:type', component: CreateUser },
{ path: '', component: HomeComponent, canActivate: [authGuard] }, // ruta protegida
{ path: 'start', component: Startpage }, // ruta protegida
{ path: '**', redirectTo: '' } // redirección para rutas desconocidas
];
