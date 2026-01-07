import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div [class.card]="true" [class.card-elevated]="elevated">
      <div *ngIf="title" class="card-header">
        <h2 class="card-title">{{ title }}</h2>
        <div *ngIf="subtitle" class="card-subtitle">{{ subtitle }}</div>
      </div>
      <div class="card-body">
        <ng-content></ng-content>
      </div>
      <div *ngIf="footer" class="card-footer">
        <ng-content select="[card-footer]"></ng-content>
      </div>
    </div>
  `,
  styleUrl: './card.component.scss'
})
export class CardComponent {
  @Input() title?: string;
  @Input() subtitle?: string;
  @Input() elevated = false;
  @Input() footer = false;
}
