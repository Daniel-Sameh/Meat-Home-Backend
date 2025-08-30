package com.backend.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Order.Status status;
    public enum Status {
        PENDING,
        CONFIRMED,
        IN_PREPARATION,
        READY,
        ASSIGNED,
        ON_WAY,
        DELIVERED,
        CANCELLED
    }
    private LocalDateTime time;
}
