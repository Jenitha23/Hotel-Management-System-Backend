package com.hotelmanagement.bookingadmin.dto;


import com.hotelmanagement.bookingadmin.domain.BookingStatus;
import java.time.LocalDate;

public record BookingSummaryDto(
        Long id,
        String customerName,
        String customerEmail,
        String roomNumber,
        String roomType,
        LocalDate bookingDate,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BookingStatus status
) {}
