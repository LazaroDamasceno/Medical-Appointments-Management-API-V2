package com.api.v2.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeFormatter {

    public static String format(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        return "%s%s[%s]".formatted(localDateTime, zoneOffset, zoneId);
    }

}
