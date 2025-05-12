package com.ecommerce.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "store_name")
    private String storeName;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.valueOf(75000);


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
    // DataInitializer için sadece ID setlemek amacıyla eklenmiştir.
    public User(Long id) {
        this.id = id;
    }
    // Default boş constructor (ZORUNLU!)
    public User() {
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
}
