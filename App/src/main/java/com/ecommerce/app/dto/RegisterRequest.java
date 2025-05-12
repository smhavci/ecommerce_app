package com.ecommerce.app.dto;

import com.ecommerce.app.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "Email boş olamaz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    private String storeName;


    @NotNull(message = "Rol seçilmelidir (örneğin: ROLE_USER)")
    private Role role = Role.ROLE_USER; // varsayılan user

}
