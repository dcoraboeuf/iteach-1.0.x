package net.nemerosa.iteach.ui.support;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class DefaultUIFormatter implements UIFormatter {
    @Override
    public String formatDate(LocalDateTime dateTime, Locale locale) {
        return dateTime != null ? formatDate(dateTime.toLocalDate(), locale) : "";
    }

    @Override
    public String formatDate(LocalDate date, Locale locale) {
        return date != null ? date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)) : "";
    }

    @Override
    public String formatTime(LocalDateTime dateTime, Locale locale) {
        return dateTime != null ? formatTime(dateTime.toLocalTime(), locale) : "";
    }

    @Override
    public String formatTime(LocalTime time, Locale locale) {
        return time != null ? time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)) : "";
    }

    @Override
    public String formatMonth(YearMonth month, Locale locale) {
        return month != null ? month.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale)) : "";
    }

    @Override
    public String formatDateTime(LocalDateTime dateTime, Locale locale) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale)) : "";
    }
}
