package com.example.go4lunch.utils;

import static com.example.go4lunch.ui.placeDetail.PlaceDetailActivity.PLACE_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.ui.placeDetail.PlaceDetailActivity;

public class NotificationUtils {

    public static void createLunchNotification(@NonNull Context context, @NonNull Place place, @Nullable String workmates) {

        String LUNCH_CHANNEL_ID = "Lunch_channel";
        int LUNCH_NOTIFICATION_ID = 1;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(LUNCH_CHANNEL_ID, context.getString(R.string.lunch_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.lunch_channel_description));
            notificationManager.createNotificationChannel(channel);
        }

        // Define the activity used when a user clicks on the notification.
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PLACE_ID, place.getUid());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String content;
        if (workmates != null) {
            content = context.getString(R.string.lunch_notification_content, place.getName(), place.getAddress(), workmates);
        } else {
            content = context.getString(R.string.lunch_notification_content_no_workmate, place.getName(), place.getAddress());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, LUNCH_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_dining_24)
                .setContentTitle(context.getString(R.string.lunch_notification_title))
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify(LUNCH_NOTIFICATION_ID, builder.build());
    }

}
