import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService, Product } from '@app/core/product.service';
import { CategoryService } from '@app/core/category.service';
import { Category } from '@app/core/models/category.model';

@Component({
  selector: 'app-product-create',
  standalone: false,
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.css'
})
export class ProductCreateComponent implements OnInit {
  categories: Category[] = [];
  products: Product[] = [];
  productForm!: FormGroup;
  isLoading = true;
  error: string | null = null;
  editMode = false;
  selectedProductId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.fetchProducts();
    this.loadCategories();
  }

  private initForm(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      category: [null, Validators.required]
    });
  }

  fetchProducts(): void {
    this.productService.getMyProducts().subscribe({
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


  private loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Kategori listesi yüklenemedi:', err);
      }
    });
  }

  submit(): void {
    if (this.productForm.invalid) return;

    const formValue = this.productForm.value;

    // Backend'in beklediği gibi objeyi düzenle
    const productData = {
      ...formValue,
      category: {
        id: formValue.category.id // 👈 burada sadece id’yi al
      }
    };

    if (this.editMode && this.selectedProductId !== null) {
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

  deleteProduct(id: number): void {
    if (!confirm('Bu ürünü silmek istediğinizden emin misiniz?')) return;

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products = this.products.filter(p => p.id !== id);
      },
      error: () => {
        alert('Silme işlemi başarısız.');
      }
    });
  }

  resetForm(): void {
    this.productForm.reset();
    this.editMode = false;
    this.selectedProductId = null;
  }
}
