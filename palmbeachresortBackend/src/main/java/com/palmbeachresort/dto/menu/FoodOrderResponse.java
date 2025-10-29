// [file name]: FoodOrderResponse.java
package com.palmbeachresort.dto.menu;

import com.palmbeachresort.entity.menu.FoodOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FoodOrderResponse {

    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private FoodOrder.OrderStatus status;
    private String specialInstructions;
    private String roomNumber;
    private Integer estimatedPreparationTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private boolean canCancel;

    // Constructors
    public FoodOrderResponse() {}

    public FoodOrderResponse(Long id, String orderNumber, Long customerId,
                             String customerName, String customerEmail,
                             List<OrderItemResponse> items, BigDecimal totalAmount,
                             FoodOrder.OrderStatus status, String specialInstructions,
                             String roomNumber, Integer estimatedPreparationTime,
                             LocalDateTime createdAt, LocalDateTime updatedAt,
                             LocalDateTime completedAt, boolean canCancel) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.specialInstructions = specialInstructions;
        this.roomNumber = roomNumber;
        this.estimatedPreparationTime = estimatedPreparationTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
        this.canCancel = canCancel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public FoodOrder.OrderStatus getStatus() { return status; }
    public void setStatus(FoodOrder.OrderStatus status) { this.status = status; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Integer getEstimatedPreparationTime() { return estimatedPreparationTime; }
    public void setEstimatedPreparationTime(Integer estimatedPreparationTime) { this.estimatedPreparationTime = estimatedPreparationTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public boolean isCanCancel() { return canCancel; }
    public void setCanCancel(boolean canCancel) { this.canCancel = canCancel; }
}