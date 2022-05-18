package com.jumkid.share.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String FORMAT_DDMMYYYY_HHMM = "dd-MM-yyyy HH:mm";
    public static final String FORMAT_YYYYMMDDTHHMM = "yyyy-MM-dd'T'HH:mm";
    public static final String FORMAT_MMDDYYYYTHHMM = "MM-dd-yyyy'T'HH:mm";
    public static final String YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YYYYMMDDTHHMMSS3S = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String GUEST_ROLE = "guest";
    public static final String ADMIN_ROLE = "admin";

}
