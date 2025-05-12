package com.ecommerce.app.controller;

import com.ecommerce.app.dto.LoginRequest;
import com.ecommerce.app.dto.LoginResponse;
import com.ecommerce.app.dto.RegisterRequest;
import com.ecommerce.app.dto.UserDTO;
import com.ecommerce.app.model.Role;
import com.ecommerce.app.model.User;
import com.ecommerce.app.security.JwtUtil;
import com.ecommerce.app.service.UserDetailsServiceImpl;
import com.ecommerce.app.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    // 👤 Kullanıcı giriş yapar
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwtToken = jwtUtil.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(jwtToken));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Kullanıcı adı veya şifre hatalı");
        }
    }

    // 👤 Kullanıcı kayıt olur
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());

        // ✅ Mağaza ismini de set et
        newUser.setStoreName(request.getStoreName());
        newUser.setBalance(BigDecimal.valueOf(75000));

        UserDTO savedUser = userService.saveUser(newUser);
        return ResponseEntity.ok(savedUser);
    }
}
