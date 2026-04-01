package com.example.bookingSystem.service;

import com.example.bookingSystem.dto.mapper.EventMapper;
import com.example.bookingSystem.dto.request.BookTicketsRequest;
import com.example.bookingSystem.dto.response.BookingResponse;
import com.example.bookingSystem.dto.response.EventResponse;
import com.example.bookingSystem.exception.InsufficientSeatsException;
import com.example.bookingSystem.exception.ResourceNotFoundException;
import com.example.bookingSystem.model.entity.Booking;
import com.example.bookingSystem.model.entity.Event;
import com.example.bookingSystem.model.entity.User;
import com.example.bookingSystem.repository.BookingRepository;
import com.example.bookingSystem.repository.EventRepository;
import com.example.bookingSystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            EventRepository eventRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public BookingResponse bookTickets(Long userId, BookTicketsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (event.getAvailableTickets() < request.getQuantity()) {
            throw new InsufficientSeatsException("Not enough tickets available");
        }

        event.setAvailableTickets(event.getAvailableTickets() - request.getQuantity());
        eventRepository.save(event);

        int totalPriceCents = event.getPriceCents() * request.getQuantity();
        Booking booking = Booking.builder()
                .user(user)
                .event(event)
                .customerName(user.getFullName())
                .customerEmail(user.getEmail())
                .numberOfTickets(request.getQuantity())
                .quantity(request.getQuantity())
                .totalPriceCents(totalPriceCents)
                .status("confirmed")
                .bookedAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByBookedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!"confirmed".equals(booking.getStatus())) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setStatus("cancelled");
        booking.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Return tickets
        Event event = booking.getEvent();
        event.setAvailableTickets(event.getAvailableTickets() + booking.getQuantity());
        eventRepository.save(event);

        return toResponse(booking);
    }

    public Map<String, Object> getStats() {
        long totalBookings = bookingRepository.count();
        long confirmedBookings = bookingRepository.countByStatus("confirmed");
        long totalRevenue = bookingRepository.sumConfirmedRevenue();
        return Map.of(
                "totalBookings", totalBookings,
                "confirmedBookings", confirmedBookings,
                "totalRevenue", totalRevenue
        );
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getRecentBookings() {
        return bookingRepository.findTop10ByOrderByBookedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        EventResponse eventResponse = EventMapper.toResponse(booking.getEvent());
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .eventId(booking.getEvent().getId())
                .eventTitle(booking.getEvent().getTitle())
                .eventVenue(booking.getEvent().getVenue())
                .eventLocation(booking.getEvent().getLocation())
                .quantity(booking.getQuantity())
                .totalPriceCents(booking.getTotalPriceCents())
                .status(booking.getStatus())
                .customerName(booking.getCustomerName())
                .customerEmail(booking.getCustomerEmail())
                .numberOfTickets(booking.getNumberOfTickets())
                .stripePaymentIntentId(booking.getStripePaymentIntentId())
                .bookedAt(booking.getBookedAt())
                .cancelledAt(booking.getCancelledAt())
                .event(eventResponse)
                .build();
    }
}
