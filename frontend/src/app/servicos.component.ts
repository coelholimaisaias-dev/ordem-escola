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

import { ServicosService, Servico } from './servicos.service';
import { EmpresasService, Empresa } from './empresas.service';
import { Turno } from './turmas.service';

@Component({
  selector: 'app-servicos',
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
  templateUrl: './servicos.component.html',
  styleUrl: './servicos.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ServicosComponent {
  private servicosService = inject(ServicosService);
  private empresasService = inject(EmpresasService);
  private router = inject(Router);

  servicos = signal<Servico[]>([]);
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroEmpresa = signal<number | null>(null);
  filtroTurno = signal<Turno | null>(null);

  displayedColumns = ['id', 'nome', 'empresa', 'turno', 'valorBase', 'status', 'acoes'];

  servicosFiltrados = computed(() => {
    const items = this.servicos();
    const nome = this.filtroNome().toLowerCase();
    const empresa = this.filtroEmpresa();
    const turno = this.filtroTurno();

    return items.filter(s => {
      const matchNome = !nome || s.nome.toLowerCase().includes(nome);
      const matchEmpresa = !empresa || s.empresaId === empresa;
      const matchTurno = !turno || s.turno === turno;
      return matchNome && matchEmpresa && matchTurno;
    });
  });

  async ngOnInit() {
    await this.carregarEmpresas();
    await this.carregarServicos();
  }

  async carregarServicos() {
    this.isLoading.set(true);
    try {
      const lista = await this.servicosService.listAll(
        this.filtroNome() || undefined,
        this.filtroEmpresa() || undefined,
        this.filtroTurno() || undefined
      );
      this.servicos.set(lista);
    } catch (error) {
      console.error('Erro ao carregar serviços:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async carregarEmpresas() {
    try {
      const lista = await this.empresasService.listAll();
      this.empresas.set(lista);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  }

  filtrar() {
    // computed já aplica os filtros
  }

  limparFiltros() {
    this.filtroNome.set('');
    this.filtroEmpresa.set(null);
    this.filtroTurno.set(null);
  }

  novo() {
    this.router.navigate(['/servicos/nova']);
  }

  editar(id: number) {
    this.router.navigate(['/servicos', id]);
  }

  async alternarStatus(servico: Servico) {
    try {
      await this.servicosService.update(servico.id, {
        empresaId: servico.empresaId,
        nome: servico.nome,
        turno: servico.turno,
        valorBase: servico.valorBase,
        ativo: !servico.ativo
      });
      await this.carregarServicos();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }

  getTurnoLabel(turno: Turno): string {
    const labels: { [key in Turno]: string } = {
      MANHA: 'Manhã',
      TARDE: 'Tarde',
      NOITE: 'Noite'
    };
    return labels[turno];
  }
}
