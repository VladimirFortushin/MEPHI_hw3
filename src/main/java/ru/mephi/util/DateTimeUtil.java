package ru.mephi.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static boolean isExpired(String expiresAt){
        if(expiresAt == null){
            return true;
        }
        ZonedDateTime expirationTime = ZonedDateTime.parse(expiresAt);
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(expirationTime);
    }

    public static String getCurrentFormattedDateShort() {
        return ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(dtf);
    }

    public static String getCurrentFormattedDate() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX");
        return now.format(formatter);
    }
}
