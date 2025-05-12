package com.ecommerce.app.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
}
