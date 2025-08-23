package com.backend.meat_home.repository;

import com.backend.meat_home.entity.OrderRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRateRepository extends JpaRepository<OrderRate, Long> {
    Optional<OrderRate> findByOrderOrderId(Long orderId);
}