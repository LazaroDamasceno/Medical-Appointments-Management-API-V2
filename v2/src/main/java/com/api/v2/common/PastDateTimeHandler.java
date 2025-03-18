package com.api.v2.common;

import java.time.LocalDate;

public final class PastDateTimeHandler {

    public static void handle(LocalDate date) {
        if (PastDateTimeChecker.isBeforeToday(date)) {
            throw new PastBookingDateTimeException();
        }
    }

}
