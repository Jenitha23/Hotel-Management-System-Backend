package com.hotelmanagement.bookingadmin.controller;

import com.hotelmanagement.bookingadmin.api.requests.UpdateBookingRequest;
import com.hotelmanagement.bookingadmin.api.requests.UpdateStatusRequest;
import com.hotelmanagement.bookingadmin.domain.BookingStatus;
import com.hotelmanagement.bookingadmin.dto.BookingDetailDto;
import com.hotelmanagement.bookingadmin.dto.BookingSummaryDto;
import com.hotelmanagement.bookingadmin.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // SCRU-177: list with filters + pagination/sort
    @GetMapping
    public ResponseEntity<Page<BookingSummaryDto>> getAll(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "checkInDate,asc") String sort
    ) {
        Sort s = parseSort(sort);
        // If single date provided and range not provided, treat as exact-day intersection
        LocalDate from = dateFrom;
        LocalDate to = dateTo;
        if (date != null && from == null && to == null) {
            from = date;
            to = date;
        }
        Pageable pageable = PageRequest.of(page, size, s);
        return ResponseEntity.ok(service.getAll(status, roomType, from, to, pageable));
    }

    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(Sort.Order.asc("checkInDate"));
        }
        String[] parts = sortParam.split(";");
        java.util.List<Sort.Order> orders = new java.util.ArrayList<>();
        for (String part : parts) {
            String p = part.trim();
            if (p.isEmpty()) continue;
            String[] kv = p.split(",");
            String property = kv[0].trim();
            if (property.isEmpty()) continue;
            Sort.Direction dir = Sort.Direction.ASC;
            if (kv.length > 1) {
                String d = kv[1].trim();
                if ("desc".equalsIgnoreCase(d)) dir = Sort.Direction.DESC;
            }
            orders.add(new Sort.Order(dir, property));
        }
        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "checkInDate"));
        }
        return Sort.by(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // SCRU-178: Update Status
    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDetailDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest req
    ) {
        return ResponseEntity.ok(service.updateStatus(id, req));
    }

    // SCRU-179: Edit Details
    @PutMapping("/{id}")
    public ResponseEntity<BookingDetailDto> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequest req
    ) {
        return ResponseEntity.ok(service.updateBooking(id, req));
    }

    // SCRU-180: delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().body(
                java.util.Map.of("message", "Booking deleted successfully")
        );
    }
}
