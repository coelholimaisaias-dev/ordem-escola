import { Component, ChangeDetectionStrategy, inject, signal, computed, OnInit } from '@angular/core';
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

import { TurmasService, Turma, Turno } from './turmas.service';
import { EmpresasService, Empresa } from './empresas.service';
import { AuthService } from './auth.service';
import { Perfil } from './core/perfil';
import { TURNOS, getTurnoLabel } from './core/turnos';

@Component({
  selector: 'app-turmas',
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
  templateUrl: './turmas.component.html',
  styleUrl: './turmas.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TurmasComponent implements OnInit {
  private turmasService = inject(TurmasService);
  private empresasService = inject(EmpresasService);
  private router = inject(Router);
  private authService = inject(AuthService);

  turmas = signal<Turma[]>([]);
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroTurno = signal<Turno | null>(null);
  filtroEmpresa = signal<number | null>(null);

  displayedColumns = ['id', 'nome', 'turno', 'empresa', 'capacidade', 'valorBase', 'status', 'acoes'];

  isAdmin = this.authService.isAdmin();
  empresaIdUsuario = this.authService.getEmpresaId();
  readonly turnos = TURNOS;
  readonly getTurnoLabel = getTurnoLabel;

  turmasFiltradas = computed(() => {
    const turmas = this.turmas();
    const nome = this.filtroNome().toLowerCase();
    const turno = this.filtroTurno();
    const empresa = this.filtroEmpresa();
    const isAdmin = this.authService.isAdmin();
    const empresaId = this.authService.getEmpresaId();

    return turmas.filter(t => {
      const matchNome = !nome || t.nome.toLowerCase().includes(nome);
      const matchTurno = !turno || t.turno === turno;
      const matchEmpresa = !empresa || t.empresaId === empresa;
      // CLIENTE vê só turmas da sua empresa
      const matchPermissao = isAdmin || t.empresaId === empresaId;
      return matchNome && matchTurno && matchEmpresa && matchPermissao;
    });
  });

  async ngOnInit() {
    await this.carregarEmpresas();
    await this.carregarTurmas();
  }

  async carregarTurmas() {
    this.isLoading.set(true);
    try {
      const turmas = await this.turmasService.listAll();
      this.turmas.set(turmas);
    } catch (error) {
      console.error('Erro ao carregar turmas:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async carregarEmpresas() {
    try {
      const empresas = await this.empresasService.listAll();
      this.empresas.set(empresas);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  }

  filtrar() {
    // Computed signal já faz o filtro automaticamente
  }

  limparFiltros() {
    this.filtroNome.set('');
    this.filtroTurno.set(null);
    this.filtroEmpresa.set(null);
  }

  nova() {
    this.router.navigate(['/turmas/nova']);
  }

  editar(id: number) {
    this.router.navigate(['/turmas', id]);
  }

  async alternarStatus(turma: Turma) {
    try {
      await this.turmasService.update(turma.id, {
        empresaId: turma.empresaId,
        nome: turma.nome,
        turno: turma.turno,
        valorBase: turma.valorBase,
        capacidade: turma.capacidade,
        ativo: !turma.ativo
      });
      await this.carregarTurmas();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }

}
