// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth-guard';
import { CreateUser } from './modify-user/modify-user';
import { ModifyClient } from './modify-client/modify-client';
import { ModifyCoach } from './modify-coach/modify-coach';
import { Startpage } from './start-page/start-page';
import { ClientHome } from './client-home/client-home'

export const routes: Routes = [
{ path: 'login', component: LoginComponent },
{ path: 'create/user/:type', component: CreateUser },
{ path: 'create/client', component: ModifyClient },
{ path: 'create/coach', component: ModifyCoach },
{ path: 'client', component: ClientHome },
{ path: '', component: HomeComponent, canActivate: [authGuard] },
{ path: 'start', component: Startpage },
{ path: '**', redirectTo: '' }
];
