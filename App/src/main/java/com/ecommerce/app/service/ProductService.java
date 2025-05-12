package com.ecommerce.app.service;

import com.ecommerce.app.dto.ProductDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Category;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private CategoryService categoryService;
    private final ProductRepository productRepository;

    // Constructor Injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE - Ürün Kaydetme İşlemi (ProductDTO)
    public ProductDTO saveProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category fullCategory = categoryService.getCategoryById(product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı"));
            product.setCategory(fullCategory);
        }
    
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    // READ - Tüm Ürünleri Listeleme İşlemi (ProductDTO)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // READ - ID ile Ürün Getirme İşlemi (ProductDTO)
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE - Ürün Silme İşlemi
    public void deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen ürün bulunamadı: " + id);
        }
        productRepository.deleteById(id);
    }

    // UPDATE - Ürün Güncelleme İşlemi
    public ProductDTO updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + id));
    
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());
    
        // Kategori güncellemesi
        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
            Category fullCategory = categoryService.getCategoryById(updatedProduct.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı"));
            existing.setCategory(fullCategory);
        }
    
        Product saved = productRepository.save(existing);
        return convertToDTO(saved);
    }

    // KOD TEKRARINDAN KAÇINMAK İÇİN CONVERTTODTO METHODU KURULDU!!!
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());
    
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory()); // ✅ Burada doğrudan Category nesnesi atanmalı
        }
    
        return dto;
    }



    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı, ID: " + id));
    }
    
    public List<Product> getProductsBySeller(User seller) {
        return productRepository.findBySeller(seller);
    }

    public List<Product> getProductsByUser(User user) {
        return productRepository.findByUser(user);
    }
    
}