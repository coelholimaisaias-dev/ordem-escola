import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  
  console.log('AuthInterceptor - Token:', token, 'URL:', req.url);
  
  if (!token) {
    console.warn('Sem token, enviando requisição sem Authorization');
    return next(req);
  }
  
  console.log('Adicionando Authorization header com token');
  const cloned = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  return next(cloned);
};

