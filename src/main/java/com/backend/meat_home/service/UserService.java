package com.backend.meat_home.service;

import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Find by Email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find by Username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Save User
    public User save(User user) {
        return userRepository.save(user);
    }

    // Exists by Email
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Exists by Username
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
