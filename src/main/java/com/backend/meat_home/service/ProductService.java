package com.backend.meat_home.service;

import com.backend.meat_home.dto.ProductFilterRequest;
import com.backend.meat_home.dto.ProductRequest;
import com.backend.meat_home.dto.ProductUpdateRequest;
import com.backend.meat_home.entity.Product;
import com.backend.meat_home.entity.ProductStock;
import com.backend.meat_home.repository.ProductRepository;
import com.backend.meat_home.repository.ProductStockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    // Get Product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    public Product createProduct(ProductRequest productRequest) {
        // Create new product
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());

        // Set category if provided
        if (productRequest.getCategoryId() != null) {
            product.setCategory(categoryService.getCategoryById(productRequest.getCategoryId()));
        }

        // Save product first
        Product savedProduct = productRepository.save(product);

        // Create and save product stock
        ProductStock productStock = new ProductStock();
        productStock.setStock(productRequest.getStock());
        productStock.setProduct(savedProduct);
        productStockRepository.save(productStock);

        return savedProduct;
    }

    public Page<Product> getProductWithFilters(ProductFilterRequest filterRequest){
        Sort sort = Sort.by(
                filterRequest.getSortDirection().equalsIgnoreCase("ASC") ?
                        Sort.Direction.ASC : Sort.Direction.DESC,
                filterRequest.getSortBy()
        );

        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                sort
        );

        return productRepository.findWithFilters(
                filterRequest.getSearchTerm(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getCategoryIds(),
                pageable
        );
    }


    public Product updateProduct(Long id, ProductUpdateRequest productUpdateRequest){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        // Updating new fields
        if (productUpdateRequest.getName() != null) {
            product.setName(productUpdateRequest.getName());
        }
        if (productUpdateRequest.getDescription() != null) {
            product.setDescription(productUpdateRequest.getDescription());
        }
        if (productUpdateRequest.getImageUrl() != null) {
            product.setImageUrl(productUpdateRequest.getImageUrl());
        }
        if (productUpdateRequest.getPrice() != null) {
            product.setPrice(new BigDecimal(productUpdateRequest.getPrice()));
        }
        if (productUpdateRequest.getCategoryId() != null) {
            product.setCategory(categoryService.getCategoryById(productUpdateRequest.getCategoryId()));
        }

        if (productUpdateRequest.getStock() > 0) {
            ProductStock productStock = productStockRepository.findByProductId(id)
                    .orElse(new ProductStock());
            productStock.setProduct(product);
            productStock.setStock(productUpdateRequest.getStock());
            productStockRepository.save(productStock);
        }

        return productRepository.save(product);
    }


    @Transactional
    public void deleteProduct(Long id){
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }

        // Delete associated product stock first
        productStockRepository.deleteByProductId(id);

        // Then delete the product
        productRepository.deleteById(id);
    }





}
