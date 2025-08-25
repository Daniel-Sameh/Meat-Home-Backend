package com.backend.meat_home.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemRequest {
    private Long customerId;
    private Long productId;
    private float quantity;
}
