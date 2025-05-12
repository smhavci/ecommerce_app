import { Component, OnInit } from '@angular/core';
import { UserService } from '@app/core/user.service';
import { User } from '@app/core/models/user.model';

@Component({
  selector: 'app-seller-profile',
  standalone: false,
  templateUrl: './seller-profile.component.html',
  styleUrl: './seller-profile.component.css'
})
export class SellerProfileComponent implements OnInit {
  user: User | null = null;
  originalStoreName: string = '';
  editMode: boolean = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (data) => {
        this.user = data;
        this.originalStoreName = data.storeName || '';
      },
      error: (err) => console.error('Satıcı bilgisi alınamadı', err),
    });
  }

  enableEdit(): void {
    this.editMode = true;
  }

  cancelEdit(): void {
    if (this.user) {
      this.user.storeName = this.originalStoreName;
    }
    this.editMode = false;
  }

  saveChanges(): void {
    if (!this.user?.id) return;

    this.userService.updateUser(this.user.id, this.user).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.editMode = false;
      },
      error: (err) => {
        console.error('Güncelleme hatası:', err);
      }
    });
  }
}
