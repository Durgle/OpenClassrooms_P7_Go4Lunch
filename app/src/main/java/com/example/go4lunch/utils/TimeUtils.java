package com.example.go4lunch.utils;

import androidx.annotation.NonNull;

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
    public static long calculateMillisecondsUntilTarget(@NonNull LocalDateTime currentTime, int hours, int minutes, int seconds) {
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
    public static String formatDateTimeForDisplay(long timestamp, Locale locale) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendLocalized(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                .toFormatter(locale);
        return dateTime.format(formatter);
    }

}
