package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "orders") 
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;  

    private Long driverId; 

    private LocalDateTime createdAt;

    private BigDecimal totalPrice;

    private String address;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
