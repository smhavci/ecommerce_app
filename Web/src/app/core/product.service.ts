import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  stock: number;
  createdAt?: string;

  category?: {
    id: number;
    name: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl, {
      headers: this.getAuthHeaders()
    });
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product, {
      headers: this.getAuthHeaders()
    });
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product, {
      headers: this.getAuthHeaders()
    });
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // 🆕 SATICIYA AİT ÜRÜNLERİ GETİR
  getMyProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/my-products`, {
      headers: this.getAuthHeaders()
    });
  }

  // Satıcıya ait ürünleri getir
  getProductsBySeller(sellerId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`http://localhost:8080/api/products/seller/${sellerId}`, {
      headers: this.getAuthHeaders()
    });
  }

}
