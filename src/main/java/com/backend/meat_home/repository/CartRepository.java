package com.backend.meat_home.repository;

import com.backend.meat_home.entity.Cart;
import com.backend.meat_home.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(User customerId);
    Optional<Cart> findByCustomerId_Id(Long customerId);

    Optional<Cart> findById(Long id);

    void deleteById(Long id);


}
