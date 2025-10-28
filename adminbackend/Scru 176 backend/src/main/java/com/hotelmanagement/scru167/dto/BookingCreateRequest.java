package com.hotelmanagement.scru167.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BookingCreateRequest(
        @NotNull Long customerId,
        @NotNull Long roomId,
        @NotNull LocalDate checkIn,
        @NotNull LocalDate checkOut
) {}
