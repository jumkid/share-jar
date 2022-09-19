package com.jumkid.share.util;

import com.jumkid.share.exception.ModificationDatetimeOutdatedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ModificationDatetimeComparator {

    private ModificationDatetimeComparator() {}

    public static boolean isSameTime(final LocalDateTime dt1, final LocalDateTime dt2) throws ModificationDatetimeOutdatedException {
        var dt1ms = dt1.truncatedTo(ChronoUnit.MILLIS);
        var dt2ms = dt2.truncatedTo(ChronoUnit.MILLIS);

        return dt1ms.equals(dt2ms);
    }

}
