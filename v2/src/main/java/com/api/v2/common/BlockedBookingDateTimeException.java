package com.api.v2.common;

public class BlockedBookingDateTimeException extends RuntimeException {
    public BlockedBookingDateTimeException() {
        super("Booking date and time must be today or in the future.");
    }
}
