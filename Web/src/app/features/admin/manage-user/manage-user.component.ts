import { Component, OnInit } from '@angular/core';
import { UserService } from '@app/core/user.service';
import { User } from '@app/core/models/user.model';

@Component({
  selector: 'app-manage-user',
  standalone: false,
  templateUrl: './manage-user.component.html',
  styleUrl: './manage-user.component.css'
})
export class ManageUserComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  searchQuery: string = '';
  isLoading = true;
  error: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.filteredUsers = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Kullanıcılar yüklenemedi.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  deleteUser(id: number): void {
    if (!confirm('Bu kullanıcıyı silmek istediğinize emin misiniz?')) return;

    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.users = this.users.filter(user => user.id !== id);
        this.applyFilter();
      },
      error: (err) => {
        if (err.status === 404) {
          // Kullanıcı zaten silinmiş, kullanıcıyı listeden çıkar ve sessizce geç
          this.users = this.users.filter(user => user.id !== id);
          this.applyFilter();
          console.warn('Kullanıcı zaten silinmişti.');
        } else {
          console.error('Kullanıcı silinemedi:', err);
          alert('Kullanıcı silinirken bir hata oluştu.');
        }
      }
    });
  }


  applyFilter(): void {
    const query = this.searchQuery.toLowerCase().trim();
    this.filteredUsers = this.users.filter(user =>
      user.username.toLowerCase().includes(query) ||
      user.email.toLowerCase().includes(query)
    );
  }

  changeUserRole(userId: number, currentRole: string): void {
    const newRole = currentRole === 'ROLE_ADMIN' ? 'ROLE_USER' : 'ROLE_ADMIN';
    this.userService.updateUserRole(userId, newRole).subscribe({
      next: () => {
        const updated = this.users.find(u => u.id === userId);
        if (updated) updated.role = newRole;
      },
      error: () => {
        alert('Rol güncellenemedi.');
      }
    });
  }
}
