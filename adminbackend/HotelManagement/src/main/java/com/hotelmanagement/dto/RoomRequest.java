package com.hotelmanagement.dto;

import com.hotelmanagement.entity.RoomType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request payload used when creating/updating a room.
 */
public class RoomRequest {

    @NotBlank(message = "roomNumber is required")
    @Size(max = 32)
    private String roomNumber;

    @NotNull(message = "type is required")
    private RoomType type;

    @NotNull(message = "pricePerNight is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "pricePerNight must be >= 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal pricePerNight;

    @Min(value = 1, message = "capacity must be >= 1")
    private int capacity;

    private boolean available;

    @Size(max = 10_000)
    private String description;

    // Getters/Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
