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

import { ProdutosService, ProdutoCreateRequest } from './produtos.service';
import { EmpresasService, Empresa } from './empresas.service';
import { AuthService } from './auth.service';
import { TipoProduto, TipoProdutoLabels } from './core/tipo-produto';

@Component({
  selector: 'app-produtos-form',
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
  templateUrl: './produtos-form.component.html',
  styleUrl: './produtos-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProdutosFormComponent {
  private fb = inject(FormBuilder);
  private produtosService = inject(ProdutosService);
  private empresasService = inject(EmpresasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  form!: FormGroup;
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);
  isSaving = signal(false);
  produtoId: number | null = null;
  isEditMode = signal(false);

  isAdmin = computed(() => this.authService.isAdmin());
  empresaIdUsuario = this.authService.getEmpresaId();

  readonly tiposProduto = Object.entries(TipoProdutoLabels).map(([value, label]) => ({
    value,
    label
  }));

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

    this.produtoId = this.route.snapshot.paramMap.get('id') ? Number(this.route.snapshot.paramMap.get('id')) : null;
    if (this.produtoId) {
      this.isEditMode.set(true);
      await this.carregarProduto();
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
      descricao: [''],
      tipo: ['', Validators.required],
      valorUnitario: ['', [Validators.required, Validators.min(0)]],
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

  async carregarProduto() {
    if (!this.produtoId) return;
    this.isLoading.set(true);
    try {
      const produto = await this.produtosService.getById(this.produtoId);
      this.form.patchValue({
        empresaId: produto.empresaId,
        nome: produto.nome,
        descricao: produto.descricao,
        tipo: produto.tipo,
        valorUnitario: produto.valorUnitario,
        ativo: produto.ativo
      });
    } catch (error) {
      console.error('Erro ao carregar produto:', error);
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
        await this.produtosService.update(this.produtoId!, {
          nome: data.nome,
          descricao: data.descricao,
          tipo: data.tipo,
          valorUnitario: data.valorUnitario,
          ativo: data.ativo ?? true
        });
      } else {
        await this.produtosService.create({
          empresaId: data.empresaId,
          nome: data.nome,
          descricao: data.descricao,
          tipo: data.tipo,
          valorUnitario: data.valorUnitario
        } as ProdutoCreateRequest);
      }
      this.router.navigate(['/produtos']);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      this.isSaving.set(false);
    }
  }

  cancelar() {
    this.router.navigate(['/produtos']);
  }
}
