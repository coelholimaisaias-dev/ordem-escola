import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { HomeComponent } from './home.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
	{ path: '', component: HomeComponent, canActivate: [authGuard] },
	{ path: 'login', component: LoginComponent },
	{ path: '**', redirectTo: '' }
];
