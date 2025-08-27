package com.backend.meat_home.dto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class RateRequestDTO {
    private int rate;
    private String review;
}