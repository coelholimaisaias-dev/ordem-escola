import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export enum Turno {
  MANHA = 'MANHA',
  TARDE = 'TARDE',
  NOITE = 'NOITE'
}

export interface Turma {
  id: number;
  empresaId: number;
  empresaNome: string;
  nome: string;
  turno: Turno;
  valorBase: number;
  capacidade: number;
  ativo: boolean;
}

export interface TurmaCreateRequest {
  empresaId: number;
  nome: string;
  turno: Turno;
  valorBase: number;
  capacidade: number;
}

export interface TurmaUpdateRequest {
  empresaId: number;
  nome: string;
  turno: Turno;
  valorBase: number;
  capacidade: number;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class TurmasService {
  private http = inject(HttpClient);
  private apiUrl = '/api/turmas';

  async listAll() {
    return firstValueFrom(this.http.get<Turma[]>(this.apiUrl));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Turma>(`${this.apiUrl}/${id}`));
  }

  async create(turma: TurmaCreateRequest) {
    return firstValueFrom(this.http.post<Turma>(this.apiUrl, turma));
  }

  async update(id: number, turma: TurmaUpdateRequest) {
    return firstValueFrom(this.http.put<Turma>(`${this.apiUrl}/${id}`, turma));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }
}
