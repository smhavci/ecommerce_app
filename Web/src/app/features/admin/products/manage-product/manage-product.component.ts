import { Component, OnInit } from '@angular/core';
import { ProductService } from '@app/core/product.service';
import { Product } from '@app/core/product.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '@app/core/category.service';
import { Category } from '@app/core/models/category.model';


@Component({
  selector: 'app-manage-product',
  standalone: false,
  templateUrl: './manage-product.component.html',
  styleUrl: './manage-product.component.css'
})


export class ManageProductComponent implements OnInit {
  categories: Category[] = [];
  products: Product[] = [];
  productForm!: FormGroup;
  isLoading = true;
  error: string | null = null;
  editMode = false;
  selectedProductId: number | null = null;

  constructor(
    private productService: ProductService,
    private fb: FormBuilder,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.fetchProducts();

    // Kategorileri API'den alıyoruz
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
        console.log('Kategori verisi:', this.categories); // Burada kategorilerin gelmesini kontrol edin.
      },
      error: (err) => {
        console.error('Kategori listesi yüklenemedi:', err);
      }
    });
  }


  initForm(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      category: [null, Validators.required] // Kategori seçimi zorunlu hale getirildi
    });
  }

  fetchProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Ürünler yüklenemedi.';
        this.isLoading = false;
      }
    });
  }

  submit(): void {
    if (this.productForm.invalid) return;

    const productData = this.productForm.value;
    if (this.editMode && this.selectedProductId !== null) {
      // Güncelleme
      this.productService.updateProduct(this.selectedProductId, productData).subscribe({
        next: (updated) => {
          const index = this.products.findIndex(p => p.id === updated.id);
          this.products[index] = updated;
          this.resetForm();
        },
        error: (err) => {
          console.error('Güncelleme hatası:', err);
          alert('Ürün güncellenemedi.');
        }
      });
    } else {
      // Yeni ürün
      this.productService.createProduct(productData).subscribe({
        next: (created) => {
          this.products.push(created);
          this.resetForm();
        },
        error: (err) => {
          console.error('Ekleme hatası:', err);
          alert('Ürün eklenemedi.');
        }
      });
    }
  }

  editProduct(product: Product): void {
    this.editMode = true;
    this.selectedProductId = product.id;
    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      stock: product.stock,
      category: product.category?.id || null
    });
  }

  resetForm(): void {
    this.productForm.reset();
    this.editMode = false;
    this.selectedProductId = null;
  }

  deleteProduct(id: number): void {
    if (!confirm('Bu ürünü silmek istediğinizden emin misiniz?')) return;

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        console.log('Silme başarılı')
        this.products = this.products.filter(p => p.id !== id);
      },
      error: () => {
        alert('Silme işlemi başarısız.');
      }
    });
  }
}
