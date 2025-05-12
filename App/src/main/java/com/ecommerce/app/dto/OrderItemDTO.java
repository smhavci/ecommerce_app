package com.ecommerce.app.dto;

import com.ecommerce.app.model.OrderItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemDTO {
    private String productName;
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double price;

    public OrderItemDTO(OrderItem item) {
    this.productName = item.getProduct().getName();
    this.quantity = item.getQuantity();
    this.price = item.getPrice();
}
}
