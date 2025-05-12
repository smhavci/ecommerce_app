package com.ecommerce.app.controller;

import com.ecommerce.app.model.Order;
import com.ecommerce.app.model.User;
import com.ecommerce.app.service.OrderService;
import com.ecommerce.app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.dto.OrderDTO;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    @Autowired // (Yorum: Tek constructor varsa @Autowired yazmasan da olur ama yazınca hiçbir şey bozulmaz.)
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // READ - Tüm siparişleri listele (OrderDTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Giriş yapan kullanıcının kendi siparişleri
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<OrderDTO> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    // READ - ID ile sipariş getir (OrderDTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<OrderDTO>> getOrderById(@PathVariable Long id) {
        Optional<OrderDTO> order = orderService.getOrderById(id);

        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Sipariş bulunamadı: " + id);
        }

        return ResponseEntity.ok(order);
    }

    // CREATE - Sipariş ekle (OrderDTO)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        OrderDTO savedOrder = orderService.saveOrder(request, user);
        return ResponseEntity.ok(savedOrder);
    }
    

    // DELETE - Sipariş sil
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }

    // UPDATE - Sipariş Güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        OrderDTO updated = orderService.updateOrder(id, updatedOrder);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }


    @PatchMapping("/orders/{id}/cancel")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        orderService.cancelOrderBySeller(id, currentUser);
        return ResponseEntity.ok("Sipariş iptal edildi ve para iadesi yapıldı.");
    }

    // OrderController.java
    @GetMapping("/orders/seller/orders")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public List<OrderDTO> getSellerOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<Order> orders = orderService.getOrdersBySeller(currentUser); // Satıcının siparişlerini çek
        return orders.stream().map(order -> new OrderDTO(order)).collect(Collectors.toList());
    }

}
