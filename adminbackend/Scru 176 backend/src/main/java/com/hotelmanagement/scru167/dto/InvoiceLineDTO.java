package com.hotelmanagement.scru167.dto;

import java.math.BigDecimal;

public record InvoiceLineDTO(
        String category,      // "Room", "Food", "Room Service"
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {}
