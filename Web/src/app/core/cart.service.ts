import { Injectable } from '@angular/core';
import { Product } from './product.service';
import { CartItem } from './models/cart-item.model';

@Injectable({
  providedIn: 'root'
})

export class CartService {
  private storageKey = 'cart';

  // Sepeti getir
  getCart(): CartItem[] {
    const cart = localStorage.getItem(this.storageKey);
    return cart ? JSON.parse(cart) : [];
  }

  // Sepete ürün ekle (varsa artır)
  addToCart(product: Product): void {
    if (!product || !product.id) {
      console.error('❌ Ürün sepete eklenemedi: Geçersiz ürün nesnesi.', product);
      return;
    }

    const cart: CartItem[] = this.getCart();
    const existing = cart.find(item => item.product.id === product.id);

    if (existing) {
      existing.quantity++;
    } else {
      cart.push({ product, quantity: 1 });
    }

    this.saveCart(cart);
  }

  // Miktarı güncelle
  updateQuantity(productId: number, quantity: number): void {
    const cart = this.getCart();
    const item = cart.find(i => i.product.id === productId);

    if (item) {
      item.quantity = quantity;
      if (item.quantity <= 0) {
        this.removeFromCart(productId);
        return;
      }
    }

    this.saveCart(cart);
  }

  // Ürünü sepetten çıkar
  removeFromCart(productId: number): void {
    const updatedCart = this.getCart().filter(i => i.product.id !== productId);
    this.saveCart(updatedCart);
  }

  // Sepeti tamamen temizle
  clearCart(): void {
    localStorage.removeItem(this.storageKey);
  }

  // Sepeti kaydet
  private saveCart(cart: CartItem[]): void {
    localStorage.setItem(this.storageKey, JSON.stringify(cart));
  }

  // Manuel güncelleme
  updateCart(cart: CartItem[]): void {
    this.saveCart(cart);
  }
}
