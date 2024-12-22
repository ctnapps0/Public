package com.ctnapps.suvelatakipisi;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_POST_NOTIFICATIONS = 1001;

    private Button waterReminderSettingsButton, medicationReminderSettingsButton;
    private ProgressBar waterProgressBar;
    private AlarmManager alarmManager;
    private PendingIntent waterPendingIntent, medicationPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // İzin kontrolü ve isteme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, REQUEST_POST_NOTIFICATIONS);
            }
        }

        Button feedbackButton = findViewById(R.id.feedbackButton);
        waterReminderSettingsButton = findViewById(R.id.waterReminderSettingsButton);
        medicationReminderSettingsButton = findViewById(R.id.medicationReminderSettingsButton);
        waterProgressBar = findViewById(R.id.waterProgressBar);

        feedbackButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        waterReminderSettingsButton.setOnClickListener(v -> {
            Log.d(TAG, "waterReminderSettingsButton clicked");
            openSettingsActivity("water");
        });

        medicationReminderSettingsButton.setOnClickListener(v -> {
            Log.d(TAG, "medicationReminderSettingsButton clicked");
            openSettingsActivity("medication");
        });

        createNotificationChannels();
        applyNotificationSettings();
    }

    private void openSettingsActivity(String type) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("reminderType", type);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_statistics) { // İstatistik ekranı
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bildirim izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bildirim izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel waterChannel = new NotificationChannel(
                    "waterReminderChannel",
                    "Water Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            waterChannel.setDescription("Channel for water reminders");

            NotificationChannel medicationChannel = new NotificationChannel(
                    "medicationReminderChannel",
                    "Medication Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            medicationChannel.setDescription("Channel for medication reminders");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(waterChannel);
            notificationManager.createNotificationChannel(medicationChannel);
            Log.d(TAG, "Notification channels created");
        }
    }

    private void applyNotificationSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        Uri notificationSoundUri = Uri.parse(sharedPreferences.getString("notification_sound_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()));
        int vibrationPattern = sharedPreferences.getInt("vibration_pattern", 0);

        if (notificationsEnabled) {
            sendCustomNotification("Hatırlatma", "Zamanı geldi!", "waterReminderChannel", notificationSoundUri, vibrationPattern);
        }
    }

    private void sendCustomNotification(String title, String text, String channelId, Uri soundUri, int vibration) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(getVibrationPattern(vibration));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private long[] getVibrationPattern(int pattern) {
        switch (pattern) {
            case 1:
                return new long[]{0, 500, 1000, 500};
            case 2:
                return new long[]{0, 250, 500, 250};
            default:
                return new long[]{0, 500, 1000, 500};
        }
    }

    private void scheduleNotification(String title, String content, long delay) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("content", content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
