package net.nemerosa.iteach.ui.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Locale;

public interface UIFormatter {

    String formatDate(LocalDateTime dateTime, Locale locale);

    String formatDate(LocalDate date, Locale locale);

    String formatTime(LocalDateTime dateTime, Locale locale);

    String formatTime(LocalTime time, Locale locale);

    String formatMonth(YearMonth month, Locale locale);

    String formatDateTime(LocalDateTime dateTime, Locale locale);
}
