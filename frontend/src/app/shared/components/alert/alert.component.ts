import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

export type AlertType = 'success' | 'error' | 'warning' | 'info';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss'
})
export class AlertComponent {
  @Input() type: AlertType = 'info';
  @Input() message = '';
  @Input() dismissible = true;
  @Output() dismissed = new EventEmitter<void>();

  isDismissed = false;

  dismiss(): void {
    this.isDismissed = true;
    this.dismissed.emit();
  }

  getIcon(): string {
    const icons: Record<AlertType, string> = {
      success: '✓',
      error: '✕',
      warning: '⚠',
      info: 'ⓘ'
    };
    return icons[this.type];
  }
}
