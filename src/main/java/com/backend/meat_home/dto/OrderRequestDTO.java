package com.backend.meat_home.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrderRequestDTO {
    private String address;
    private List<OrderItemDTO> items;
}
