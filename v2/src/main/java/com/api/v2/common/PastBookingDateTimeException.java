package com.api.v2.common;

public class PastBookingDateTimeException extends RuntimeException {
    public PastBookingDateTimeException() {
        super("Booking date and time must be today or in the future.");
    }
}
