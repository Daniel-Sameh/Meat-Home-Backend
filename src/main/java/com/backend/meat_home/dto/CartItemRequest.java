package com.backend.meat_home.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemRequest {

    private Long customerId;

    private Long productId;

    @NotBlank(message = "Quantity is required")
    private float quantity;

}
