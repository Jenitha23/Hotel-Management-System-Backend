package com.palmbeachresort.dto;

import com.palmbeachresort.model.Order;

public class OrderDTO {
    private String id;
    private String customerName;
    private String roomNumber;
    private String item;
    private Integer quantity;
    private String orderTime;
    private String estimatedTime;
    private String status;
    private String location;
    private String specialInstructions;
    private String priority;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.customerName = order.getCustomerName();
        this.roomNumber = order.getRoomNumber();
        this.item = order.getItem();
        this.quantity = order.getQuantity();
        this.orderTime = order.getOrderTime();
        this.estimatedTime = order.getEstimatedTime();
        this.status = order.getStatus().name().toLowerCase();
        this.location = order.getLocation();
        this.specialInstructions = order.getSpecialInstructions();
        this.priority = order.getPriority().name().toLowerCase();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}