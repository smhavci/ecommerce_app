package com.ecommerce.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private Long paymentId;
    private String fullName;
    private String address;
    private String phone;
    private List<OrderItemRequest> items;
}