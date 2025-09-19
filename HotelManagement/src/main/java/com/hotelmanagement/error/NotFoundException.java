package com.hotelmanagement.error;


/**
 * 404 marker exception.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
