import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { ServicosService, ServicoCreateRequest } from './servicos.service';
import { EmpresasService, Empresa } from './empresas.service';
import { Turno } from './turmas.service';
import { AuthService } from './auth.service';
import { TURNOS } from './core/turnos';

@Component({
  selector: 'app-servicos-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSlideToggleModule
  ],
  templateUrl: './servicos-form.component.html',
  styleUrl: './servicos-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ServicosFormComponent {
  private fb = inject(FormBuilder);
  private servicosService = inject(ServicosService);
  private empresasService = inject(EmpresasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  form!: FormGroup;
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);
  isSaving = signal(false);
  servicoId: number | null = null;
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

    this.servicoId = this.route.snapshot.paramMap.get('id') ? Number(this.route.snapshot.paramMap.get('id')) : null;
    if (this.servicoId) {
      this.isEditMode.set(true);
      await this.carregarServico();
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
      valorBase: ['', [Validators.required, Validators.min(0)]],
      ativo: [true]
    });
  }

  async carregarEmpresas() {
    try {
      const lista = await this.empresasService.listAll();
      this.empresas.set(lista);
    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
    }
  }

  async carregarServico() {
    if (!this.servicoId) return;
    this.isLoading.set(true);
    try {
      const servico = await this.servicosService.getById(this.servicoId);
      this.form.patchValue({
        empresaId: servico.empresaId,
        nome: servico.nome,
        turno: servico.turno,
        valorBase: servico.valorBase,
        ativo: servico.ativo
      });
    } catch (error) {
      console.error('Erro ao carregar serviço:', error);
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
        await this.servicosService.update(this.servicoId!, {
          empresaId: data.empresaId,
          nome: data.nome,
          turno: data.turno,
          valorBase: data.valorBase,
          ativo: data.ativo ?? true
        });
      } else {
        await this.servicosService.create({
          empresaId: data.empresaId,
          nome: data.nome,
          turno: data.turno,
          valorBase: data.valorBase
        } as ServicoCreateRequest);
      }
      this.router.navigate(['/servicos']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/servicos']);
  }
}
