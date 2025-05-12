package com.ecommerce.app.repository;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(User seller); // 🔍 Sadece satıcının ürünlerini getir

    List<Product> findByUser(User user);

}
