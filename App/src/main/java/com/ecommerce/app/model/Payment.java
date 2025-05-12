package com.ecommerce.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @NotBlank(message = "Ödeme türü boş olamaz")
    @Column(nullable = false)
    private String paymentType; // CARD, PAYPAL vs.

    @Column(nullable = false)
    private String paymentStatus; // SUCCESS, FAILED vs.

    private LocalDateTime paymentDate;

    @PrePersist
    protected void onCreate() {
        paymentDate = LocalDateTime.now();
    }

    // DataInitializer için sadece ID setlemek amacıyla eklenmiştir.
    public Payment(Long id) {
        this.id = id;
    }

    // Default boş constructor (ZORUNLU!)
    public Payment() {
    }

}
