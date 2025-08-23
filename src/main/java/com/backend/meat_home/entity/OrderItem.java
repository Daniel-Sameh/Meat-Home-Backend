package com.backend.meat_home.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore  
    private Order order;

    private Long productId;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

    public Long getOrderId() {
    return order != null ? order.getOrderId() : null;
}
}
