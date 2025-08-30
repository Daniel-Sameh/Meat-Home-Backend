package com.backend.meat_home.controller;

import com.backend.meat_home.dto.ProductFilterRequest;
import com.backend.meat_home.dto.ProductRequest;
import com.backend.meat_home.dto.ProductUpdateRequest;
import com.backend.meat_home.entity.Product;
import com.backend.meat_home.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(201).body(productService.createProduct(productRequest));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam(required = false) String searchTerm,
                                                        @RequestParam(required = false) BigDecimal minPrice,
                                                        @RequestParam(required = false) BigDecimal maxPrice,
                                                        @RequestParam(required = false) List<Long> categoryIds,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "ASC") String sortDirection) {
        ProductFilterRequest filterRequest = new ProductFilterRequest();
        filterRequest.setSearchTerm(searchTerm);
        filterRequest.setMinPrice(minPrice);
        filterRequest.setMaxPrice(maxPrice);
        filterRequest.setCategoryIds(categoryIds);
        filterRequest.setPage(page);
        filterRequest.setSize(size);
        filterRequest.setSortBy(sortBy);
        filterRequest.setSortDirection(sortDirection);

        return ResponseEntity.ok(productService.getProductWithFilters(filterRequest));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest updateRequest) {
        return ResponseEntity.ok(productService.updateProduct(productId, updateRequest));
    }



}
