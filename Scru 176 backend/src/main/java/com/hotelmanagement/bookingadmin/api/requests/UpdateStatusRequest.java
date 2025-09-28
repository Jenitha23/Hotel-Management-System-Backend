package com.hotelmanagement.bookingadmin.api.requests;

import com.hotelmanagement.bookingadmin.domain.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull BookingStatus status,
        @NotNull Long version
) {}
