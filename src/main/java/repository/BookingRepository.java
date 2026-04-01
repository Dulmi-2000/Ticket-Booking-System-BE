package com.example.bookingSystem.repository;

import com.example.bookingSystem.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    List<Booking> findByUserId(Long userId);
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(b.totalPriceCents), 0) FROM Booking b WHERE b.status = 'confirmed'")
    long sumConfirmedRevenue();

    List<Booking> findTop10ByOrderByBookedAtDesc();
}
