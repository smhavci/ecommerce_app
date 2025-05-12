package com.ecommerce.app.service;

import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Category;
import com.ecommerce.app.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE - Yeni kategori kaydet
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // READ - Tüm kategorileri getir
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // READ - ID ile kategori getir
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // DELETE - Kategori sil
    public void deleteCategory(Long id) {
        boolean exists = categoryRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen kategori bulunamadı: " + id);
        }

        categoryRepository.deleteById(id);
    }

    // UPDATE - Kategoriyi güncelle
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + id));
    
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
    
        return categoryRepository.save(existing);
    }
}
