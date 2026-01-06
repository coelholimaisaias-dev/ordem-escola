import { Component, signal, inject } from '@angular/core';
import { RouterOutlet, Router, NavigationStart } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
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
    this.router.events.subscribe((ev) => {
      if (ev instanceof NavigationStart) {
        const url = ev.url;
        if (!this.auth.isAuthenticated() && url !== '/login') {
          this.router.navigate(['/login'], { queryParams: { redirect: url } });
        }
      }
    });
  }

}
