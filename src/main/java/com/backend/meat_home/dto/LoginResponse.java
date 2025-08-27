package com.backend.meat_home.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String token;
    private String role;
    private String status;
    private int statusCode;

    public static LoginResponse success(String token, String role, String status) {
        return LoginResponse.builder()
                .message("Login successful")
                .token(token)
                .role(role)
                .status(status)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public static LoginResponse error(String message, HttpStatus httpStatus) {
        return LoginResponse.builder()
                .message(message)
                .statusCode(httpStatus.value())
                .build();
    }
}
