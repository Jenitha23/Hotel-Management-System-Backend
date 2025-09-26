package com.hotelmanagement.bookingadmin.service;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) { super(msg); }
}
