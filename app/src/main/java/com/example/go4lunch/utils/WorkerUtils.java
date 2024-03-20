package com.example.go4lunch.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.go4lunch.worker.NotificationWorker;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class WorkerUtils {

    /**
     * Schedules a lunch notification to be triggered at the specified time
     *
     * @param context Context
     * @param hours   Hours
     * @param minutes Minutes
     * @param seconds Seconds
     */
    public static void scheduleLunchNotification(@NonNull Context context, int hours, int minutes, int seconds) {

        LocalDateTime currentTime = LocalDateTime.now();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(TimeUtils.calculateMillisecondsUntilTarget(currentTime, hours, minutes, seconds), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .enqueueUniqueWork(NotificationWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest);
    }

}
