package com.example.bookingSystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String venue;

    private String location;

    @Column(nullable = false)
    private LocalDate date;

    private String time;

    @Column(nullable = false)
    private Integer priceCents;

    @Column(nullable = false)
    private Integer totalTickets;

    @Column(nullable = false)
    private Integer availableTickets;

    private String imageUrl;

    private String category;

    @ElementCollection
    @CollectionTable(name = "event_included_items", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "item")
    private List<String> includedItems;

    private String cancellationPolicy;

    private String refundPolicy;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
