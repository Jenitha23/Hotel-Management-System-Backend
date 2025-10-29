package com.palmbeachresort.dto.menu;

import jakarta.validation.constraints.Size;

public class FoodOrderRequest {

    @Size(max = 500, message = "Special instructions must not exceed 500 characters")
    private String specialInstructions;

    @Size(max = 20, message = "Room number must not exceed 20 characters")
    private String roomNumber;

    // Constructors
    public FoodOrderRequest() {}

    public FoodOrderRequest(String specialInstructions, String roomNumber) {
        this.specialInstructions = specialInstructions;
        this.roomNumber = roomNumber;
    }

    // Getters and Setters
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}