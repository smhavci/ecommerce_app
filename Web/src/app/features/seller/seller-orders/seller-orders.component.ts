import { Component, OnInit } from '@angular/core';
import { OrderService } from '@app/core/order.service';

@Component({
  selector: 'app-seller-orders',
  standalone: false,
  templateUrl: './seller-orders.component.html',
  styleUrl: './seller-orders.component.css'
})

export class SellerOrdersComponent implements OnInit {
  orders: any[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders(); // Siparişleri yükle
  }

  loadOrders(): void {
    this.orderService.getSellerOrders().subscribe(data => {
      this.orders = data; // Siparişleri ekrana yazdır
    });
  }

  cancelOrder(orderId: number): void {
    this.orderService.cancelOrder(orderId).subscribe({
      next: () => {
        alert("Sipariş başarıyla iptal edildi.");
        this.loadOrders(); // Listeyi yenile
      },
      error: () => {
        alert("Sipariş iptal edilemedi.");
      }
    });
  }
}
