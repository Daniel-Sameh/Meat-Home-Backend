package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platformName;
    private String logoUrl;

    private String facebookUrl;
    private String whatsappNumber;
    private String phoneNumber;
    private String secondPhoneNumber;

    private String aboutImageUrl;

    @Column(columnDefinition = "TEXT")
    private String aboutDescription;

    @Column(columnDefinition = "TEXT")
    private String termsConditions;
}
