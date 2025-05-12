import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
  orderId: number;
}

export interface Order {
  id: number;
  user: {
    id: number;
    username: string;
  };
  orderDate: string;
  status: string;
  totalAmount: number;
  items: OrderItem[]; // 🔥 Burası eksikti!
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  getAllOrders(): Observable<Order[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Order[]>(this.apiUrl, { headers });
  }

  updateOrderStatus(id: number, status: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put(`${this.apiUrl}/${id}/status`, { status }, { headers });
  }

  cancelOrder(orderId: number): Observable<any> {
    return this.http.patch(`/api/orders/${orderId}/cancel`, {});
  }

  getSellerOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/seller/orders`); // Backend'deki seller orders endpoint'ine istek gönder
  }

}
