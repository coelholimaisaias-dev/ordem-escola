import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Turno } from './turmas.service';

export interface Servico {
  id: number;
  empresaId: number;
  empresaNome: string;
  nome: string;
  turno: Turno;
  valorBase: number;
  ativo: boolean;
}

export interface ServicoCreateRequest {
  empresaId: number;
  nome: string;
  turno: Turno;
  valorBase: number;
}

export interface ServicoUpdateRequest {
  empresaId: number;
  nome: string;
  turno: Turno;
  valorBase: number;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ServicosService {
  private http = inject(HttpClient);
  private apiUrl = '/api/servicos';

  async listAll(nome?: string, empresaId?: number, turno?: Turno) {
    let url = this.apiUrl;
    const params = new URLSearchParams();
    if (nome) params.append('nome', nome);
    if (empresaId) params.append('empresaId', empresaId.toString());
    if (turno) params.append('turno', turno);

    if (params.toString()) url += `?${params.toString()}`;

    return firstValueFrom(this.http.get<Servico[]>(url));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Servico>(`${this.apiUrl}/${id}`));
  }

  async create(payload: ServicoCreateRequest) {
    return firstValueFrom(this.http.post<Servico>(this.apiUrl, payload));
  }

  async update(id: number, payload: ServicoUpdateRequest) {
    return firstValueFrom(this.http.put<Servico>(`${this.apiUrl}/${id}`, payload));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }
}
