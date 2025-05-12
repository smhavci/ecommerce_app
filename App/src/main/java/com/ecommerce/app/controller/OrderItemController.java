package com.ecommerce.app.controller;

import com.ecommerce.app.model.OrderItem;
import com.ecommerce.app.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.dto.OrderItemDTO;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // CREATE - OrderItem oluştur (DTO)
    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItem orderItem) {
        OrderItemDTO savedOrderItem = orderItemService.saveOrderItem(orderItem);
        return ResponseEntity.ok(savedOrderItem);
    }

    // READ - Tüm OrderItem'ları listele (DTO)
    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> orderItems = orderItemService.getAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    // READ - ID'ye göre OrderItem getir (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<Optional<OrderItemDTO>> getOrderItemById(@PathVariable Long id) {
        Optional<OrderItemDTO> orderItem = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(orderItem);
    }

    // DELETE - OrderItem sil
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("OrderItem deleted successfully!");
    }

    // UPDATE - OrderItem güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem updatedItem) {
        OrderItemDTO updated = orderItemService.updateOrderItem(id, updatedItem);
        return ResponseEntity.ok(updated);
    }

}
