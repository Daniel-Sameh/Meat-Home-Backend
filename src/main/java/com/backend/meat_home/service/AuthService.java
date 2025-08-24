package com.backend.meat_home.service;

import com.backend.meat_home.dto.LoginRequest;
import com.backend.meat_home.dto.LoginResponse;
import com.backend.meat_home.dto.RegisterRequest;
import com.backend.meat_home.dto.RegisterResponse;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Validate duplicates
        if (userService.existsByEmail(request.getEmail())) {
            return buildResponse("Email is already in use!", null, null, HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByUsername(request.getUsername())) {
            return buildResponse("Username is already taken!", null, null, HttpStatus.BAD_REQUEST);
        }
        User.Status status = initiateStatus(request.getRole());
        String message = createMessage(status);

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(status)
                .build();
        userService.save(user);

        return buildResponse(
                message,
                user.getRole().name(),
                user.getStatus().name(),
                HttpStatus.CREATED
        );
    }

    public LoginResponse login(LoginRequest request) {
        // Find user by username
        Optional<User> userOptional = userService.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            return LoginResponse.error("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.error("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }


        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return LoginResponse.success(token, user.getRole().name(), user.getStatus().name());
    }


    private User.Status initiateStatus(User.Role role) {
        return role == User.Role.CUSTOMER ? User.Status.ACTIVE : User.Status.INACTIVE;
    }

    private String createMessage(User.Status status) {
        return status == User.Status.ACTIVE
                ? "Registration successful. Your account is active."
                : "Registration successful. Your account is inactive until approved by the admin.";
    }

    private RegisterResponse buildResponse(String message, String role, String status, HttpStatus statusCode) {
        return new RegisterResponse(message, role, status, statusCode);
    }
}
