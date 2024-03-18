package com.example.go4lunch.injection;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.example.go4lunch.MainApplication;
import com.example.go4lunch.worker.NotificationWorker;

public class Go4LunchWorkerFactory extends WorkerFactory {

    private final MainApplication application;

    public Go4LunchWorkerFactory(MainApplication application) {
        this.application = application;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext, @NonNull String workerClassName, @NonNull WorkerParameters workerParameters) {
        if (workerClassName.equals(NotificationWorker.class.getName())) {
            return new NotificationWorker(appContext, workerParameters);
        }
        return null;
    }
}