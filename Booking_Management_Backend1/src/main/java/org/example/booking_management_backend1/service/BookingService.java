package org.example.booking_management_backend1.service;


import org.example.booking_management_backend1.dto.BookingRequest;
import org.example.booking_management_backend1.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    List<BookingResponse> getBookingsByCustomerId(Long customerId);
    BookingResponse getBookingById(Long id);
    BookingResponse cancelBooking(Long id);
    List<BookingResponse> getAllBookings();
}