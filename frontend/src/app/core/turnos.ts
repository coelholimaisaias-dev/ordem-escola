import { Turno } from '../turmas.service';

export interface TurnoOption {
  value: Turno;
  label: string;
}

export const TURNOS: TurnoOption[] = [
  { value: Turno.MANHA, label: 'Manhã' },
  { value: Turno.TARDE, label: 'Tarde' },
  { value: Turno.INTEGRAL, label: 'Integral' }
];

export function getTurnoLabel(turno: Turno): string {
  return TURNOS.find(t => t.value === turno)?.label ?? turno;
}
