import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('Ordem-Escola');

  private router = inject(Router);
  private auth = inject(AuthService);
  readonly isAuthenticated = this.auth.isAuthenticated;

  constructor() {
    // Navigation guarding moved to AuthGuard for clarity.
  }

  async logout() {
    await this.auth.performLogout();
    this.router.navigate(['/login']);
  }

}
