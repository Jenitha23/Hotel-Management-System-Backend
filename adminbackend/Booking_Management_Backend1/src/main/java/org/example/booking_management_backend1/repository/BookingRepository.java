package org.example.booking_management_backend1.repository;

import org.example.booking_management_backend1.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);

    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingsByCustomerId(Long customerId);
}