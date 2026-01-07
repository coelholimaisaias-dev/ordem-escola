import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Components
import { ButtonComponent } from './components/button/button.component';
import { CardComponent } from './components/card/card.component';
import { AlertComponent } from './components/alert/alert.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FormFieldComponent } from './components/form-field/form-field.component';

const SHARED_COMPONENTS = [
  ButtonComponent,
  CardComponent,
  AlertComponent,
  NavbarComponent,
  FormFieldComponent
];

@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule, ...SHARED_COMPONENTS],
  exports: [CommonModule, FormsModule, ReactiveFormsModule, ...SHARED_COMPONENTS]
})
export class SharedModule {}

// Export components for standalone usage
export {
  ButtonComponent,
  CardComponent,
  AlertComponent,
  NavbarComponent,
  FormFieldComponent
};
