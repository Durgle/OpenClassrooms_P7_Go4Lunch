package com.example.go4lunch.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class TimeUtils {

    /**
     * Calculates the difference in milliseconds between the current time and a specified target time.
     */
    public static long calculateMillisecondsUntilTarget(int hours, int minutes, int seconds) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime targetTime = LocalDateTime.of(
                currentTime.toLocalDate(),
                LocalTime.of(hours, minutes, seconds)
        );

        if (currentTime.isAfter(targetTime)) {
            targetTime = targetTime.plusDays(1);
        }

        return currentTime.until(targetTime, ChronoUnit.MILLIS);
    }

    /**
     * Formats it into a human-readable representation of the date and time
     *
     * @param timestamp Timestamp
     * @return Formatted date
     */
    public static String formatDateTimeForDisplay(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendLocalized(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                .toFormatter(Locale.getDefault());
        return dateTime.format(formatter);
    }

}
