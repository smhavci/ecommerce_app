package com.ecommerce.app.service;

import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Payment;
import com.ecommerce.app.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // CREATE - Yeni ödeme kaydet
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // READ - Tüm ödemeleri getir
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // READ - ID ile ödeme getir
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    // DELETE - Ödeme sil
    public void deletePayment(Long id) {
        boolean exists = paymentRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen ödeme kaydı bulunamadı: " + id);
        }

        paymentRepository.deleteById(id);
    }

    // UPDATE - Ödeme güncelle
    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment existing = paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ödeme bulunamadı: " + id));
    
        existing.setPaymentType(updatedPayment.getPaymentType());
        existing.setPaymentStatus(updatedPayment.getPaymentStatus());
    
        if (updatedPayment.getPaymentDate() != null) {
            existing.setPaymentDate(updatedPayment.getPaymentDate());
        }
    
        return paymentRepository.save(existing);
    }
    
}
