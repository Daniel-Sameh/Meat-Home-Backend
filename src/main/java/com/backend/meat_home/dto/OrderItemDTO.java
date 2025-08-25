package com.backend.meat_home.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
}
