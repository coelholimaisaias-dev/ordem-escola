import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { TurmasService, Turno, TurmaCreateRequest } from './turmas.service';
import { EmpresasService, Empresa } from './empresas.service';
import { AuthService } from './auth.service';
import { TURNOS } from './core/turnos';

@Component({
  selector: 'app-turmas-form',
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
  templateUrl: './turmas-form.component.html',
  styleUrl: './turmas-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TurmasFormComponent {
  private fb = inject(FormBuilder);
  private turmasService = inject(TurmasService);
  private empresasService = inject(EmpresasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  form!: FormGroup;
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);
  isSaving = signal(false);
  turmaId: number | null = null;
  isEditMode = signal(false);

  isAdmin = computed(() => this.authService.isAdmin());
  empresaIdUsuario = this.authService.getEmpresaId();
  readonly turnos = TURNOS;

  // Filtra empresas: CLIENTE vê só sua empresa
  empresasFiltradas = computed(() => {
    const todasEmpresas = this.empresas();
    if (this.isAdmin()) {
      return todasEmpresas;
    }
    return todasEmpresas.filter(e => e.id === this.empresaIdUsuario);
  });

  async ngOnInit() {
    this.criarForm();
    await this.carregarEmpresas();

    // Verificar se é edição
    this.turmaId = this.route.snapshot.paramMap.get('id') ?
      Number(this.route.snapshot.paramMap.get('id')) : null;

    if (this.turmaId) {
      this.isEditMode.set(true);
      await this.carregarTurma();
    } else {
      // Se CLIENTE, pré-preencher com sua empresa
      if (!this.isAdmin() && this.empresaIdUsuario) {
        this.form.patchValue({ empresaId: this.empresaIdUsuario });
      }
    }
  }

  criarForm() {
    this.form = this.fb.group({
      empresaId: ['', Validators.required],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      turno: ['', Validators.required],
      capacidade: ['', [Validators.required, Validators.min(1)]],
      valorBase: ['', [Validators.required, Validators.min(0)]]
    });
  }

  async carregarEmpresas() {
    try {
      const empresas = await this.empresasService.listAll();
      this.empresas.set(empresas);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  }

  async carregarTurma() {
    if (!this.turmaId) return;

    this.isLoading.set(true);
    try {
      const turma = await this.turmasService.getById(this.turmaId);
      this.form.patchValue({
        empresaId: turma.empresaId,
        nome: turma.nome,
        turno: turma.turno,
        capacidade: turma.capacidade,
        valorBase: turma.valorBase
      });

      // CLIENTE só pode editar turmas da sua empresa
      if (!this.isAdmin() && turma.empresaId !== this.empresaIdUsuario) {
        this.router.navigate(['/turmas']);
        return;
      }
    } catch (error) {
      console.error('Erro ao carregar turma:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async salvar() {
    if (this.form.invalid) return;

    // CLIENTE sempre salva na sua empresa
    const empresaId = this.isAdmin() ? this.form.get('empresaId')?.value : this.empresaIdUsuario;

    this.isSaving.set(true);
    try {
      const data = this.form.value;

      if (this.isEditMode()) {
        await this.turmasService.update(this.turmaId!, {
          empresaId: empresaId,
          nome: data.nome,
          turno: data.turno,
          capacidade: data.capacidade,
          valorBase: data.valorBase,
          ativo: true
        });
      } else {
        await this.turmasService.create({
          empresaId: empresaId,
          nome: data.nome,
          turno: data.turno,
          capacidade: data.capacidade,
          valorBase: data.valorBase
        } as TurmaCreateRequest);
      }

      this.router.navigate(['/turmas']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/turmas']);
  }
}
