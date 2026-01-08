import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { Role } from './core/roles';

export const roleGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const auth = inject(AuthService);
  const required: Role[] = (route.data?.['roles'] as Role[]) ?? [];
  const hasAuth = auth.isAuthenticated();
  if (!hasAuth) {
    router.navigate(['/login']);
    return false;
  }
  if (required.length === 0) return true;
  const role = auth.getRole();
  const allowed = required.includes(role);
  if (!allowed) {
    router.navigate(['/']);
  }
  return allowed;
};
