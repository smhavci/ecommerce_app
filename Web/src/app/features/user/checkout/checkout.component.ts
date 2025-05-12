import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartService } from '@app/core/cart.service';
import { Product } from '@app/core/product.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CartItem } from '@app/core/models/cart-item.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  checkoutForm!: FormGroup;
  cartItems: CartItem[] = [];
  totalPrice: number = 0;

  constructor(
    private fb: FormBuilder,
    private cartService: CartService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cartItems = this.cartService.getCart();

    this.totalPrice = this.cartItems.reduce(
      (acc, item) => acc + item.product.price * item.quantity,
      0
    );

    this.checkoutForm = this.fb.group({
      fullName: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', Validators.required]
    });
  }

  submitOrder(): void {
    if (this.checkoutForm.invalid) return;

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Giriş yapmanız gerekmektedir.');
      return;
    }

    const items = this.cartItems.map(item => ({
      productId: item.product.id,
      quantity: item.quantity,
      price: item.product.price
    }));

    const orderPayload = {
      fullName: this.checkoutForm.value.fullName,
      address: this.checkoutForm.value.address,
      phone: this.checkoutForm.value.phone,
      items
    };

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http.post('http://localhost:8080/api/orders', orderPayload, { headers }).subscribe({
      next: () => {
        alert('✅ Siparişiniz başarıyla alındı!');
        this.cartService.clearCart();
        this.router.navigate(['/user/order-history']); // ✅ Sipariş geçmişine yönlendirme
      },
      error: (err) => {
        console.error('❌ Sipariş gönderilemedi:', err);

        const errorMessage = err.error?.message || JSON.stringify(err.error) || 'Sipariş gönderilirken bir hata oluştu.';
        alert(errorMessage);
      }

    });
  }
}
