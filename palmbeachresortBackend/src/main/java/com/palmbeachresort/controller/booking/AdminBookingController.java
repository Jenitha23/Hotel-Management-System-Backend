package com.palmbeachresort.controller.booking;

import com.palmbeachresort.dto.booking.BookingResponse;
import com.palmbeachresort.dto.booking.BookingUpdateRequest;
import com.palmbeachresort.entity.Booking;
import com.palmbeachresort.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@CrossOrigin(origins = "https://frontend-palmbeachresort.vercel.app", allowCredentials = "true")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Get all bookings
     * GET /api/admin/bookings
     */
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Get booking by ID
     * GET /api/admin/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    /**
     * Get bookings by status
     * GET /api/admin/bookings/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(@PathVariable Booking.BookingStatus status) {
        List<BookingResponse> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Update booking status
     * PATCH /api/admin/bookings/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(@PathVariable Long id,
                                                               @RequestParam Booking.BookingStatus status) {
        BookingResponse updatedBooking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Update booking details
     * PUT /api/admin/bookings/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                         @RequestBody BookingUpdateRequest updateRequest) {
        BookingResponse updatedBooking = bookingService.updateBooking(id, updateRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Delete a booking
     * DELETE /api/admin/bookings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get today's check-ins
     * GET /api/admin/bookings/today/checkins
     */
    @GetMapping("/today/checkins")
    public ResponseEntity<List<BookingResponse>> getTodayCheckIns() {
        List<BookingResponse> checkIns = bookingService.getTodayCheckIns();
        return ResponseEntity.ok(checkIns);
    }

    /**
     * Get today's check-outs
     * GET /api/admin/bookings/today/checkouts
     */
    @GetMapping("/today/checkouts")
    public ResponseEntity<List<BookingResponse>> getTodayCheckOuts() {
        List<BookingResponse> checkOuts = bookingService.getTodayCheckOuts();
        return ResponseEntity.ok(checkOuts);
    }

    /**
     * Check-in a guest
     * POST /api/admin/bookings/{id}/checkin
     */
    @PostMapping("/{id}/checkin")
    public ResponseEntity<BookingResponse> checkIn(@PathVariable Long id) {
        BookingResponse checkedIn = bookingService.checkIn(id);
        return ResponseEntity.ok(checkedIn);
    }

    /**
     * Check-out a guest
     * POST /api/admin/bookings/{id}/checkout
     */
    @PostMapping("/{id}/checkout")
    public ResponseEntity<BookingResponse> checkOut(@PathVariable Long id) {
        BookingResponse checkedOut = bookingService.checkOut(id);
        return ResponseEntity.ok(checkedOut);
    }

    /**
     * Get booking statistics
     * GET /api/admin/bookings/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getBookingStatistics() {
        Long pendingCount = bookingService.getBookingCountByStatus(Booking.BookingStatus.PENDING);
        Long confirmedCount = bookingService.getBookingCountByStatus(Booking.BookingStatus.CONFIRMED);
        Long checkedInCount = bookingService.getBookingCountByStatus(Booking.BookingStatus.CHECKED_IN);
        Long checkedOutCount = bookingService.getBookingCountByStatus(Booking.BookingStatus.CHECKED_OUT);
        Long cancelledCount = bookingService.getBookingCountByStatus(Booking.BookingStatus.CANCELLED);

        return ResponseEntity.ok().body(new Object() {
            public final Long pending = pendingCount;
            public final Long confirmed = confirmedCount;
            public final Long checkedIn = checkedInCount;
            public final Long checkedOut = checkedOutCount;
            public final Long cancelled = cancelledCount;
            public final Long total = pendingCount + confirmedCount + checkedInCount + checkedOutCount + cancelledCount;
        });
    }
}