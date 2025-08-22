package com.backend.meat_home.repository;

import com.backend.meat_home.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
