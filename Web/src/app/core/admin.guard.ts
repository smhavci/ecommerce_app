import { Injectable, inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from './auth.service';

export const AdminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  return authService.getUserRole() === 'ROLE_ADMIN';
};
