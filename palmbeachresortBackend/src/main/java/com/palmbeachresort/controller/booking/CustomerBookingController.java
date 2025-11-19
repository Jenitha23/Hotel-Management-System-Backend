package com.palmbeachresort.controller.booking;

import com.palmbeachresort.dto.booking.BookingRequest;
import com.palmbeachresort.dto.booking.BookingResponse;
import com.palmbeachresort.dto.booking.BookingUpdateRequest;
import com.palmbeachresort.service.BookingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/bookings")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class CustomerBookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new booking
     * POST /api/customer/bookings
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest,
                                                         HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookingResponse booking = bookingService.createBooking(bookingRequest, customerId);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /**
     * Get all bookings for current customer
     * GET /api/customer/bookings
     */
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getCustomerBookings(HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<BookingResponse> bookings = bookingService.getCustomerBookings(customerId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Get specific booking by ID
     * GET /api/customer/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getCustomerBooking(@PathVariable Long id, HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookingResponse booking = bookingService.getCustomerBooking(id, customerId);
        return ResponseEntity.ok(booking);
    }

    /**
     * Cancel a booking
     * POST /api/customer/bookings/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id, HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookingResponse cancelledBooking = bookingService.cancelBooking(id, customerId);
        return ResponseEntity.ok(cancelledBooking);
    }

    /**
     * Update a booking
     * PUT /api/customer/bookings/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                         @Valid @RequestBody BookingUpdateRequest updateRequest,
                                                         HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookingResponse updatedBooking = bookingService.updateBooking(id, customerId, updateRequest);
        return ResponseEntity.ok(updatedBooking);
    }
}