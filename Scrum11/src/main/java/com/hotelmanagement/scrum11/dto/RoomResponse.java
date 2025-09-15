package com.hotelmanagement.scrum11.dto;

import com.hotelmanagement.scrum11.rooms.RoomType;
import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String name,
        RoomType type,
        BigDecimal price,
        Boolean available,
        Double rating,
        String description,
        String imageUrl
) {}
