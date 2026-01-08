import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../auth.service';
import { MENU_ITEMS } from '../../../core/menu';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [CommonModule, MatListModule, MatIconModule, RouterModule],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidenavComponent {
  protected auth = inject(AuthService);
  protected readonly menus = MENU_ITEMS;
  protected isCollapsed = signal(false);

  isItemAllowed(roles: string[]): boolean {
    return roles.includes(this.auth.getRole());
  }

  toggleSidebar() {
    this.isCollapsed.update(v => !v);
  }
}
