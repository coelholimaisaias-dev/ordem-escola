import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { UsuariosService, Usuario, UsuarioCreateRequest } from './usuarios.service';
import { EmpresasService, Empresa } from './empresas.service';
import { Perfil } from './core/perfil';

@Component({
  selector: 'app-usuarios-form',
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
  templateUrl: './usuarios-form.component.html',
  styleUrl: './usuarios-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsuariosFormComponent {
  private fb = inject(FormBuilder);
  private usuariosService = inject(UsuariosService);
  private empresasService = inject(EmpresasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  form!: FormGroup;
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);
  isSaving = signal(false);
  usuarioId: number | null = null;
  isEditMode = signal(false);

  perfis = Object.values(Perfil);

  async ngOnInit() {
    this.criarForm();
    await this.carregarEmpresas();

    // Verificar se é edição
    this.usuarioId = this.route.snapshot.paramMap.get('id') ?
      Number(this.route.snapshot.paramMap.get('id')) : null;

    if (this.usuarioId) {
      this.isEditMode.set(true);
      await this.carregarUsuario();
    }
  }

  criarForm() {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', this.usuarioId ? [] : Validators.required],
      empresaId: ['', Validators.required],
      perfil: ['', Validators.required]
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

  async carregarUsuario() {
    if (!this.usuarioId) return;

    this.isLoading.set(true);
    try {
      const usuario = await this.usuariosService.getById(this.usuarioId);
      this.form.patchValue({
        nome: usuario.nome,
        email: usuario.email,
        empresaId: usuario.empresaId,
        perfil: usuario.perfil
      });
      // Remove a validação de senha em modo edição
      this.form.get('senha')?.clearAsyncValidators();
    } catch (error) {
      console.error('Erro ao carregar usuário:', error);
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
        await this.usuariosService.update(this.usuarioId!, {
          nome: data.nome,
          email: data.email,
          empresaId: data.empresaId,
          perfil: data.perfil,
          ativo: true // manter ativo ao editar
        });
      } else {
        await this.usuariosService.create(data as UsuarioCreateRequest);
      }

      this.router.navigate(['/usuarios']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/usuarios']);
  }
}
