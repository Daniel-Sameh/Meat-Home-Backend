package com.backend.meat_home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class PasswordResetResponse {
    private String message;
    private HttpStatus statusCode;

    public static PasswordResetResponse success(String message) {
        return new PasswordResetResponse(message, HttpStatus.OK);
    }

    public static PasswordResetResponse error(String message, HttpStatus status) {
        return new PasswordResetResponse(message, status);
    }
}