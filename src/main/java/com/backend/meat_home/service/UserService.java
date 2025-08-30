package com.backend.meat_home.service;

import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Exists by Email
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Exists by Username
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Create User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User By ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get User By Role
    public List<User> getUserByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    // Delete User
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.deleteById(id);
    }

    // Activate User
    public User activateUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(User.Status.ACTIVE);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Deactivate User
    public User deactivateUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(User.Status.INACTIVE);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update Profile
    public User updateUserPartially(Map<String, Object> updates) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    String newEmail = (String) value;
                    userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(user.getId())) {
                            throw new IllegalArgumentException("Email already in use by another user");
                        }
                    });
                    user.setEmail(newEmail);
                    break;
                case "username":
                    String newUsername = (String) value;
                    userRepository.findByUsername(newUsername).ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(user.getId())) {
                            throw new IllegalArgumentException("Username already in use by another user");
                        }
                    });
                    user.setUsername(newUsername);
                    break;
                case "phoneNumber":
                    user.setPhoneNumber((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        return userRepository.save(user);
    }

}

