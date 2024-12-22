package com.ctnapps.suvelatakipisi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class WaterReminderBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "WaterReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "waterReminderChannel")

                .setContentTitle("Su İçme Zamanı!")
                .setContentText("Su içmeyi unutmayın.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, builder.build());
        Log.d(TAG, "Notification sent");
    }
}
