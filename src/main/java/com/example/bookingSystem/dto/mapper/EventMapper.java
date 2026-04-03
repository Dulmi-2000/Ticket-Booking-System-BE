package com.example.bookingSystem.dto.mapper;

import com.example.bookingSystem.dto.request.CreateEventRequest;
import com.example.bookingSystem.dto.response.EventResponse;
import com.example.bookingSystem.model.entity.Event;

import java.time.LocalDate;
import java.util.List;

public final class EventMapper {
    private static final List<String> DEFAULT_INCLUDED_ITEMS = List.of(
            "General venue access",
            "Live entertainment",
            "Event program booklet",
            "Access to all stages"
    );

    private static final String DEFAULT_CANCELLATION_POLICY = "Free cancellation up to 24h before the event.";
    private static final String DEFAULT_REFUND_POLICY = "Easy refund if the event is cancelled.";

    private EventMapper() {
    }

    public static Event toEntity(CreateEventRequest request) {
        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .venue(request.getVenue())
                .location(request.getLocation())
                .date(LocalDate.parse(request.getDate()))
                .time(request.getTime())
                .priceCents(request.getPriceCents())
                .totalTickets(request.getTotalTickets())
                .availableTickets(request.getTotalTickets())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .includedItems(request.getIncludedItems() == null ? DEFAULT_INCLUDED_ITEMS : request.getIncludedItems())
                .cancellationPolicy(request.getCancellationPolicy() == null || request.getCancellationPolicy().isBlank()
                        ? DEFAULT_CANCELLATION_POLICY
                        : request.getCancellationPolicy())
                .refundPolicy(request.getRefundPolicy() == null || request.getRefundPolicy().isBlank()
                        ? DEFAULT_REFUND_POLICY
                        : request.getRefundPolicy())
                .isFeatured(request.getIsFeatured() != null && request.getIsFeatured())
                .build();
    }

    public static EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .venue(event.getVenue())
                .location(event.getLocation())
                .date(event.getDate())
                .time(event.getTime())
                .priceCents(event.getPriceCents())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .imageUrl(event.getImageUrl())
                .category(event.getCategory())
                .includedItems(event.getIncludedItems() == null || event.getIncludedItems().isEmpty()
                        ? DEFAULT_INCLUDED_ITEMS
                        : event.getIncludedItems())
                .cancellationPolicy(event.getCancellationPolicy() == null || event.getCancellationPolicy().isBlank()
                        ? DEFAULT_CANCELLATION_POLICY
                        : event.getCancellationPolicy())
                .refundPolicy(event.getRefundPolicy() == null || event.getRefundPolicy().isBlank()
                        ? DEFAULT_REFUND_POLICY
                        : event.getRefundPolicy())
                .isFeatured(event.getIsFeatured())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
