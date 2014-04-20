package net.nemerosa.iteach.dao.jdbc;

import org.joda.money.Money;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class SQLUtils {

    private SQLUtils() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static Timestamp toTimestamp(ZonedDateTime dateTime) {
        return new Timestamp(dateTime.toInstant().getEpochSecond() * 1000L);
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        } else {
            return Enum.valueOf(enumClass, value);
        }
    }

    public static ZonedDateTime fromTimestamp(ResultSet rs, String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return getDateTime(timestamp);
    }

    public static ZonedDateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneOffset.UTC) : null;
    }

    public static Money toMoney(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
        if (value == null) {
            return null;
        } else {
            return Money.parse(value);
        }
    }

    public static LocalDateTime getLocalDateTimeFromDB(ResultSet rs, String column) throws SQLException {
        String date = rs.getString(column);
        if (date != null) {
            return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            return null;
        }
    }

    public static String getDBValueFromLocalDateTime(LocalDateTime start) {
        if (start != null) {
            return start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            return null;
        }
    }

    public static Integer getInteger(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

}
