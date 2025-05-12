package com.ecommerce.app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cart - User ilişkisi (OneToOne)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Cart - CartItem ilişkisi (OneToMany)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Sepetin oluşturulma zamanı
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // DataInitializer için sadece ID setlemek amacıyla eklenmiştir.
    public Cart(Long id) {
        this.id = id;
    }
    // Default boş constructor (ZORUNLU!)
    public Cart() {
    }
}
