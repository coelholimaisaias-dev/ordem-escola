import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { HomeComponent } from './home.component';
import { authGuard } from './auth.guard';
import { roleGuard } from './role.guard';
import { UsuariosComponent } from './usuarios.component';
import { UsuariosFormComponent } from './usuarios-form.component';
import { EmpresasComponent } from './empresas.component';
import { TurmasComponent } from './turmas.component';
import { AlunosComponent } from './alunos.component';
import { Perfil } from './core/perfil';

export const routes: Routes = [
	{ path: '', component: HomeComponent, canActivate: [authGuard] },
	{ path: 'login', component: LoginComponent },
	{ path: 'usuarios', component: UsuariosComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'usuarios/novo', component: UsuariosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'usuarios/:id', component: UsuariosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'empresas', component: EmpresasComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'turmas', component: TurmasComponent, canActivate: [authGuard] },
	{ path: 'alunos', component: AlunosComponent, canActivate: [authGuard] },
	{ path: '**', redirectTo: '' }
];
