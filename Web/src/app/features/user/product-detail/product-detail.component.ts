import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '@app/core/cart.service';
import { ProductService, Product } from '@app/core/product.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-product-detail',
  standalone: false,
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {
  product!: Product;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id).subscribe({
      next: (data) => this.product = data,
      error: (err) => console.error('Ürün yüklenemedi:', err)
    });
  }

  addToCart(): void {
    if (this.product) {
      this.cartService.addToCart(this.product);
      this.router.navigate(['/user/cart']);
    }
  }


}
