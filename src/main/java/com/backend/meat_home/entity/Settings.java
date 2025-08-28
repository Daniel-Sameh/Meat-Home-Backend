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

    private String platformName = "Meat-Home";
    private String logoUrl = "https://example.com/logo.png";

    private String facebookUrl = "https://facebook.com/Meat-Home";
    private String whatsappNumber = "+201234567890";
    private String phoneNumber = "+20111222333";
    private String secondPhoneNumber = "+20112233444";

    private String aboutImageUrl = "https://example.com/about.png";

    @Column(columnDefinition = "TEXT")
    private String aboutDescription = "Welcome to Meat Home! This is the about description.";

    @Column(columnDefinition = "TEXT")
    private String termsConditions = "These are the default terms and conditions.";

}
