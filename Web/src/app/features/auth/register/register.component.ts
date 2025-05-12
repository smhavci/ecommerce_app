import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: false
})
export class RegisterComponent {
  registerForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';

  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['ROLE_USER', Validators.required],
      storeName: ['']
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const formValue = { ...this.registerForm.value };

      // 🧽 Eğer kullanıcı ROLE_USER seçmişse, storeName gönderilmesin
      if (formValue.role !== 'ROLE_SELLER') {
        delete formValue.storeName;
      }

      this.authService.register(formValue).subscribe({
        next: () => {
          this.successMessage = 'Kayıt başarılı! Giriş ekranına yönlendiriliyorsunuz...';
          setTimeout(() => this.router.navigate(['/auth/login']), 2000);
        },
        error: () => {
          this.errorMessage = 'Kayıt başarısız. Lütfen bilgileri kontrol edin.';
        }
      });
    }
  }

}
