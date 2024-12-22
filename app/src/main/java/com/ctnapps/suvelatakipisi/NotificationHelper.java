package com.ctnapps.suvelatakipisi;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void sendWaterReminderNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.WATER_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Su İçme Zamanı")
                .setContentText("Bir bardak su içmeyi unutmayın!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }

    public static void sendMedicationReminderNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.MEDICATION_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_medication)
                .setContentTitle("İlaç Alma Zamanı")
                .setContentText("İlaçlarınızı almayı unutmayın!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());
    }

    public static void sendDailyWaterIntakeNotification(Context context, String dailyIntake) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("Günlük su tüketiminiz: " + dailyIntake + " mL. Daha fazla su içmeyi unutmayın!");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.WATER_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Günlük Su Tüketimi")
                .setStyle(bigTextStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(3, builder.build());
    }

    public static void sendWaterReminderWithAction(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.WATER_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Su İçme Zamanı")
                .setContentText("Bir bardak su içmeyi unutmayın!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_drink, "Hemen Su İç", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(4, builder.build());
    }

    public static void sendSilentNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.WATER_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle("Genel Hatırlatma")
                .setContentText("Günlük su hedefinizi kontrol edin.")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(5, builder.build());
    }
}
