package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "enquiries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean unread;

    @Column(nullable = false)
    private boolean hidden = false;
}
