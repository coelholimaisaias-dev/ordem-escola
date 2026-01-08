import { Role } from './roles';
import { Perfil } from './perfil';

export interface MenuItem {
  label: string;
  path: string;
  icon: string;
  roles: Role[]; // allowed roles
}

export const MENU_ITEMS: MenuItem[] = [
  { label: 'Usuários', path: '/usuarios', icon: 'people', roles: [Perfil.ADMIN] },
  { label: 'Empresas', path: '/empresas', icon: 'business', roles: [Perfil.ADMIN] },
  { label: 'Turmas', path: '/turmas', icon: 'groups', roles: [Perfil.ADMIN, Perfil.CLIENTE] },
  { label: 'Alunos', path: '/alunos', icon: 'school', roles: [Perfil.ADMIN, Perfil.CLIENTE] },
  { label: 'Serviços', path: '/servicos', icon: 'handyman', roles: [Perfil.ADMIN, Perfil.CLIENTE] },
];
