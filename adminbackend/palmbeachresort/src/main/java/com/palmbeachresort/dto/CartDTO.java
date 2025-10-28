package com.palmbeachresort.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
    private Long id;
    private String sessionId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;

    // Constructors, Getters, Setters
    public CartDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}