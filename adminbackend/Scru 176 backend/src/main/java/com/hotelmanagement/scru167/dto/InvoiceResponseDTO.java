package com.hotelmanagement.scru167.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceResponseDTO(
        Long bookingId,
        String guestName,
        String roomNumber,
        LocalDate checkIn,
        LocalDate checkOut,
        int nights,
        List<InvoiceLineDTO> lines,
        BigDecimal subTotal,
        BigDecimal tax,          // e.g., 12.5%
        BigDecimal serviceCharge,// optional
        BigDecimal grandTotal
) {}
