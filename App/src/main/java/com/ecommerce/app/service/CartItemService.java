package com.ecommerce.app.service;

import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import com.ecommerce.app.dto.CartItemDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // CREATE
    public CartItemDTO saveCartItem(CartItem cartItem) {
        CartItem savedItem = cartItemRepository.save(cartItem);
        return convertToDTO(savedItem);
    }

    // READ - Tüm CartItem'ları getir
    public List<CartItemDTO> getAllCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ - ID ile CartItem getir
    public Optional<CartItemDTO> getCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    // UPDATE
    public CartItemDTO updateCartItem(Long id, CartItem updatedItem) {
        CartItem existing = cartItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CartItem bulunamadı: " + id));

        existing.setQuantity(updatedItem.getQuantity());

        if (updatedItem.getProduct() != null) {
            existing.setProduct(updatedItem.getProduct());
        }

        if (updatedItem.getCart() != null) {
            existing.setCart(updatedItem.getCart());
        }

        CartItem saved = cartItemRepository.save(existing);
        return convertToDTO(saved);
    }


    // CONVERTTODTO METDOHU
    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCart().getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
