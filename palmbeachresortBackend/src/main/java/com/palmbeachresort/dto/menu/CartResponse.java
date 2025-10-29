// [file name]: CartResponse.java
package com.palmbeachresort.dto.menu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CartResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime updatedAt;

    // Default constructor
    public CartResponse() {
        this.items = Collections.emptyList();
        this.totalAmount = BigDecimal.ZERO;
        this.totalItems = 0;
        this.updatedAt = LocalDateTime.now();
    }

    // Updated constructor with null safety
    public CartResponse(Long id, Long customerId, String customerName,
                        List<CartItemResponse> items, BigDecimal totalAmount,
                        Integer totalItems, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName != null ? customerName : "Unknown Customer";
        this.items = items != null ? items : Collections.emptyList(); // NEVER NULL
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO; // NEVER NULL
        this.totalItems = totalItems != null ? totalItems : 0; // NEVER NULL
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now(); // NEVER NULL
    }

    // Getters and Setters with null safety
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) {
        this.customerName = customerName != null ? customerName : "Unknown Customer";
    }

    public List<CartItemResponse> getItems() {
        return items != null ? items : Collections.emptyList(); // NEVER NULL
    }
    public void setItems(List<CartItemResponse> items) {
        this.items = items != null ? items : Collections.emptyList(); // NEVER NULL
    }

    public BigDecimal getTotalAmount() {
        return totalAmount != null ? totalAmount : BigDecimal.ZERO; // NEVER NULL
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO; // NEVER NULL
    }

    public Integer getTotalItems() {
        return totalItems != null ? totalItems : 0; // NEVER NULL
    }
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems != null ? totalItems : 0; // NEVER NULL
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt != null ? updatedAt : LocalDateTime.now(); // NEVER NULL
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now(); // NEVER NULL
    }
}