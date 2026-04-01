package com.example.bookingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private String eventVenue;
    private String eventLocation;
    private Integer quantity;
    private Integer totalPriceCents;
    private String status;
    private String stripePaymentIntentId;
    private LocalDateTime bookedAt;
    private LocalDateTime cancelledAt;
    private EventResponse event;
}
