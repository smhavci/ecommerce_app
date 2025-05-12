package com.ecommerce.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bir Order'a ait (ManyToOne ilişki)
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Bir Product'a ait (ManyToOne ilişki)
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;
}
