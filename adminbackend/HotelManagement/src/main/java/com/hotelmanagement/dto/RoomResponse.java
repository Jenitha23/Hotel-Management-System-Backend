package com.hotelmanagement.dto;


import com.hotelmanagement.entity.RoomType;

import java.math.BigDecimal;

/**
 * Response payload returned to clients for room data.
 */
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private RoomType type;
    private BigDecimal pricePerNight;
    private int capacity;
    private boolean available;
    private String description;

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
