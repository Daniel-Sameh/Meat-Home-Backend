package com.backend.meat_home.repository;

import com.backend.meat_home.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);
    Optional<Product> findByCategory(String category);
    Optional<Product> findByPrice(Double price);

}
