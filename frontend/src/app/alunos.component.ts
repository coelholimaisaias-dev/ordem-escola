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

import { AlunosService, Aluno } from './alunos.service';

@Component({
  selector: 'app-alunos',
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
  templateUrl: './alunos.component.html',
  styleUrl: './alunos.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AlunosComponent {
  private alunosService = inject(AlunosService);
  private router = inject(Router);

  alunos = signal<Aluno[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroEmail = signal('');

  displayedColumns = ['id', 'nome', 'email', 'dataNascimento', 'status', 'acoes'];

  alunosFiltrados = computed(() => {
    const alunos = this.alunos();
    const nome = this.filtroNome().toLowerCase();
    const email = this.filtroEmail().toLowerCase();

    return alunos.filter(a => {
      const matchNome = !nome || a.usuarioNome.toLowerCase().includes(nome);
      const matchEmail = !email || a.usuarioEmail.toLowerCase().includes(email);
      return matchNome && matchEmail;
    });
  });

  async ngOnInit() {
    await this.carregarAlunos();
  }

  async carregarAlunos() {
    this.isLoading.set(true);
    try {
      const alunos = await this.alunosService.listAll();
      this.alunos.set(alunos);
    } catch (error) {
      console.error('Erro ao carregar alunos:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  filtrar() {
    // Computed signal já faz o filtro automaticamente
  }

  limparFiltros() {
    this.filtroNome.set('');
    this.filtroEmail.set('');
  }

  novo() {
    this.router.navigate(['/alunos/novo']);
  }

  editar(id: number) {
    this.router.navigate(['/alunos', id]);
  }

  async alternarStatus(aluno: Aluno) {
    try {
      await this.alunosService.update(aluno.id, {
        usuarioId: aluno.usuarioId,
        dataNascimento: aluno.dataNascimento,
        ativo: !aluno.ativo
      });
      await this.carregarAlunos();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }
}
