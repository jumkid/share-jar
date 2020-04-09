package com.jumkid.share.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

}
