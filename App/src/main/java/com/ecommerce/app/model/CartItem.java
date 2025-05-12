package com.ecommerce.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cart - CartItem ilişkisi (ManyToOne)
    @NotNull(message = "Sepet bilgisi boş olamaz")
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // CartItem - Product ilişkisi (ManyToOne)
    @NotNull(message = "Ürün bilgisi boş olamaz")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Ürün adedi boş olamaz")
    @Min(value = 1, message= "Ürün adedi en az 1 olmalıdır")
    @Column(nullable = false)
    private Integer quantity;
}
