import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ProdutosService, Produto } from './produtos.service';
import { TipoProduto, TipoProdutoLabels } from './core/tipo-produto';

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],
  templateUrl: './produtos.component.html',
  styleUrl: './produtos.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProdutosComponent {
  private produtosService = inject(ProdutosService);
  private router = inject(Router);

  produtos = signal<Produto[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroTipo = signal<string | null>(null);

  readonly tiposProduto = Object.entries(TipoProdutoLabels).map(([value, label]) => ({
    value,
    label
  }));

  getTipoProdutoLabel = (tipo: TipoProduto) => TipoProdutoLabels[tipo];

  displayedColumns = ['id', 'nome', 'tipo', 'empresa', 'valorUnitario', 'status', 'acoes'];

  produtosFiltrados = computed(() => {
    const items = this.produtos();
    const nome = this.filtroNome().toLowerCase();
    const tipo = this.filtroTipo();

    return items.filter(p => {
      const matchNome = !nome || p.nome.toLowerCase().includes(nome);
      const matchTipo = !tipo || p.tipo === tipo;
      return matchNome && matchTipo;
    });
  });

  async ngOnInit() {
    await this.carregarProdutos();
  }

  async carregarProdutos() {
    this.isLoading.set(true);
    try {
      const lista = await this.produtosService.listAll(
        this.filtroNome() || undefined,
        this.filtroTipo() || undefined
      );
      this.produtos.set(lista);
    } catch (error) {
      console.error('Erro ao carregar produtos:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  filtrar() {
    // computed já aplica os filtros
  }

  limparFiltros() {
    this.filtroNome.set('');
    this.filtroTipo.set(null);
  }

  novo() {
    this.router.navigate(['/produtos/novo']);
  }

  editar(id: number) {
    this.router.navigate(['/produtos', id]);
  }

  async alternarStatus(produto: Produto) {
    try {
      await this.produtosService.update(produto.id, {
        nome: produto.nome,
        descricao: produto.descricao,
        tipo: produto.tipo,
        valorUnitario: produto.valorUnitario,
        ativo: !produto.ativo
      });
      await this.carregarProdutos();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }
}
