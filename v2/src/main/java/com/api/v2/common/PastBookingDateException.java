package com.api.v2.common;

public class PastBookingDateException extends RuntimeException {
    public PastBookingDateException() {
        super("Booking date must be today or in the future.");
    }
}
