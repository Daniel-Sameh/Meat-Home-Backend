package com.backend.meat_home.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFilterRequest {
    private String searchTerm;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> categoryIds;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}