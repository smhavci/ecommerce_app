package com.ecommerce.app.controller;

import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Payment;
import com.ecommerce.app.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // CREATE - Ödeme Ekle
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody @Valid Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return ResponseEntity.ok(savedPayment);
    }

    // READ - Tüm Ödemeleri Listele
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // READ - ID ile Ödeme Getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Payment>> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);

        if (payment.isEmpty()) {
            throw new ResourceNotFoundException("Ödeme kaydı bulunamadı: " + id);
        }

        return ResponseEntity.ok(payment);
    }

    // DELETE - Ödeme Sil
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully!");
    }

    // UPDATE - Ödeme Güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment updatedPayment) {
        Payment updated = paymentService.updatePayment(id, updatedPayment);
        return ResponseEntity.ok(updated);
    }

}
