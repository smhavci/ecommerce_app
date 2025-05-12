import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SellerGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const role = this.authService.getUserRole(); // JWT'den rolü al
    if (role === 'ROLE_SELLER') {
      return true; // sadece seller geçebilir
    }

    this.router.navigate(['/']); // yetkisi olmayanları anasayfaya gönder
    return false;
  }
}
