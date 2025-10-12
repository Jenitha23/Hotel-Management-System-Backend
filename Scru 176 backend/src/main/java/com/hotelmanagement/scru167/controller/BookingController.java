package com.hotelmanagement.scru167.controller;


import com.hotelmanagement.scru167.dto.BookingCreateRequest;
import com.hotelmanagement.scru167.domain.Booking;
import com.hotelmanagement.scru167.Services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService s) { this.bookingService = s; }

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody @Valid BookingCreateRequest req) {
        return ResponseEntity.ok(bookingService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> get(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.get(id));
    }
}
