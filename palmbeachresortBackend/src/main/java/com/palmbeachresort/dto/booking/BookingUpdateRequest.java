package com.palmbeachresort.dto.booking;

import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public class BookingUpdateRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestCount;
    private String specialRequests;

    // Default constructor
    public BookingUpdateRequest() {}

    // Getters and Setters
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}