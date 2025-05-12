import { Component, OnInit } from '@angular/core';
import { OrderService, Order } from '@app/core/order.service';
@Component({
  selector: 'app-manage-orders',
  standalone: false,
  templateUrl: './manage-orders.component.html',
  styleUrl: './manage-orders.component.css'
})
export class ManageOrdersComponent implements OnInit {
  orders: Order[] = [];
  filteredOrders: Order[] = [];
  isLoading = true;
  error: string | null = null;
  searchUsername: string = '';
  selectedOrder: Order | null = null;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.filteredOrders = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Siparişler yüklenemedi';
        this.isLoading = false;
      }
    });
  }

  updateStatus(order: Order, event: Event): void {
    const newStatus = (event.target as HTMLSelectElement).value;

    this.orderService.updateOrderStatus(order.id, newStatus).subscribe({
      next: () => {
        order.status = newStatus;
        this.applyFilter();
      },
      error: () => {
        alert('Sipariş durumu güncellenemedi.');
      }
    });
  }

  applyFilter(): void {
    const query = this.searchUsername.toLowerCase();
    this.filteredOrders = this.orders.filter(order =>
      order.user?.username?.toLowerCase().includes(query)
    );
  }

  selectOrder(order: Order): void {
    this.selectedOrder = order;
  }

  showDetails(order: Order): void {
    this.selectedOrder = order;
  }

  closeModal(): void {
    this.selectedOrder = null;
  }
}
