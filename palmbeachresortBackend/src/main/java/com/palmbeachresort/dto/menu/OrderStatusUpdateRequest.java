// [file name]: OrderStatusUpdateRequest.java
package com.palmbeachresort.dto.menu;

import com.palmbeachresort.entity.menu.FoodOrder;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private FoodOrder.OrderStatus status;

    // Constructors
    public OrderStatusUpdateRequest() {}

    public OrderStatusUpdateRequest(FoodOrder.OrderStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public FoodOrder.OrderStatus getStatus() { return status; }
    public void setStatus(FoodOrder.OrderStatus status) { this.status = status; }
}