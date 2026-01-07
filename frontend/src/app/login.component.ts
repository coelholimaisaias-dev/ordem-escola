import { Component, ChangeDetectionStrategy, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { AuthService } from './auth.service';
import { CardComponent } from './shared/components/card/card.component';
import { ButtonComponent } from './shared/components/button/button.component';
import { AlertComponent } from './shared/components/alert/alert.component';
import { FormFieldComponent } from './shared/components/form-field/form-field.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    CardComponent,
    ButtonComponent,
    AlertComponent,
    FormFieldComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private auth = inject(AuthService);

  email = '';
  senha = '';
  errorMessage: string | null = null;
  successMessage: string | null = null;
  isLoading = signal(false);

  async submit() {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.email || !this.senha) {
      this.errorMessage = 'E-mail e senha são obrigatórios';
      return;
    }

    this.isLoading.set(true);

    try {
      const result = await this.auth.login(this.email, this.senha);

      if (result.ok) {
        this.successMessage = 'Login realizado com sucesso!';

        setTimeout(() => {
          const redirect = this.route.snapshot.queryParamMap.get('redirect') || '/';
          this.router.navigateByUrl(redirect === '/login' ? '/' : redirect);
        }, 500);
      } else {
        this.errorMessage = result.message;
      }
    } catch (error) {
      this.errorMessage = 'Erro ao tentar fazer login. Tente novamente.';
    } finally {
      this.isLoading.set(false);
    }
  }

  cancel() {
    this.email = '';
    this.senha = '';
    this.errorMessage = null;
    this.successMessage = null;
  }
}
