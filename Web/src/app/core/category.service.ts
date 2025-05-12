import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from './models/category.model';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = 'http://localhost:8080/api/categories';

  constructor(private http: HttpClient) {}

  // Auth header için fonksiyon
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  // Kategorileri getiren fonksiyon
  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl, {
      headers: this.getAuthHeaders()
    });
  }
}
