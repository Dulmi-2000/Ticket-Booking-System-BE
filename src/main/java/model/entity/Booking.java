package com.example.bookingSystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer totalPriceCents;

    @Column(nullable = false)
    @Builder.Default
    private String status = "pending";

    private String stripePaymentIntentId;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime bookedAt = LocalDateTime.now();

    private LocalDateTime cancelledAt;

    @PrePersist
    protected void onCreate() {
        if (bookedAt == null) bookedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }
}
