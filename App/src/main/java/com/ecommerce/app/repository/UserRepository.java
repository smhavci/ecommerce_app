package com.ecommerce.app.repository;

import com.ecommerce.app.model.Role;
import com.ecommerce.app.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 🔍 Kullanıcıyı username ile bul (login işlemi için şart!)
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);

}
