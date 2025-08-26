package com.backend.meat_home.repository;

import com.backend.meat_home.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerId(Long customerId);
    Optional<Order> findByOrderIdAndCustomerId(Long orderId, Long customerId);

}