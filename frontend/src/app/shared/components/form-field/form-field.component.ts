import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-form-field',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-field.component.html',
  styleUrl: './form-field.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FormFieldComponent),
      multi: true
    }
  ]
})
export class FormFieldComponent implements ControlValueAccessor {
  @Input() label = '';
  @Input() placeholder = '';
  @Input() type: 'text' | 'email' | 'password' | 'number' = 'text';
  @Input() disabled = false;
  @Input() error: string | null = null;
  @Input() helper = '';
  @Output() valueChange = new EventEmitter<string>();

  value = '';
  touched = false;

  onBlur(): void {
    this.touched = true;
  }

  onChange(val: string): void {
    this.value = val;
    this.valueChange.emit(val);
    this.onChangeCallback(val);
  }

  // ControlValueAccessor methods
  onChangeCallback = (val: any) => {};
  onTouchedCallback = () => {};

  writeValue(val: any): void {
    if (val !== undefined && val !== null) {
      this.value = val;
    }
  }

  registerOnChange(fn: any): void {
    this.onChangeCallback = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouchedCallback = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
