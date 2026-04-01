package com.example.bookingSystem.repository;

import com.example.bookingSystem.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByDateGreaterThanEqualOrderByDateAsc(LocalDate date);

    List<Event> findByIsFeaturedTrueAndDateGreaterThanEqualOrderByDateAsc(LocalDate date);

    @Query("SELECT DISTINCT e.category FROM Event e WHERE e.category IS NOT NULL ORDER BY e.category")
    List<String> findDistinctCategories();

    @Query("SELECT e FROM Event e WHERE e.date >= :fromDate " +
           "AND (:query IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(e.description) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(e.venue) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(e.location) LIKE LOWER(CONCAT('%',:query,'%'))) " +
           "AND (:category IS NULL OR e.category = :category) " +
           "AND (:dateFrom IS NULL OR e.date >= :dateFrom) " +
           "AND (:dateTo IS NULL OR e.date <= :dateTo) " +
           "ORDER BY e.date ASC")
    List<Event> searchEvents(
            @Param("fromDate") LocalDate fromDate,
            @Param("query") String query,
            @Param("category") String category,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo
    );

    long countByDateGreaterThanEqual(LocalDate date);
}
