package com.example.bookingSystem.controller;

import com.example.bookingSystem.dto.request.CreateEventRequest;
import com.example.bookingSystem.dto.response.EventResponse;
import com.example.bookingSystem.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        List<EventResponse> events;
        if (q != null || category != null || dateFrom != null || dateTo != null) {
            events = eventService.searchEvents(q, category, dateFrom, dateTo);
        } else {
            events = eventService.getAllEvents();
        }
        List<String> categories = eventService.getCategories();
        return ResponseEntity.ok(Map.of("events", events, "categories", categories));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponse>> getAllEventsAdmin() {
        return ResponseEntity.ok(eventService.getAllEventsAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventResponseById(id));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<EventResponse>> getFeaturedEvents() {
        return ResponseEntity.ok(eventService.getFeaturedEvents());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(eventService.getCategories());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.Map<String, Long>> getEventStats() {
        return ResponseEntity.ok(java.util.Map.of(
                "totalEvents", eventService.getEventsCount(),
                "upcomingEvents", eventService.getUpcomingEventsCount()
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(Map.of("event", eventService.createEvent(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable Long id, @Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(Map.of("event", eventService.updateEvent(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
