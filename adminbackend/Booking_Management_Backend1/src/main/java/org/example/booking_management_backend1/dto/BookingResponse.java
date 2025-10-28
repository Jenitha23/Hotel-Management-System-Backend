package org.example.booking_management_backend1.dto;


import org.example.booking_management_backend1.model.Booking;

import java.time.LocalDate;

public class BookingResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long roomId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private String status;
    private Double totalPrice;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.customerId = booking.getCustomer().getId();
        this.customerName = booking.getCustomer().getName();
        this.roomId = booking.getRoom().getId();
        this.roomNumber = booking.getRoom().getRoomNumber();
        this.checkInDate = booking.getCheckInDate();
        this.checkOutDate = booking.getCheckOutDate();
        this.numberOfGuests = booking.getNumberOfGuests();
        this.status = booking.getStatus();

        // Calculate total price
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                booking.getCheckInDate(), booking.getCheckOutDate());
        this.totalPrice = nights * booking.getRoom().getPrice();
    }

    // Getters
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Long getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public Integer getNumberOfGuests() { return numberOfGuests; }
    public String getStatus() { return status; }
    public Double getTotalPrice() { return totalPrice; }
}