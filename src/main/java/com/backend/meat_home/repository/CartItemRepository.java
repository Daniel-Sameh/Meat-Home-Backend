package com.backend.meat_home.repository;

import com.backend.meat_home.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findAllByCartId(Long cartId);
    void deleteByCartIdAndProductId(Long cartId, Long productId);
    void deleteAllByCartId(Long cartId);
}
