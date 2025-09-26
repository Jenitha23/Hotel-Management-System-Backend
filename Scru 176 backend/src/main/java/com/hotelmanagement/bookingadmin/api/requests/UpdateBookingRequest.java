package com.hotelmanagement.bookingadmin.api.requests;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateBookingRequest(
        @NotNull Long roomId,
        @NotNull LocalDate checkInDate,
        @NotNull LocalDate checkOutDate,
        @Size(max = 5000) String specialRequest,
        @NotNull Long version
) {}
