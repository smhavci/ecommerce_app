import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Order {
  id: number;
  status: string;
  totalAmount: number;
  orderDate: string;
}

@Component({
  selector: 'app-order-history',
  standalone: false,
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.css'
})
export class OrderHistoryComponent implements OnInit {
  orders: Order[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    this.http.get<Order[]>('http://localhost:8080/api/orders/user', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (data) => this.orders = data,
      error: (err) => console.error('Siparişler yüklenemedi:', err)
    });
  }
}
