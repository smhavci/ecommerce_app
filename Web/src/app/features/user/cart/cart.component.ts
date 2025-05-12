import { Component, OnInit } from '@angular/core';
import { CartService } from '@app/core/cart.service';
import { Product } from '@app/core/product.service';
import { CartItem } from '@app/core/models/cart-item.model';
import { Router } from '@angular/router';
@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cartItems = this.cartService.getCart();
    this.calculateTotal();
  }

  calculateTotal(): void {
    this.totalPrice = this.cartItems.reduce(
      (acc, item) => acc + (item.product.price * item.quantity), 0
    );
  }

  increment(item: CartItem): void {
    item.quantity++;
    this.save();
  }

  decrement(item: CartItem): void {
    if (item.quantity > 1) {
      item.quantity--;
    } else {
      this.removeItem(item.product.id);
      return;
    }
    this.save();
  }

  removeItem(productId: number): void {
    this.cartItems = this.cartItems.filter(item => item.product.id !== productId);
    this.save();
  }

  clearCart(): void {
    this.cartService.clearCart();
    this.cartItems = [];
    this.totalPrice = 0;
  }

  save(): void {
    this.cartService.updateCart(this.cartItems);
    this.calculateTotal();
  }

  goToCheckout(): void {
    this.router.navigate(['/user/checkout']); // ✅ Sepeti Onayla sayfasına yönlendir
  }
}
