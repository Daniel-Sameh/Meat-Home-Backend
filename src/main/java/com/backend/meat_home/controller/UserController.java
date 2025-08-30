package com.backend.meat_home.controller;

import com.backend.meat_home.entity.User;
import com.backend.meat_home.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create User
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User By ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get User By Role
    @GetMapping("/role")
    public ResponseEntity<List<User>> getUserByRole(@RequestParam User.Role role) {
        return ResponseEntity.ok (userService.getUserByRole(role));
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Activate User
    @PutMapping("/activate/{id}")
    public ResponseEntity<User> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }

    // Deactivate User
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    // Update Profile (Partial Update)
//    @PatchMapping("/me")
//    public ResponseEntity<User> updateUserPartially(@RequestBody Map<String, Object> updates) {
//        return ResponseEntity.ok(userService.updateUserPartially(updates));
//    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateMyProfile(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.updateUserPartially(updates));
    }

}
