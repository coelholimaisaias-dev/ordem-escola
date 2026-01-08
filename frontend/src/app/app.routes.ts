import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { HomeComponent } from './home.component';
import { authGuard } from './auth.guard';
import { roleGuard } from './role.guard';
import { UsuariosComponent } from './usuarios.component';
import { UsuariosFormComponent } from './usuarios-form.component';
import { EmpresasComponent } from './empresas.component';
import { EmpresasFormComponent } from './empresas-form.component';
import { TurmasComponent } from './turmas.component';
import { TurmasFormComponent } from './turmas-form.component';
import { AlunosComponent } from './alunos.component';
import { AlunosFormComponent } from './alunos-form.component';
import { ServicosComponent } from './servicos.component';
import { ServicosFormComponent } from './servicos-form.component';
import { Perfil } from './core/perfil';

export const routes: Routes = [
	{ path: '', component: HomeComponent, canActivate: [authGuard] },
	{ path: 'login', component: LoginComponent },
	{ path: 'usuarios', component: UsuariosComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'usuarios/novo', component: UsuariosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'usuarios/:id', component: UsuariosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'empresas', component: EmpresasComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'empresas/nova', component: EmpresasFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'empresas/:id', component: EmpresasFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN] } },
	{ path: 'turmas', component: TurmasComponent, canActivate: [authGuard] },
	{ path: 'turmas/nova', component: TurmasFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: 'turmas/:id', component: TurmasFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: 'alunos', component: AlunosComponent, canActivate: [authGuard] },
	{ path: 'alunos/novo', component: AlunosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: 'alunos/:id', component: AlunosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: 'servicos', component: ServicosComponent, canActivate: [authGuard] },
	{ path: 'servicos/nova', component: ServicosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: 'servicos/:id', component: ServicosFormComponent, canActivate: [authGuard, roleGuard], data: { roles: [Perfil.ADMIN, Perfil.CLIENTE] } },
	{ path: '**', redirectTo: '' }
];
