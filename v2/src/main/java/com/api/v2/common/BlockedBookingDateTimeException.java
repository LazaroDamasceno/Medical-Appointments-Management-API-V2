package com.api.v2.common;

public class BlockedBookingDateTimeException extends RuntimeException {
    public BlockedBookingDateTimeException(String message) {
        super(message);
    }
}
