package com.palmbeachresort.dto.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FoodOrderSummary {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private List<FoodItem> items;
    private BigDecimal totalAmount;

    // Constructors
    public FoodOrderSummary() {}

    public FoodOrderSummary(Long orderId, String orderNumber, LocalDateTime orderDate,
                            String status, List<FoodItem> items, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<FoodItem> getItems() { return items; }
    public void setItems(List<FoodItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}