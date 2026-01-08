import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

import { EmpresasService, Empresa } from './empresas.service';

@Component({
  selector: 'app-empresas',
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
    MatProgressSpinnerModule,
    MatTooltipModule
  ],
  templateUrl: './empresas.component.html',
  styleUrl: './empresas.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmpresasComponent {
  private empresasService = inject(EmpresasService);
  private router = inject(Router);

  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroCnpj = signal('');

  displayedColumns = ['id', 'nome', 'cnpj', 'status', 'acoes'];

  empresasFiltradas = computed(() => {
    const empresas = this.empresas();
    const nome = this.filtroNome().toLowerCase();
    const cnpj = this.filtroCnpj().replace(/\D/g, '');

    return empresas.filter(e => {
      const matchNome = !nome || e.nome.toLowerCase().includes(nome);
      const matchCnpj = !cnpj || e.cnpj.includes(cnpj);
      return matchNome && matchCnpj;
    });
  });

  async ngOnInit() {
    await this.carregarEmpresas();
  }

  async carregarEmpresas() {
    this.isLoading.set(true);
    try {
      const empresas = await this.empresasService.listAll();
      this.empresas.set(empresas);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  filtrar() {
    // Computed signal já faz o filtro automaticamente
  }

  limparFiltros() {
    this.filtroNome.set('');
    this.filtroCnpj.set('');
  }

  nova() {
    this.router.navigate(['/empresas/nova']);
  }

  editar(id: number) {
    this.router.navigate(['/empresas', id]);
  }

  async alternarStatus(empresa: Empresa) {
    try {
      await this.empresasService.update(empresa.id, {
        nome: empresa.nome,
        cnpj: empresa.cnpj,
        ativo: !empresa.ativo
      });
      await this.carregarEmpresas();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }

  formatarCnpj(cnpj: string): string {
    return cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5');
  }
}
