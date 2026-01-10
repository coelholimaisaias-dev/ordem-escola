import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { TipoProduto } from './core/tipo-produto';

export interface Produto {
  id: number;
  empresaId: number;
  empresaNome: string;
  nome: string;
  descricao?: string;
  tipo: TipoProduto;
  valorUnitario: number;
  estoque?: number;
  ativo: boolean;
}

export interface ProdutoCreateRequest {
  empresaId: number;
  nome: string;
  descricao?: string;
  tipo: TipoProduto;
  valorUnitario: number;
}

export interface ProdutoUpdateRequest {
  nome: string;
  descricao?: string;
  tipo: TipoProduto;
  valorUnitario: number;
  estoque?: number;
  ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProdutosService {
  private http = inject(HttpClient);
  private apiUrl = '/api/produtos';

  async listAll(nome?: string, tipo?: string, ativo?: boolean) {
    let url = this.apiUrl;
    const params = new URLSearchParams();
    if (nome) params.append('nome', nome);
    if (tipo) params.append('tipo', tipo);
    if (ativo !== undefined) params.append('ativo', ativo.toString());

    if (params.toString()) url += `?${params.toString()}`;

    return firstValueFrom(this.http.get<Produto[]>(url));
  }

  async getById(id: number) {
    return firstValueFrom(this.http.get<Produto>(`${this.apiUrl}/${id}`));
  }

  async create(payload: ProdutoCreateRequest) {
    return firstValueFrom(this.http.post<Produto>(this.apiUrl, payload));
  }

  async update(id: number, payload: ProdutoUpdateRequest) {
    return firstValueFrom(this.http.put<Produto>(`${this.apiUrl}/${id}`, payload));
  }

  async delete(id: number) {
    return firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
  }
}
