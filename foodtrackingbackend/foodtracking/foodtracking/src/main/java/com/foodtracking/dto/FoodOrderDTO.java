package com.foodtracking.dto;

import com.foodtracking.model.OrderStatus;

public class FoodOrderDTO {
    private String id;
    private String item;
    private Integer quantity;
    private String orderTime;
    private String estimatedTime;
    private OrderStatus status;
    private String location;

    // Constructors
    public FoodOrderDTO() {}

    public FoodOrderDTO(String id, String item, Integer quantity, String orderTime,
                        String estimatedTime, OrderStatus status, String location) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.orderTime = orderTime;
        this.estimatedTime = estimatedTime;
        this.status = status;
        this.location = location;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}