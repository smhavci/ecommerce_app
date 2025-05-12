import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../core/models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    let token = '';

    // SSR (Sunucu tarafı) ortamda localStorage yoktur, kontrol et
    if (typeof window !== 'undefined') {
      token = localStorage.getItem('token') || '';
    }

    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }


  // Giriş yapan kullanıcı bilgisi
  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/me`, {
      headers: this.getAuthHeaders()
    });
  }

  // Kullanıcı bilgilerini güncelle (storeName dahil)
  updateUser(id: number, userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${id}`, userData, {
      headers: this.getAuthHeaders()
    });
  }

  // Admin işlemleri
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl, {
      headers: this.getAuthHeaders()
    });
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  updateUserRole(id: number, newRole: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/role`, { role: newRole }, {
      headers: this.getAuthHeaders()
    });
  }

  getSellers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/sellers`, {
      headers: this.getAuthHeaders()
    });
  }

}
