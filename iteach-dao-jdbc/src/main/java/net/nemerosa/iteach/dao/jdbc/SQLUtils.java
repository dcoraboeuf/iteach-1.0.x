package net.nemerosa.iteach.dao.jdbc;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class SQLUtils {

    private SQLUtils() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static String toTimestamp(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
