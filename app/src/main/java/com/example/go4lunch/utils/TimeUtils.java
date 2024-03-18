package com.example.go4lunch.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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

}
