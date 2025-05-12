import { Component, OnInit } from '@angular/core';
import { UserService } from '@app/core/user.service';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  user: any;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (data) => (this.user = data),
      error: (err) => console.error('Kullanıcı bilgisi alınamadı', err),
    });
  }
}
