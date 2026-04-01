package com.example.bookingSystem.dto.mapper;

import com.example.bookingSystem.dto.request.CreateEventRequest;
import com.example.bookingSystem.dto.response.EventResponse;
import com.example.bookingSystem.model.entity.Event;

import java.time.LocalDate;

public final class EventMapper {
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
                .isFeatured(event.getIsFeatured())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
