package com.hotelmanagement.project.dto;

import com.hotelmanagement.project.model.OrderStatus;
import com.hotelmanagement.project.model.Priority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class OrderRequest {
    @NotBlank public String customerName;
    @NotBlank public String roomNumber;
    @NotBlank public String item;
    @Min(1) public int quantity;
    public LocalDateTime orderTime;
    public LocalDateTime estimatedTime;
    public OrderStatus status;
    public String location;
    public String specialInstructions;
    public Priority priority;
}
