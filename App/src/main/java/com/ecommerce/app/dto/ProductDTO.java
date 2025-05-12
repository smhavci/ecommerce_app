package com.ecommerce.app.dto;

import com.ecommerce.app.model.Category;
import com.ecommerce.app.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;

    private Long categoryId;         // ✔️ ID tutmak için
    private String categoryName;     // ✔️ İsmini göstermek için
    private Category category; // bu kullanılmalı
    
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.createdAt = product.getCreatedAt();

        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
            this.category = product.getCategory(); 
        }
    }
}
