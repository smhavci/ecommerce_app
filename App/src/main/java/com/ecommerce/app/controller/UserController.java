package com.ecommerce.app.controller;

import com.ecommerce.app.model.User;
import com.ecommerce.app.service.UserService;
import com.ecommerce.app.dto.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.app.model.Role;
// UserDTO importu
import com.ecommerce.app.dto.UserDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth") // JWT token gerektirir
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired // Bağımlılığı (dependency) otomatik injekte ediyoruz
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE - Kullanıcı Ekleme Endpoint'i (Giden veri User, dönen veri UserDTO)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
        UserDTO savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // READ - Tüm Kullanıcıları Listeleme Endpoint'i (UserDTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newRole = body.get("role");
        userService.updateUserRole(id, newRole);
        return ResponseEntity.ok().build();
    }


    // READ - ID ile Kullanıcı Getirme Endpoint'i (UserDTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + id);
        }


        return ResponseEntity.ok(user);
    }

    // DELETE - Kullanıcı Silme Endpoint'i
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // UPDATE - Kullanıcı Güncelleme işlemi 
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')") // 👈 Satıcılar da erişebilsin
    public ResponseEntity<UserDTO> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody User updatedUser) {

        UserDTO updated = userService.updateUser(id, updatedUser); // storeName burada set ediliyor zaten
        return ResponseEntity.ok(updated);
    }


    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = userService.convertToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('USER')")
    public List<UserDTO> getAllSellers() {
        List<User> sellers = userService.findAllByRole(Role.ROLE_SELLER);
        return sellers.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setStoreName(user.getStoreName());
            dto.setUsername(user.getUsername());
            return dto;
        }).toList();
    }


}
