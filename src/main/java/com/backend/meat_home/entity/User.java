package com.backend.meat_home.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Status status;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    public enum Role {
        ADMIN,
        CALL_CENTER_AGENT,
        CUSTOMER,
        DRIVER
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    // Temporary test main method
    public static void main(String[] args) {
        User testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .password("test_password")
                .phoneNumber("0123456789")
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .username("test_user")
                .build();

        System.out.println("User created: " + testUser);
        System.out.println("Role: " + testUser.getRole());
        System.out.println("Status: " + testUser.getStatus());


        assert testUser.getId() == 1L;
        assert testUser.getRole() == Role.ADMIN;
        assert testUser.getStatus() == Status.ACTIVE;

    }
}
