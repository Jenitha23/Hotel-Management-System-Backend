package com.palmbeachresort.service;

import com.palmbeachresort.dto.booking.BookingRequest;
import com.palmbeachresort.dto.booking.BookingResponse;
import com.palmbeachresort.dto.booking.BookingUpdateRequest;
import com.palmbeachresort.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    // Customer operations
    BookingResponse createBooking(BookingRequest bookingRequest, Long customerId);
    List<BookingResponse> getCustomerBookings(Long customerId);
    BookingResponse getCustomerBooking(Long bookingId, Long customerId);
    BookingResponse cancelBooking(Long bookingId, Long customerId);
    BookingResponse updateBooking(Long bookingId, Long customerId, BookingUpdateRequest updateRequest);

    // Admin operations
    List<BookingResponse> getAllBookings();
    List<BookingResponse> getBookingsByStatus(Booking.BookingStatus status);
    BookingResponse getBookingById(Long bookingId);
    BookingResponse updateBookingStatus(Long bookingId, Booking.BookingStatus status);
    BookingResponse updateBooking(Long bookingId, BookingUpdateRequest updateRequest);
    void deleteBooking(Long bookingId);

    // Utility methods
    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
    List<BookingResponse> getTodayCheckIns();
    List<BookingResponse> getTodayCheckOuts();
    BookingResponse checkIn(Long bookingId);
    BookingResponse checkOut(Long bookingId);

    // Statistics
    Long getBookingCountByStatus(Booking.BookingStatus status);
}