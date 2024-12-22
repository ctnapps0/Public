package com.ctnapps.suvelatakipisi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MedicationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "MedicationReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "medicationReminderChannel")

                .setContentTitle("İlaç İçme Zamanı!")
                .setContentText("İlaçlarınızı almayı unutmayın.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1002, builder.build());
        Log.d(TAG, "Notification sent");
    }
}
