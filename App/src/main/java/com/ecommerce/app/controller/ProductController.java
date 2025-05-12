package com.ecommerce.app.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.Role;
import com.ecommerce.app.model.User;
import com.ecommerce.app.service.ProductService;
import com.ecommerce.app.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// ProductDTO importu
import com.ecommerce.app.dto.ProductDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final UserService userService;
    private final ProductService productService;

    @Autowired // Constructor Injection
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid Product product, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        // Eğer seller ise: bakiye kontrolü ve kullanıcıya ürün bağlama
        if (currentUser.getRole() == Role.ROLE_SELLER) {
            if (currentUser.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(null); // 400 - Bakiye yetersiz
            }

            product.setUser(currentUser); // Sadece seller'a ürün bağlanır
        }

        // Admin ise product.setUser() yapılmaz → seller null olabilir (nullable = true)
        ProductDTO savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // READ - Tüm Ürünleri Listeleme (ProductDTO listesi dönüyor)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SELLER')")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // READ - ID ile Ürün Getirme (ProductDTO Optional dönüyor)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductDTO>> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> product = productService.getProductById(id);

        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Ürün bulunamadı: " + id);
        }

        return ResponseEntity.ok(product);
    }

    // DELETE - Ürün Silme
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // UPDATE - Ürün Güncelleme 
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        ProductDTO updated = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(updated);
    }

    // ✅ Sadece giriş yapan satıcının ürünlerini getir
    @GetMapping("/my-products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDTO>> getMyProducts(@AuthenticationPrincipal UserDetails userDetails) {
        User seller = userService.findByUsername(userDetails.getUsername());
        List<Product> products = productService.getProductsByUser(seller);  // ✅ user_id ile filtrele
        List<ProductDTO> dtos = products.stream().map(ProductDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/seller/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductDTO>> getProductsBySeller(@PathVariable Long id) {
        User seller = userService.findById(id);
        List<Product> products = productService.getProductsByUser(seller);
        List<ProductDTO> dtos = products.stream().map(ProductDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }


}
