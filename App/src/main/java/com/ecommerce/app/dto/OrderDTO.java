package com.ecommerce.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.app.enums.OrderStatus;
import com.ecommerce.app.enums.ShippingStatus;
import com.ecommerce.app.model.Order;

@NoArgsConstructor
@Data
public class OrderDTO {
    private Long id;
    private Long userId;         // User nesnesi yerine sadece userId

    private Long paymentId;      // Payment nesnesi yerine sadece paymentId
    private UserDTO user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private ShippingStatus shippingStatus;
    private Double totalAmount;
    
    private List<OrderItemDTO> items;


    public OrderDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.orderDate = order.getOrderDate();
        this.userId = order.getUser().getId(); // isteğe bağlı
        // Dilersen item'ları da mapleyebilirsin:
        this.items = order.getOrderItems().stream()
            .map(OrderItemDTO::new)
            .collect(Collectors.toList());
    }

}
