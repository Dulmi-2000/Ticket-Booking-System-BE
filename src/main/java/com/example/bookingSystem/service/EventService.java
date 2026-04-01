package com.example.bookingSystem.service;

import com.example.bookingSystem.dto.mapper.EventMapper;
import com.example.bookingSystem.dto.request.CreateEventRequest;
import com.example.bookingSystem.dto.response.EventResponse;
import com.example.bookingSystem.exception.ResourceNotFoundException;
import com.example.bookingSystem.model.entity.Event;
import com.example.bookingSystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventResponse createEvent(CreateEventRequest request) {
        Event event = EventMapper.toEntity(request);
        return EventMapper.toResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findByDateGreaterThanEqualOrderByDateAsc(LocalDate.now())
                .stream().map(EventMapper::toResponse).toList();
    }

    public List<EventResponse> getAllEventsAdmin() {
        return eventRepository.findAll()
                .stream().map(EventMapper::toResponse).toList();
    }

    public EventResponse getEventResponseById(Long eventId) {
        Event event = getEventById(eventId);
        return EventMapper.toResponse(event);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));
    }

    public List<EventResponse> getFeaturedEvents() {
        return eventRepository.findByIsFeaturedTrueAndDateGreaterThanEqualOrderByDateAsc(LocalDate.now())
                .stream().map(EventMapper::toResponse).toList();
    }

    public List<String> getCategories() {
        return eventRepository.findDistinctCategories();
    }

    public List<EventResponse> searchEvents(String query, String category, String dateFrom, String dateTo) {
        LocalDate from = (dateFrom != null && !dateFrom.isEmpty()) ? LocalDate.parse(dateFrom) : null;
        LocalDate to = (dateTo != null && !dateTo.isEmpty()) ? LocalDate.parse(dateTo) : null;
        String q = (query != null && !query.isEmpty()) ? query : null;
        String cat = (category != null && !category.isEmpty() && !"all".equals(category)) ? category : null;

        return eventRepository.searchEvents(LocalDate.now(), q, cat, from, to)
                .stream().map(EventMapper::toResponse).toList();
    }

    @Transactional
    public EventResponse updateEvent(Long eventId, CreateEventRequest request) {
        Event event = getEventById(eventId);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setLocation(request.getLocation());
        event.setDate(LocalDate.parse(request.getDate()));
        event.setTime(request.getTime());
        event.setPriceCents(request.getPriceCents());
        if (request.getTotalTickets() != null) {
            int diff = request.getTotalTickets() - event.getTotalTickets();
            event.setTotalTickets(request.getTotalTickets());
            event.setAvailableTickets(event.getAvailableTickets() + diff);
        }
        event.setImageUrl(request.getImageUrl());
        event.setCategory(request.getCategory());
        event.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
        return EventMapper.toResponse(eventRepository.save(event));
    }

    public void deleteEvent(Long eventId) {
        Event event = getEventById(eventId);
        eventRepository.delete(event);
    }

    public long getEventsCount() {
        return eventRepository.count();
    }

    public long getUpcomingEventsCount() {
        return eventRepository.countByDateGreaterThanEqual(LocalDate.now());
    }
}
