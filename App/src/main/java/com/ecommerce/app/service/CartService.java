package com.ecommerce.app.service;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Cart;
import com.ecommerce.app.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // CREATE - Sepet oluştur (DTO)
    public CartDTO saveCart(Cart cart) {
        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }

    // READ - Tüm sepetleri getir (DTO)
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ - ID ile sepet getir (DTO)
    public Optional<CartDTO> getCartById(Long id) {
        return cartRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE - Sepet sil
    public void deleteCart(Long id) {
        boolean exists = cartRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen sepet bulunamadı: " + id);
        }

        cartRepository.deleteById(id);
    }

    // UPDATE - Sepet güncelle
    public CartDTO updateCart(Long id, Cart updatedCart) {
        Cart existing = cartRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sepet bulunamadı: " + id));
    
        // Sepetin kullanıcısı değiştirilecekse:
        if (updatedCart.getUser() != null) {
            existing.setUser(updatedCart.getUser());
        }
    
        // createdAt alanını elle değiştiriyorsan, kontrol:
        if (updatedCart.getCreatedAt() != null) {
            existing.setCreatedAt(updatedCart.getCreatedAt());
        }
    
        Cart saved = cartRepository.save(existing);
        return convertToDTO(saved);
    }
    

    // CONVERTTODTO METHODU KURULDU!!!
    private CartDTO convertToDTO(Cart cart) {
    CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCreatedAt(cart.getCreatedAt());
        return dto;
    }
}
