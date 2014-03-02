package net.nemerosa.iteach.dao.jdbc;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public final class SQLUtils {

    private SQLUtils() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static Timestamp toTimestamp(ZonedDateTime dateTime) {
        return new Timestamp(dateTime.toInstant().getEpochSecond() * 1000L);
    }

}
