package com.ecommerce.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Kullanıcı boş olamaz")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Ürün boş olamaz")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @NotNull(message = "Puan boş olamaz")
    @Min(value = 1, message = "Puan en az 1 olmalıdır")
    @Max(value = 5, message = "Puan en fazla 5 olmalıdır")
    @Column(nullable = false)
    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
