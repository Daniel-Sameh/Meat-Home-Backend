package com.backend.meat_home.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;

    @NotBlank(message = "price is required")
    private BigDecimal price;

    @NotBlank(message = "stock is required")
    private float stock;

    private Long categoryId;

    private String imageUrl;
}
