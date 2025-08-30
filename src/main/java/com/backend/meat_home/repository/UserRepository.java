package com.backend.meat_home.repository;

import com.backend.meat_home.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(User.Role role);
    Optional<User> findByUsername(String username);
    Optional<User> getUserByEmailOrUsername(String email, String username);
    Optional<User> findById(Long id);


}
