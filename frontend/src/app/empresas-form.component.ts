import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { EmpresasService, EmpresaCreateRequest } from './empresas.service';

@Component({
  selector: 'app-empresas-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './empresas-form.component.html',
  styleUrl: './empresas-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmpresasFormComponent {
  private fb = inject(FormBuilder);
  private empresasService = inject(EmpresasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  form!: FormGroup;
  isLoading = signal(false);
  isSaving = signal(false);
  empresaId: number | null = null;
  isEditMode = signal(false);

  async ngOnInit() {
    this.criarForm();

    // Verificar se é edição
    this.empresaId = this.route.snapshot.paramMap.get('id') ?
      Number(this.route.snapshot.paramMap.get('id')) : null;

    if (this.empresaId) {
      this.isEditMode.set(true);
      await this.carregarEmpresa();
    }
  }

  criarForm() {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      cnpj: ['', [Validators.required, Validators.pattern(/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/)]]
    });

    // Aplicar máscara ao CNPJ
    this.form.get('cnpj')?.valueChanges.subscribe(value => {
      if (value) {
        const cnpj = value.replace(/\D/g, '');
        if (cnpj.length <= 14) {
          const formatted = this.aplicarMascaraCnpj(cnpj);
          if (formatted !== value) {
            this.form.get('cnpj')?.setValue(formatted, { emitEvent: false });
          }
        }
      }
    });
  }

  aplicarMascaraCnpj(cnpj: string): string {
    cnpj = cnpj.replace(/\D/g, '');
    cnpj = cnpj.replace(/^(\d{2})(\d)/, '$1.$2');
    cnpj = cnpj.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
    cnpj = cnpj.replace(/\.(\d{3})(\d)/, '.$1/$2');
    cnpj = cnpj.replace(/(\d{4})(\d)/, '$1-$2');
    return cnpj;
  }

  async carregarEmpresa() {
    if (!this.empresaId) return;

    this.isLoading.set(true);
    try {
      const empresa = await this.empresasService.getById(this.empresaId);
      this.form.patchValue({
        nome: empresa.nome,
        cnpj: this.aplicarMascaraCnpj(empresa.cnpj)
      });
    } catch (error) {
      console.error('Erro ao carregar empresa:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async salvar() {
    if (this.form.invalid) return;

    this.isSaving.set(true);
    try {
      const data = this.form.value;
      const cnpjLimpo = data.cnpj.replace(/\D/g, '');

      if (this.isEditMode()) {
        await this.empresasService.update(this.empresaId!, {
          nome: data.nome,
          cnpj: cnpjLimpo,
          ativo: true
        });
      } else {
        await this.empresasService.create({
          nome: data.nome,
          cnpj: cnpjLimpo
        } as EmpresaCreateRequest);
      }

      this.router.navigate(['/empresas']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/empresas']);
  }
}
