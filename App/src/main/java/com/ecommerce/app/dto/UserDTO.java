package com.ecommerce.app.dto;

import com.ecommerce.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    private String storeName;
    
    private BigDecimal balance;
    
}
