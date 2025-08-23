package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "order_rate", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"order_id"})
})
public class OrderRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    @JsonIgnore  
    private Order order;

    private int rate; 

    @Column(length = 2000)
    private String review;

    private boolean visible = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}