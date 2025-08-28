package com.backend.meat_home.controller;

import com.backend.meat_home.entity.Category;
import com.backend.meat_home.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Create
    @PostMapping("/create")
    public Category create(@RequestBody Category dto) {
        return categoryService.createCategory(dto);
    }

    // Update
    @PatchMapping("/update/{id}")
    public Category update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return categoryService.updateCategory(id, updates);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
    
    // Get All
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    // Get by ID
    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}

