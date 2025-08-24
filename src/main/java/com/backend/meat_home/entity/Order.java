package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "orders") // علشان كلمة order محجوزة
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;  // هنجيبه من الـ Authentication

    private Long driverId; // ممكن يكون null لحد ما يتعين driver

    private LocalDateTime createdAt;

    private Double totalPrice;

    private String address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
