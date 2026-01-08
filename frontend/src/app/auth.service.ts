import { Injectable, signal, inject } from '@angular/core';
import { computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Role } from './core/roles';
import { Perfil } from './core/perfil';
import { firstValueFrom } from 'rxjs';

interface LoginResponse {
  token: string;
  username?: string;
  name?: string;
  perfil?: Perfil;
  empresaId?: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private token = signal<string | null>(localStorage.getItem('auth_token'));
  private user = signal<string | null>(localStorage.getItem('user'));
  private role = signal<Role>((localStorage.getItem('user_role') as Role) || Perfil.CLIENTE);
  private empresaId = signal<number | null>(parseInt(localStorage.getItem('user_empresa_id') || 'null', 10) || null);
  readonly isAuthenticated = computed(() => !!this.token());

  async login(email: string, senha: string) {
    const body = { email, senha };
    try {
      const res = await firstValueFrom(this.http.post<LoginResponse>(`/api/auth/login`, body));
      console.log('Resposta do login:', res);
      if (res?.token) {
        this.token.set(res.token);
        localStorage.setItem('auth_token', res.token);
      } else {
        console.warn('Token não encontrado na resposta. Backend enviando cookie?');
        // Se não tem token no body, assumir que está autenticado (cookie)
        this.token.set('authenticated');
        localStorage.setItem('auth_token', 'authenticated');
      }
      const uname = res.name ?? res.username ?? email;
      this.user.set(uname);
      localStorage.setItem('user', uname);

      // Capturar role do backend
      const role = res.perfil === Perfil.ADMIN ? Perfil.ADMIN : Perfil.CLIENTE;
      this.setRole(role);

      // Capturar empresaId do backend
      if (res.empresaId) {
        this.setEmpresaId(res.empresaId);
      }

      console.log('Role setado:', role, 'EmpresaId:', res.empresaId);

      return { ok: true, message: 'Autenticado' } as const;
    } catch (err: any) {
      this.logout();
      console.error('Erro no login:', err);
      const message = err?.error?.message || err?.message || 'Erro ao autenticar';
      return { ok: false, message } as const;
    }
  }

  logout() {
    this.token.set(null);
    this.user.set(null);
    this.empresaId.set(null);
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
    localStorage.removeItem('user_role');
    localStorage.removeItem('user_empresa_id');
  }

  async performLogout() {
    try {
      await firstValueFrom(this.http.post(`/api/auth/logout`, {}));
    } catch {
      // ignore
    } finally {
      this.logout();
    }
  }

  getToken() {
    return this.token();
  }

  getUser() {
    return this.user();
  }

  getRole(): Role {
    return this.role();
  }

  setRole(role: Role) {
    this.role.set(role);
    localStorage.setItem('user_role', role);
  }

  getEmpresaId(): number | null {
    return this.empresaId();
  }

  setEmpresaId(id: number) {
    this.empresaId.set(id);
    localStorage.setItem('user_empresa_id', id.toString());
  }

  isAdmin() {
    return this.role() === Perfil.ADMIN;
  }

  isCliente() {
    return this.role() === Perfil.CLIENTE;
  }
}


