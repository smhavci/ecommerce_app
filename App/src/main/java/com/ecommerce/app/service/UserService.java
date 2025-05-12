package com.ecommerce.app.service;

import com.ecommerce.app.dto.UserDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Role;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Constructor Injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE - Kullanıcı Kaydetme İşlemi (UserDTO)
    public UserDTO saveUser(User user) {
    // ✅ Eğer kullanıcı balance göndermediyse varsayılan 75.000 olarak ayarla
    if (user.getBalance() == null) {
        user.setBalance(BigDecimal.valueOf(75000));
    }

    User savedUser = userRepository.save(user);
    return convertToDTO(savedUser);
}
    

    // READ - Tüm Kullanıcıları Getirme İşlemi (UserDTO)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    

    // READ - ID İle Kullanıcı Getirtme İşlemi (UserDTO)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    

    // DELETE - Kullanıcı Silme İşlemi (Sadece silme işlemi yapıp bir şey döndürmediğinden dolayı UserDTO yapmaya gerek yoktur!!!)
    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
    }

    // UPDATE - KULLANICI GÜNCELLEME
    public UserDTO updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));

        // 🔧 Kullanıcı kendi bilgilerini güncelliyorsa sadece izinli alanlar güncellenir
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        
        // 🔐 Sadece ADMIN rolü güncelleyebilir, burada bilinçli olarak role güncellemesi yapılmıyor
        // existing.setRole(updatedUser.getRole()); 

        // 🏪 Mağaza ismi güncellenebilir
        existing.setStoreName(updatedUser.getStoreName());

        // 🔑 Şifre güncelleniyorsa encode et
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        User saved = userRepository.save(existing);
        return convertToDTO(saved);
    }
    
    // HER METHODDA KOD TEKRARINDAN KAÇMAK İÇİN CONVERTTODTO KURULDU.
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.getStoreName(), // ✅ storeName artık burada
            user.getBalance()
        );
    }
    
    

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void updateUserRole(Long id, String newRole) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        try {
            Role roleEnum = Role.valueOf(newRole); // String → Enum
            user.setRole(roleEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Geçersiz rol adı: " + newRole);
        }

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
    }
    
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı"));
    }

}   
