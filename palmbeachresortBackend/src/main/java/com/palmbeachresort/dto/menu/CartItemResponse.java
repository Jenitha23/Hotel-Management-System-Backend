// [file name]: CartItemResponse.java
package com.palmbeachresort.dto.menu;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemImage;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;

    // Constructors
    public CartItemResponse() {}

    public CartItemResponse(Long id, Long menuItemId, String menuItemName,
                            String menuItemImage, BigDecimal unitPrice,
                            Integer quantity, BigDecimal subtotal) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.menuItemImage = menuItemImage;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
    public String getMenuItemImage() { return menuItemImage; }
    public void setMenuItemImage(String menuItemImage) { this.menuItemImage = menuItemImage; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}