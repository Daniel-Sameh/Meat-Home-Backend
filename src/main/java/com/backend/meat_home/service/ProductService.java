package com.backend.meat_home.service;

import com.backend.meat_home.entity.Product;
import com.backend.meat_home.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}
