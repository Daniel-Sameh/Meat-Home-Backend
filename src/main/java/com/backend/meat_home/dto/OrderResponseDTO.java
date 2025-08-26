package com.backend.meat_home.dto;

import com.backend.meat_home.entity.OrderItem;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private LocalDateTime createdAt;
    private double totalPrice;
    private List<OrderItem> items;
}
