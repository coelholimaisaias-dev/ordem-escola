import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface Empresa {
  id: number;
  nome: string;
  cnpj: string;
  ativo: boolean;
}

export interface EmpresaCreateRequest {
  nome: string;
  cnpj: string;
}

export interface EmpresaUpdateRequest {
  nome: string;
  cnpj: string;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class EmpresasService {
  private http = inject(HttpClient);
  private apiUrl = '/api/empresas';

  async listAll() {
    return firstValueFrom(this.http.get<Empresa[]>(this.apiUrl));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Empresa>(`${this.apiUrl}/${id}`));
  }

  async create(empresa: EmpresaCreateRequest) {
    return firstValueFrom(this.http.post<Empresa>(this.apiUrl, empresa));
  }

  async update(id: number, empresa: EmpresaUpdateRequest) {
    return firstValueFrom(this.http.put<Empresa>(`${this.apiUrl}/${id}`, empresa));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }
}
