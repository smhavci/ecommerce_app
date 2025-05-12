package com.ecommerce.app.service;

import com.ecommerce.app.model.Review;
import com.ecommerce.app.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import com.ecommerce.app.dto.ReviewDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // CREATE - Yeni review ekle
    public ReviewDTO saveReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    // READ - Tüm reviewları getir
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ - ID ile review getir
    public Optional<ReviewDTO> getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE - Review sil
    public void deleteReview(Long id) {
        boolean exists = reviewRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen yorum bulunamadı: " + id);
        }

        reviewRepository.deleteById(id);
    }

    // UPDATE - Review güncelle
    public ReviewDTO updateReview(Long id, Review updatedReview) {
        Review existing = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı: " + id));
    
        existing.setRating(updatedReview.getRating());
        existing.setComment(updatedReview.getComment());
    
        if (updatedReview.getProduct() != null) {
            existing.setProduct(updatedReview.getProduct());
        }
    
        if (updatedReview.getUser() != null) {
            existing.setUser(updatedReview.getUser());
        }
    
        Review saved = reviewRepository.save(existing);
        return convertToDTO(saved);
    }
    

    // CONVERT METHODU
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setProductId(review.getProduct().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
