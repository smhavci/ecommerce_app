package com.ecommerce.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.app.enums.OrderStatus;
import com.ecommerce.app.enums.ShippingStatus;

@Entity
@Table(name = "orders") // 'order' kelimesi SQL'de rezerve bir kelime olduğu için tablo adını "orders" yaptık!
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bir sipariş bir kullanıcıya ait (ManyToOne ilişki)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Siparişin ödeme bilgisi (OneToOne ilişki)
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // (PENDING, SHIPPED, DELIVERED vs.)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingStatus shippingStatus;

    @Column(nullable = false)
    private Double totalAmount;

    // Sipariş oluşturulunca tarih atansın
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    // DataInitializer için sadece ID setlemek amacıyla eklenmiştir.
    public Order(Long id) {
        this.id = id;
    }

    // Default boş constructor (ZORUNLU)
    public Order() {
    }
}
