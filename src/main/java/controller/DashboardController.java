package com.example.bookingSystem.controller;

import com.example.bookingSystem.repository.BookingRepository;
import com.example.bookingSystem.repository.EventRepository;
import com.example.bookingSystem.repository.UserRepository;
import com.example.bookingSystem.service.BookingService;
import com.example.bookingSystem.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {
    private final UserRepository userRepository;
    private final EventService eventService;
    private final BookingService bookingService;

    public DashboardController(
            UserRepository userRepository,
            EventService eventService,
            BookingService bookingService
    ) {
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.bookingService = bookingService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> bookingStats = bookingService.getStats();
        return ResponseEntity.ok(Map.of(
                "users", userRepository.count(),
                "events", eventService.getEventsCount(),
                "upcomingEvents", eventService.getUpcomingEventsCount(),
                "totalBookings", bookingStats.get("totalBookings"),
                "confirmedBookings", bookingStats.get("confirmedBookings"),
                "totalRevenue", bookingStats.get("totalRevenue")
        ));
    }
}
