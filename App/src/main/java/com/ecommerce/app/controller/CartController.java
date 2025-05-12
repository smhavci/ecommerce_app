package com.ecommerce.app.controller;

import com.ecommerce.app.model.Cart;
import com.ecommerce.app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // CREATE
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody Cart cart) {
        CartDTO savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }

    // READ - Tüm sepetleri getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    // READ - ID ile sepet getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CartDTO>> getCartById(@PathVariable Long id) {
        Optional<CartDTO> cart = cartService.getCartById(id);

        if (cart.isEmpty()) {
            throw new ResourceNotFoundException("Sepet bulunamadı: " + id);
        }

        return ResponseEntity.ok(cart);
    }

    // DELETE
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok("Cart deleted successfully!");
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long id, @RequestBody Cart updatedCart) {
        CartDTO updated = cartService.updateCart(id, updatedCart);
        return ResponseEntity.ok(updated);
    }

}
