package com.ctnapps.suvelatakipisi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationSettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RINGTONE = 1;

    private Switch notificationsEnabledSwitch;
    private Button selectNotificationSoundButton;
    private Spinner vibrationPatternSpinner;
    private Button saveSettingsButton;

    private Uri notificationSoundUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        notificationsEnabledSwitch = findViewById(R.id.notificationsEnabledSwitch);
        selectNotificationSoundButton = findViewById(R.id.selectNotificationSoundButton);
        vibrationPatternSpinner = findViewById(R.id.vibrationPatternSpinner);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        loadSettings();

        selectNotificationSoundButton.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Bildirim Sesini Seç");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationSoundUri);
            startActivityForResult(intent, REQUEST_CODE_RINGTONE);
        });

        saveSettingsButton.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RINGTONE && resultCode == RESULT_OK) {
            notificationSoundUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (notificationSoundUri != null) {
                selectNotificationSoundButton.setText(RingtoneManager.getRingtone(this, notificationSoundUri).getTitle(this));
            }
        }
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        notificationsEnabledSwitch.setChecked(sharedPreferences.getBoolean("notifications_enabled", true));
        notificationSoundUri = Uri.parse(sharedPreferences.getString("notification_sound_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()));
        vibrationPatternSpinner.setSelection(sharedPreferences.getInt("vibration_pattern", 0));
        selectNotificationSoundButton.setText(RingtoneManager.getRingtone(this, notificationSoundUri).getTitle(this));
    }

    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications_enabled", notificationsEnabledSwitch.isChecked());
        editor.putString("notification_sound_uri", notificationSoundUri.toString());
        editor.putInt("vibration_pattern", vibrationPatternSpinner.getSelectedItemPosition());
        editor.apply();
    }
}
