package org.example.booking_management_backend1.controller;


import jakarta.validation.Valid;
import org.example.booking_management_backend1.dto.ApiResponse;
import org.example.booking_management_backend1.dto.BookingResponse;
import org.example.booking_management_backend1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.example.booking_management_backend1.dto.BookingRequest;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        try {
            BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Booking created successfully", bookingResponse)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getBookingsByCustomerId(@PathVariable Long customerId) {
        try {
            List<BookingResponse> bookings = bookingService.getBookingsByCustomerId(customerId);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Bookings retrieved successfully", bookings)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBookingById(@PathVariable Long id) {
        try {
            BookingResponse booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Booking retrieved successfully", booking)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
// cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long id) {
        try {
            BookingResponse cancelledBooking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Booking cancelled successfully", cancelledBooking)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBookings() {
        try {
            List<BookingResponse> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(
                    new ApiResponse(true, "All bookings retrieved ", bookings)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving bookings: " + e.getMessage()));
        }
    }
}
