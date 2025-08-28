package com.backend.meat_home.service;

import com.backend.meat_home.entity.Category;
import com.backend.meat_home.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Create Category
    public Category createCategory(Category dto) {
        Category category = new Category();
        category.setName(dto.getName());
        Category saved = categoryRepository.save(category);
        return new Category(saved.getId(), saved.getName());
    }

    // Update Category
    public Category updateCategory(Long id, Map<String, Object> updates) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (updates.containsKey("name")) {
            category.setName((String) updates.get("name"));
        }

        Category updated = categoryRepository.save(category);
        return new Category(updated.getId(), updated.getName());
    }

    // Delete Category
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    //Get all Categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new Category(c.getId(), c.getName()))
                .toList();
    }

    // Get Category by ID
    public Category getCategoryById(Long id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
            
    return new Category(category.getId(), category.getName());
    }
}




