package com.backend.meat_home.service;

import com.backend.meat_home.dto.*;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.UserRepository;
import com.backend.meat_home.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Sign up
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
        userRepository.save(user);

        return buildResponse(
                message,
                user.getRole().name(),
                user.getStatus().name(),
                HttpStatus.CREATED
        );
    }

    // Login
    public LoginResponse login(LoginRequest request) {
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

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

    //Reset Password
    public PasswordResetResponse resetPassword(String userEmail, PasswordResetRequest request) {
        // Find user by email from JWT token
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return PasswordResetResponse.error("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return PasswordResetResponse.error("Invalid old password", HttpStatus.BAD_REQUEST);
        }

        // Ensure new password is different from old password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return PasswordResetResponse.error("New password must be different from the current password", HttpStatus.BAD_REQUEST);
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return PasswordResetResponse.success("Password reset successfully");
    }

    // User Status
    private User.Status initiateStatus(User.Role role) {
        return role == User.Role.CUSTOMER ? User.Status.ACTIVE : User.Status.INACTIVE;
    }

    // Create Message
    private String createMessage(User.Status status) {
        return status == User.Status.ACTIVE
                ? "Registration successful. Your account is active."
                : "Registration successful. Your account is inactive until approved by the admin.";
    }

    // Build Response
    private RegisterResponse buildResponse(String message, String role, String status, HttpStatus statusCode) {
        return new RegisterResponse(message, role, status, statusCode);
    }
}
