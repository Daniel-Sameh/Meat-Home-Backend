package com.backend.meat_home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private String role;
    private String status;
    private HttpStatus statusCode;
}