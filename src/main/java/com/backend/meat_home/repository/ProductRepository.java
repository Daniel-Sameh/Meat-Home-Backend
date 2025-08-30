package com.backend.meat_home.repository;

import com.backend.meat_home.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Optional<List<Product>> findAllByCategory_Id(Long categoryId);
    Optional<List<Product>> findAllByPriceLessThanEqual(BigDecimal price);


    @Query("SELECT p FROM Product p WHERE " +
            "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:categoryIds IS NULL OR p.category.id IN :categoryIds)")
    Page<Product> findWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );

}
