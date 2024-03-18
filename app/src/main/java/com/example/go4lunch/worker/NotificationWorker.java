package com.example.go4lunch.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.go4lunch.MainApplication;
import com.example.go4lunch.data.models.LunchNotification;
import com.example.go4lunch.data.models.firestore.Setting;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.utils.NotificationUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class NotificationWorker extends Worker {

    public static String UNIQUE_WORK_NAME = "lunch_notification";
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        MainApplication application = (MainApplication) context.getApplicationContext();
        this.userRepository = application.getUserRepository();
        this.settingRepository = application.getSettingRepository();
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            Setting settings = this.settingRepository.getSyncSettings();

            if (settings == null || settings.getEnableNotifications()) {
                LunchNotification lunchNotificationData = this.userRepository.getLunchNotificationData();
                if (lunchNotificationData != null) {

                    List<User> workmates = lunchNotificationData.getWorkmate();
                    String workmatesString;
                    if (workmates != null) {
                        workmatesString = workmates.stream()
                                .map(User::getDisplayName)
                                .collect(Collectors.joining(", "));
                    } else {
                        workmatesString = null;
                    }
                    NotificationUtils.createLunchNotification(getApplicationContext(), lunchNotificationData.getPlace(), workmatesString);
                    userRepository.updateChosenRestaurant(null);
                    return Result.success();
                }
                return Result.failure();
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e("NotificationWorker", e.toString());
            return Result.failure();
        }
        return Result.success();
    }
}
