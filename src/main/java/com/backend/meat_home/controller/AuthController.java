package com.backend.meat_home.controller;

import com.backend.meat_home.dto.*;
// ... existing code ...
import com.backend.meat_home.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        PasswordResetResponse response = authService.resetPassword(userEmail, request);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

}