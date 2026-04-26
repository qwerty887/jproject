package org.jproject.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtils {

    public static Instant getCurrentTime() {
        return Instant.now();
    }

    public static Instant getMaxTime() {
        final LocalDateTime localDateTime = LocalDateTime.of(2999, 12, 31, 23, 59, 59);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
