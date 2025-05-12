import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: false
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    console.log('Form gönderildi');

    if (this.loginForm.valid) {
      console.log('Form geçerli, login denemesi yapılıyor...');

      this.authService.login(this.loginForm.value).subscribe({
        next: (res) => {
          console.log('Giriş başarılı, token:', res.token);
          localStorage.setItem('token', res.token);

          this.alertMessage = 'Giriş başarılı!';
          this.alertType = 'success';

          this.router.navigate(['/']); // veya yönlenmesini istediğin rota
        },
        error: (err) => {
          console.error('Hata oluştu:', err);
          this.errorMessage = 'Kullanıcı adı veya şifre yanlış';

          this.alertMessage = 'Giriş başarısız! Lütfen bilgileri kontrol edin.';
          this.alertType = 'error';
        }
      });
    } else {
      console.warn('Form geçersiz!');
    }
  }

}
