package com.hotelmanagement.scru167.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OrderItemRequest(
        @NotNull Long bookingId,
        @NotBlank String type,     // FOOD or ROOM_SERVICE
        @NotBlank String description,
        @Positive int quantity,
        @Positive BigDecimal unitPrice
) {}
