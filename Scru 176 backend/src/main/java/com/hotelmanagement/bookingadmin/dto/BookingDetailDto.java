package com.hotelmanagement.bookingadmin.dto;

import com.hotelmanagement.bookingadmin.domain.BookingStatus;
import java.time.LocalDate;

public record BookingDetailDto(
        Long id,
        Long customerId,
        String customerName,
        String customerEmail,
        Long roomId,
        String roomNumber,
        String roomType,
        LocalDate bookingDate,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BookingStatus status,
        String specialRequest,
        Long version
) {}

