import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService, Product } from '@app/core/product.service';
import { CategoryService } from '@app/core/category.service';
import { Category } from '@app/core/models/category.model';
import { CartService } from '@app/core/cart.service';
import { UserService } from '@app/core/user.service';
import { User } from '@app/core/models/user.model';
@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
  standalone: false
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];

  categories: Category[] = [];
  sellers: User[] = [];

  selectedCategoryId: string = '';
  selectedSellerId: string = '';

  isLoading: boolean = true;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private cartService: CartService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchProducts();
    this.fetchCategories();
    this.loadSellers();
  }

  fetchProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.filterProducts(); // filtreyi burada da uygula
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Ürünler yüklenemedi:', err);
        this.isLoading = false;
      }
    });
  }

  fetchCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Kategoriler yüklenemedi:', err);
      }
    });
  }

  loadSellers(): void {
    this.userService.getSellers().subscribe({
      next: (data) => this.sellers = data,
      error: () => console.error('Mağazalar yüklenemedi')
    });
  }

  filterByCategory(): void {
    if (!this.selectedCategoryId) {
      this.filteredProducts = this.products;
    } else {
      this.filteredProducts = this.products.filter(
        p => p.category?.id === +this.selectedCategoryId
      );
    }
  }


  filterBySeller(): void {
    if (!this.selectedSellerId) {
      // Mağaza seçilmediyse tüm ürünleri getir
      this.fetchProducts();
      return;
    }

    this.productService.getProductsBySeller(+this.selectedSellerId).subscribe({
      next: (data) => {
        this.products = data;
        this.filterProducts(); // kategoriye göre tekrar filtrele
      },
      error: () => {
        console.error('Ürünler filtrelenemedi');
      }
    });
  }


  filterProducts(): void {
    this.filteredProducts = this.products.filter(p => {
      const categoryMatch = !this.selectedCategoryId || p.category?.id === +this.selectedCategoryId;
      return categoryMatch;
    });
  }

  goToDetail(id: number): void {
    this.router.navigate(['/user/products', id]);
  }

  addToCart(product: Product): void {
    this.cartService.addToCart(product);
    alert(`'${product.name}' sepete eklendi.`);
  }
}
