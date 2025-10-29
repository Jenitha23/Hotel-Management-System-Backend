package com.palmbeachresort.repository;

import com.palmbeachresort.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by customer
    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // Find booking by reference
    Optional<Booking> findByBookingReference(String bookingReference);

    // Find bookings by status
    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);

    // Find overlapping bookings for a room
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
            "AND b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
            "AND ((b.checkInDate BETWEEN :checkInDate AND :checkOutDate) " +
            "OR (b.checkOutDate BETWEEN :checkInDate AND :checkOutDate) " +
            "OR (b.checkInDate <= :checkInDate AND b.checkOutDate >= :checkOutDate))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);

    // Find today's check-ins
    @Query("SELECT b FROM Booking b WHERE b.checkInDate = :today " +
            "AND b.status = 'CONFIRMED' ORDER BY b.checkInDate")
    List<Booking> findTodayCheckIns(@Param("today") LocalDate today);

    // Find today's check-outs
    @Query("SELECT b FROM Booking b WHERE b.checkOutDate = :today " +
            "AND b.status IN ('CONFIRMED', 'CHECKED_IN') ORDER BY b.checkOutDate")
    List<Booking> findTodayCheckOuts(@Param("today") LocalDate today);

    // Count bookings by status
    Long countByStatus(Booking.BookingStatus status);

    // Find bookings within date range
    @Query("SELECT b FROM Booking b WHERE " +
            "((b.checkInDate BETWEEN :startDate AND :endDate) " +
            "OR (b.checkOutDate BETWEEN :startDate AND :endDate)) " +
            "ORDER BY b.checkInDate")
    List<Booking> findBookingsInDateRange(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}