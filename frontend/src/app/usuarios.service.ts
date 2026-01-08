import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Perfil } from './core/perfil';

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  empresaId: number;
  empresaNome: string;
  perfil: Perfil;
  ativo: boolean;
}

export interface UsuarioCreateRequest {
  nome: string;
  email: string;
  senha: string;
  empresaId: number;
  perfil: Perfil;
}

export interface UsuarioUpdateRequest {
  nome: string;
  email: string;
  empresaId: number;
  perfil: Perfil;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  private http = inject(HttpClient);
  private apiUrl = '/usuarios';

  async listAll(nome?: string, empresaId?: number) {
    let url = this.apiUrl;
    const params = new URLSearchParams();
    if (nome) params.append('nome', nome);
    if (empresaId) params.append('empresaId', empresaId.toString());

    if (params.toString()) {
      url += '?' + params.toString();
    }

    return firstValueFrom(this.http.get<Usuario[]>(url));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Usuario>(`${this.apiUrl}/${id}`));
  }

  async create(usuario: UsuarioCreateRequest) {
    return firstValueFrom(this.http.post<Usuario>(this.apiUrl, usuario));
  }

  async update(id: number, usuario: UsuarioUpdateRequest) {
    return firstValueFrom(this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }

  async toggleStatus(id: number, ativo: boolean) {
    return firstValueFrom(
      this.http.patch<Usuario>(`${this.apiUrl}/${id}/status`, { ativo })
    );
  }
}
