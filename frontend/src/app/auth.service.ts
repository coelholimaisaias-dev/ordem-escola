import { Injectable, signal, inject } from '@angular/core';
import { computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

interface LoginResponse {
  token: string;
  username?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private token = signal<string | null>(localStorage.getItem('auth_token'));
  private user = signal<string | null>(localStorage.getItem('user'));
  readonly isAuthenticated = computed(() => !!this.token());

  async login(email: string, senha: string) {
    const body = { email, senha };
    try {
      const res = await firstValueFrom(this.http.post<LoginResponse>(`/auth/login`, body));
      if (res?.token) {
        this.token.set(res.token);
        localStorage.setItem('auth_token', res.token);
      }
      const uname = res.username ?? email;
      this.user.set(uname);
      localStorage.setItem('user', uname);
      return { ok: true, message: 'Autenticado' } as const;
    } catch (err: any) {
      this.logout();
      const message = err?.error?.message || err?.message || 'Erro ao autenticar';
      return { ok: false, message } as const;
    }
  }

  logout() {
    this.token.set(null);
    this.user.set(null);
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
  }

  async performLogout() {
    try {
      await firstValueFrom(this.http.post(`/auth/logout`, {}));
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
}

