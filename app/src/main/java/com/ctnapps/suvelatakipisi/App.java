package com.ctnapps.suvelatakipisi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String WATER_REMINDER_CHANNEL_ID = "water_reminder_channel";
    public static final String MEDICATION_REMINDER_CHANNEL_ID = "medication_reminder_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel waterReminderChannel = new NotificationChannel(
                    WATER_REMINDER_CHANNEL_ID,
                    "Water Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            waterReminderChannel.setDescription("Channel for water intake reminders");

            NotificationChannel medicationReminderChannel = new NotificationChannel(
                    MEDICATION_REMINDER_CHANNEL_ID,
                    "Medication Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            medicationReminderChannel.setDescription("Channel for medication reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(waterReminderChannel);
            manager.createNotificationChannel(medicationReminderChannel);
        }
    }
}
