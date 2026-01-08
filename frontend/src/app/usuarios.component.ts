import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
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

import { UsuariosService, Usuario } from './usuarios.service';
import { EmpresasService, Empresa } from './empresas.service';

@Component({
  selector: 'app-usuarios',
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
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsuariosComponent {
  private usuariosService = inject(UsuariosService);
  private empresasService = inject(EmpresasService);
  private router = inject(Router);

  usuarios = signal<Usuario[]>([]);
  empresas = signal<Empresa[]>([]);
  isLoading = signal(false);

  filtroNome = signal('');
  filtroEmpresa = signal<number | null>(null);

  displayedColumns = ['id', 'nome', 'email', 'empresa', 'perfil', 'status', 'acoes'];

  async ngOnInit() {
    await this.carregarEmpresas();
    await this.carregarUsuarios();
  }

  async carregarUsuarios() {
    this.isLoading.set(true);
    try {
      const usuarios = await this.usuariosService.listAll(
        this.filtroNome() || undefined,
        this.filtroEmpresa() || undefined
      );
      this.usuarios.set(usuarios);
    } catch (error) {
      console.error('Erro ao carregar usuários:', error);
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

  async filtrar() {
    await this.carregarUsuarios();
  }

  async limparFiltros() {
    this.filtroNome.set('');
    this.filtroEmpresa.set(null);
    await this.carregarUsuarios();
  }

  novo() {
    this.router.navigate(['/usuarios/novo']);
  }

  editar(id: number) {
    this.router.navigate(['/usuarios', id]);
  }

  async alternarStatus(usuario: Usuario) {
    try {
      await this.usuariosService.update(usuario.id, {
        nome: usuario.nome,
        email: usuario.email,
        empresaId: usuario.empresaId,
        perfil: usuario.perfil,
        ativo: !usuario.ativo
      });
      await this.carregarUsuarios();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
    }
  }

  getEmpresaNome(empresaId: number): string {
    return this.empresas().find(e => e.id === empresaId)?.nome || '-';
  }
}
