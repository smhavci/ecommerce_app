package com.ecommerce.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ürün adı boş olamaz")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Fiyat boş olamaz")
    @DecimalMin(value = "0.01", message = "Fiyat sıfırdan büyük olmalıdır")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Stok boş olamaz")
    @Min(value = 0, message = "Stok negatif olamaz")
    @Column(nullable = false)
    private Integer stock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product() {
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


    // ✅ Yeni: Ürünü oluşturan satıcı
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = true) // nullable önemli!
    private User seller;

    @ManyToOne
    @JoinColumn(name = "user_id") // ürün hangi kullanıcıya/satıcıya ait
    private User user;
}
