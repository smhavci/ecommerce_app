package com.ecommerce.app.controller;

import com.ecommerce.app.model.Review;
import com.ecommerce.app.service.ReviewService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.dto.ReviewDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // CREATE - Yeni Review oluştur
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody @Valid Review review) {
        ReviewDTO savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    // READ - Tüm Review'ları listele
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // READ - ID ile Review getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ReviewDTO>> getReviewById(@PathVariable Long id) {
        Optional<ReviewDTO> review = reviewService.getReviewById(id);

            if (review.isEmpty()) {
            throw new ResourceNotFoundException("Yorum bulunamadı: " + id);
        }

        return ResponseEntity.ok(review);
    }

    // DELETE - Review sil
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully!");
    }

    // UPDATE - Review güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        ReviewDTO updated = reviewService.updateReview(id, updatedReview);
        return ResponseEntity.ok(updated);
    }

}
