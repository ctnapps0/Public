package com.ctnapps.suvelatakipisi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String DEFAULT_CHANNEL_ID = "default_channel";
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String channelId = intent.getStringExtra("channelId");

        Log.d(TAG, "Notification received: Title = " + title + ", Content = " + content + ", Channel = " + channelId);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Hatırlatma Kanalı",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId != null ? channelId : DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
