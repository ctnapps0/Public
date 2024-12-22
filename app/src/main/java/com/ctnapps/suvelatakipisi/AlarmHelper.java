package com.ctnapps.suvelatakipisi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class AlarmHelper {

    public static void scheduleReminderWithExactTime(Context context, int hour, int minute, int interval, int messageIndex, String channelId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE);
        String[] messages;
        if ("waterReminderChannel".equals(channelId)) {
            messages = context.getResources().getStringArray(R.array.water_reminder_messages);
        } else {
            messages = context.getResources().getStringArray(R.array.medication_reminder_messages);
        }

        intent.putExtra("title", channelId.equals("waterReminderChannel") ? "Su İçme Hatırlatıcısı" : "İlaç Hatırlatıcısı");
        intent.putExtra("content", messages[messageIndex]);
        intent.putExtra("channelId", channelId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long triggerAtMillis = calculateTriggerAtMillis(hour, minute);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    private static long calculateTriggerAtMillis(int hour, int minute) {
        long currentTimeMillis = System.currentTimeMillis();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static void scheduleReminder(Context context, int interval, String channelId) {
        // İlk açılışta bildirim gönderimini iptal ettik
        // Bu metodu sadece belirli zamanlarda çağıracağız

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE);
        String[] messages;
        int messageIndex;

        if ("waterReminderChannel".equals(channelId)) {
            messages = context.getResources().getStringArray(R.array.water_reminder_messages);
            messageIndex = sharedPreferences.getInt("waterMessageIndex", 0);
            intent.putExtra("title", "Su İçme Hatırlatıcısı");
            intent.putExtra("content", messages[messageIndex]);
        } else {
            messages = context.getResources().getStringArray(R.array.medication_reminder_messages);
            messageIndex = sharedPreferences.getInt("medicationMessageIndex", 0);
            intent.putExtra("title", "İlaç Hatırlatıcısı");
            intent.putExtra("content", messages[messageIndex]);
        }

        intent.putExtra("channelId", channelId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long triggerAtMillis = System.currentTimeMillis() + interval * 60000; // Dakikayı milisaniyeye çevir

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }
}
