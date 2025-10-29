// [file name]: BillingResponse.java
package com.palmbeachresort.dto.billing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BillingResponse {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDateTime generatedAt;

    // Room Booking Details
    private RoomBookingSummary roomBooking;

    // Food Order Details
    private List<FoodOrderSummary> foodOrders;

    // Financial Summary
    private BigDecimal roomTotal;
    private BigDecimal foodTotal;
    private BigDecimal grandTotal;
    private String currency = "USD";

    // Constructors
    public BillingResponse() {}

    public BillingResponse(Long customerId, String customerName, String customerEmail,
                           RoomBookingSummary roomBooking, List<FoodOrderSummary> foodOrders,
                           BigDecimal roomTotal, BigDecimal foodTotal, BigDecimal grandTotal) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.roomBooking = roomBooking;
        this.foodOrders = foodOrders;
        this.roomTotal = roomTotal;
        this.foodTotal = foodTotal;
        this.grandTotal = grandTotal;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public RoomBookingSummary getRoomBooking() { return roomBooking; }
    public void setRoomBooking(RoomBookingSummary roomBooking) { this.roomBooking = roomBooking; }

    public List<FoodOrderSummary> getFoodOrders() { return foodOrders; }
    public void setFoodOrders(List<FoodOrderSummary> foodOrders) { this.foodOrders = foodOrders; }

    public BigDecimal getRoomTotal() { return roomTotal; }
    public void setRoomTotal(BigDecimal roomTotal) { this.roomTotal = roomTotal; }

    public BigDecimal getFoodTotal() { return foodTotal; }
    public void setFoodTotal(BigDecimal foodTotal) { this.foodTotal = foodTotal; }

    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
