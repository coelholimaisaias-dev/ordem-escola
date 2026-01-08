import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface Aluno {
  id: number;
  usuarioId: number;
  usuarioNome: string;
  usuarioEmail: string;
  dataNascimento: string;
  ativo: boolean;
}

export interface AlunoCreateRequest {
  usuarioId: number;
  dataNascimento?: string;
}

export interface AlunoUpdateRequest {
  usuarioId: number;
  dataNascimento?: string;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class AlunosService {
  private http = inject(HttpClient);
  private apiUrl = '/api/alunos';

  async listAll() {
    return firstValueFrom(this.http.get<Aluno[]>(this.apiUrl));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Aluno>(`${this.apiUrl}/${id}`));
  }

  async create(aluno: AlunoCreateRequest) {
    return firstValueFrom(this.http.post<Aluno>(this.apiUrl, aluno));
  }

  async update(id: number, aluno: AlunoUpdateRequest) {
    return firstValueFrom(this.http.put<Aluno>(`${this.apiUrl}/${id}`, aluno));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }
}
