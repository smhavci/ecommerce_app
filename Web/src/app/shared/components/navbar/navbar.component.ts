import { Component, OnInit } from '@angular/core';
import { AuthService } from '@app/core/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  username: string | null = null;
  role: string | null = null;
  profileLink: string = '/user/profile'; // Varsayılan

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();
    this.role = this.authService.getUserRole();

    this.setProfileLink(); // Profil linkini ayarla

    this.authService.loginStatus$.subscribe(status => {
      this.isLoggedIn = status;
      this.username = this.authService.getUsername();
      this.role = this.authService.getUserRole();
      this.setProfileLink(); // Login olunca tekrar ayarla
    });
  }

  setProfileLink(): void {
    if (this.role === 'ROLE_SELLER') {
      this.profileLink = '/seller/profile';
    } else if (this.role === 'ROLE_ADMIN') {
      this.profileLink = '/admin/users'; // Admin profil yerine yönetim sayfasına yönlenebilir
    } else {
      this.profileLink = '/user/profile';
    }
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.username = null;
    this.role = null;
    this.router.navigate(['/auth/login']);
  }
}
