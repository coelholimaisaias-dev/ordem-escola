import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AlunosService, AlunoCreateRequest } from './alunos.service';
import { UsuariosService, Usuario } from './usuarios.service';

@Component({
  selector: 'app-alunos-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './alunos-form.component.html',
  styleUrl: './alunos-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AlunosFormComponent {
  private fb = inject(FormBuilder);
  private alunosService = inject(AlunosService);
  private usuariosService = inject(UsuariosService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  form!: FormGroup;
  usuarios = signal<Usuario[]>([]);
  isLoading = signal(false);
  isSaving = signal(false);
  alunoId: number | null = null;
  isEditMode = signal(false);

  async ngOnInit() {
    this.criarForm();
    await this.carregarUsuarios();

    // Verificar se é edição
    this.alunoId = this.route.snapshot.paramMap.get('id') ?
      Number(this.route.snapshot.paramMap.get('id')) : null;

    if (this.alunoId) {
      this.isEditMode.set(true);
      await this.carregarAluno();
    }
  }

  criarForm() {
    this.form = this.fb.group({
      usuarioId: ['', Validators.required],
      dataNascimento: ['']
    });
  }

  async carregarUsuarios() {
    try {
      const usuarios = await this.usuariosService.listAll();
      this.usuarios.set(usuarios);
    } catch (error) {
      console.error('Erro ao carregar usuários:', error);
    }
  }

  async carregarAluno() {
    if (!this.alunoId) return;

    this.isLoading.set(true);
    try {
      const aluno = await this.alunosService.getById(this.alunoId);
      this.form.patchValue({
        usuarioId: aluno.usuarioId,
        dataNascimento: aluno.dataNascimento ? aluno.dataNascimento.split('T')[0] : ''
      });
    } catch (error) {
      console.error('Erro ao carregar aluno:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async salvar() {
    if (this.form.invalid) return;

    this.isSaving.set(true);
    try {
      const data = this.form.value;

      if (this.isEditMode()) {
        await this.alunosService.update(this.alunoId!, {
          usuarioId: data.usuarioId,
          dataNascimento: data.dataNascimento,
          ativo: true
        });
      } else {
        await this.alunosService.create({
          usuarioId: data.usuarioId,
          dataNascimento: data.dataNascimento
        } as AlunoCreateRequest);
      }

      this.router.navigate(['/alunos']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/alunos']);
  }
}
