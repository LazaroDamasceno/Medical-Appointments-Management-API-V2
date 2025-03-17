package com.api.v2.common;

import java.time.LocalDate;

public final class BlockedDateTimeHandler {

    public static void handle(LocalDate date) {
        if (DateTimeChecker.isBeforeToday(date)) {
            throw new BlockedBookingDateTimeException();
        }
    }

}
