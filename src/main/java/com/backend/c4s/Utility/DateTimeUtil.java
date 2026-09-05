package com.backend.c4s.Utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    public static final String DEFAULT_DATE_TIME_FORMAT= "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd";
    public static final String DISPLAY_DATE_TIME_FORMAT="dd MMM yyyy, hh:mm a";

    private DateTimeUtil() {}

    public static String formateDateTime(LocalDateTime dateTime){
        if (dateTime==null)return null;
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }
    public static String formatDisplayDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(DISPLAY_DATE_TIME_FORMAT));
    }
    public static String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }
    public static long toEpochMillis(LocalDateTime dateTime) {
        if (dateTime == null) return 0L;
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
