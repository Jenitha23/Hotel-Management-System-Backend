package com.hotelmanagement.scrum11.dto;

import com.hotelmanagement.scrum11.rooms.RoomType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RoomRequest(
        @NotBlank String name,
        @NotNull RoomType type,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotNull Boolean available,
        @DecimalMin("0.0") @DecimalMax("5.0") Double rating,
        String description,
        String imageUrl
) {}
