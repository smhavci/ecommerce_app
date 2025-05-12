package com.ecommerce.app.controller;

import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.service.CartItemService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.dto.CartItemDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CartItemDTO> createCartItem(@RequestBody @Valid CartItem cartItem) {
        CartItemDTO savedCartItem = cartItemService.saveCartItem(cartItem);
        return ResponseEntity.ok(savedCartItem);
    }

    // READ - Tüm CartItem'ları getir
    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getAllCartItems() {
        List<CartItemDTO> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    // READ - ID ile CartItem getir
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CartItemDTO>> getCartItemById(@PathVariable Long id) {
        Optional<CartItemDTO> cartItem = cartItemService.getCartItemById(id);
        return ResponseEntity.ok(cartItem);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok("CartItem deleted successfully!");
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long id, @RequestBody CartItem updatedItem) {
        CartItemDTO updated = cartItemService.updateCartItem(id, updatedItem);
        return ResponseEntity.ok(updated);
    }

}
