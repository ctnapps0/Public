package com.ctnapps.suvelatakipisi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText intervalEditText;
    private Spinner messageSpinner;
    private Button saveButton, profileSettingsButton, notificationSettingsButton, themeSettingsButton;
    private CheckBox timePickerCheckbox, intervalCheckbox;
    private SharedPreferences sharedPreferences;
    private String reminderType;

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reminderType = getIntent().getStringExtra("reminderType");
        if (reminderType == null) {
            setContentView(R.layout.activity_settings);
            initMainSettings();
        } else if ("water".equals(reminderType)) {
            setContentView(R.layout.activity_settings_water);
            initWaterSettings();
        } else if ("medication".equals(reminderType)) {
            setContentView(R.layout.activity_settings_medication);
            initMedicationSettings();
        }

        saveButton = findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(v -> saveSettings());
        } else {
            Log.e(TAG, "Save button is null");
        }
    }

    private void initMainSettings() {
        try {
            profileSettingsButton = findViewById(R.id.profileSettingsButton);
            notificationSettingsButton = findViewById(R.id.notificationSettingsButton);
            themeSettingsButton = findViewById(R.id.themeSettingsButton);

            if (profileSettingsButton != null) {
                profileSettingsButton.setOnClickListener(v -> openActivity(ProfileSettingsActivity.class));
            } else {
                Log.e(TAG, "Profile settings button is null");
            }

            if (notificationSettingsButton != null) {
                notificationSettingsButton.setOnClickListener(v -> openActivity(NotificationSettingsActivity.class));
            } else {
                Log.e(TAG, "Notification settings button is null");
            }

            if (themeSettingsButton != null) {
                themeSettingsButton.setOnClickListener(v -> openActivity(ThemeSettingsActivity.class));
            } else {
                Log.e(TAG, "Theme settings button is null");
            }

            Log.d(TAG, "Main settings initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing main settings", e);
        }
    }

    private void initWaterSettings() {
        try {
            timePicker = findViewById(R.id.timePicker);
            if (timePicker != null) {
                timePicker.setIs24HourView(true);
            }
            intervalEditText = findViewById(R.id.waterIntervalEditText);
            messageSpinner = findViewById(R.id.waterMessageSpinner);
            timePickerCheckbox = findViewById(R.id.timePickerCheckbox);
            intervalCheckbox = findViewById(R.id.intervalCheckbox);
            if (messageSpinner != null) {
                messageSpinner.setVisibility(View.VISIBLE);
            }
            loadWaterSettings();
            Log.d(TAG, "Water settings initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing water settings", e);
        }
    }

    private void initMedicationSettings() {
        try {
            timePicker = findViewById(R.id.timePicker);
            if (timePicker != null) {
                timePicker.setIs24HourView(true);
            }
            intervalEditText = findViewById(R.id.medicationIntervalEditText);
            messageSpinner = findViewById(R.id.medicationMessageSpinner);
            timePickerCheckbox = findViewById(R.id.timePickerCheckbox);
            intervalCheckbox = findViewById(R.id.intervalCheckbox);
            if (messageSpinner != null) {
                messageSpinner.setVisibility(View.VISIBLE);
            }
            loadMedicationSettings();
            Log.d(TAG, "Medication settings initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing medication settings", e);
        }
    }

    private void loadWaterSettings() {
        try {
            sharedPreferences = getSharedPreferences("ReminderPrefs", MODE_PRIVATE);
            int waterInterval = sharedPreferences.getInt("waterInterval", 30);
            int selectedMessageIndex = sharedPreferences.getInt("waterMessageIndex", 0);
            if (intervalEditText != null) {
                intervalEditText.setText(String.valueOf(waterInterval));
            }
            if (messageSpinner != null) {
                messageSpinner.setSelection(selectedMessageIndex);
            }
            Log.d(TAG, "Water settings loaded: " + waterInterval);
        } catch (Exception e) {
            Log.e(TAG, "Error loading water settings", e);
            Toast.makeText(this, "Ayarlar yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMedicationSettings() {
        try {
            sharedPreferences = getSharedPreferences("ReminderPrefs", MODE_PRIVATE);
            int medicationInterval = sharedPreferences.getInt("medicationInterval", 60);
            int selectedMessageIndex = sharedPreferences.getInt("medicationMessageIndex", 0);
            if (intervalEditText != null) {
                intervalEditText.setText(String.valueOf(medicationInterval));
            }
            if (messageSpinner != null) {
                messageSpinner.setSelection(selectedMessageIndex);
            }
            Log.d(TAG, "Medication settings loaded: " + medicationInterval);
        } catch (Exception e) {
            Log.e(TAG, "Error loading medication settings", e);
            Toast.makeText(this, "Ayarlar yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSettings() {
        try {
            if (intervalEditText == null) {
                Toast.makeText(this, "Interval EditText bulunamadı", Toast.LENGTH_SHORT).show();
                return;
            }
            String intervalText = intervalEditText.getText().toString();
            int newInterval = intervalText.isEmpty() ? 0 : Integer.parseInt(intervalText);

            int selectedMessageIndex = messageSpinner.getSelectedItemPosition();
            sharedPreferences = getSharedPreferences("ReminderPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean isTimePickerChecked = timePickerCheckbox != null && timePickerCheckbox.isChecked();
            boolean isIntervalChecked = intervalCheckbox != null && intervalCheckbox.isChecked();

            if (!isTimePickerChecked && !isIntervalChecked) {
                Toast.makeText(this, "Lütfen bir hatırlatma seçeneği belirleyin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isTimePickerChecked) {
                int hour;
                int minute;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if ("water".equals(reminderType)) {
                    editor.putInt("waterMessageIndex", selectedMessageIndex);
                    AlarmHelper.scheduleReminderWithExactTime(this, hour, minute, newInterval, selectedMessageIndex, "waterReminderChannel");
                } else if ("medication".equals(reminderType)) {
                    editor.putInt("medicationMessageIndex", selectedMessageIndex);
                    AlarmHelper.scheduleReminderWithExactTime(this, hour, minute, newInterval, selectedMessageIndex, "medicationReminderChannel");
                }
            }

            if (isIntervalChecked) {
                if (newInterval <= 0) {
                    Toast.makeText(this, "Lütfen pozitif bir sayı girin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("water".equals(reminderType)) {
                    editor.putInt("waterInterval", newInterval);
                    editor.putInt("waterMessageIndex", selectedMessageIndex);
                    AlarmHelper.scheduleReminder(this, newInterval, "waterReminderChannel");
                } else if ("medication".equals(reminderType)) {
                    editor.putInt("medicationInterval", newInterval);
                    editor.putInt("medicationMessageIndex", selectedMessageIndex);
                    AlarmHelper.scheduleReminder(this, newInterval, "medicationReminderChannel");
                }
            }

            editor.apply();
            Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show();
            finish(); // Ayarlar kaydedildikten sonra bu aktiviteyi kapat
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid number format", e);
            Toast.makeText(this, "Geçerli bir sayı girin", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error saving settings", e);
            Toast.makeText(this, "Ayarlar kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void openActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
            Log.d(TAG, "Opened activity: " + activityClass.getSimpleName());
        } catch (Exception e) {
            Log.e(TAG, "Error opening activity: " + activityClass.getSimpleName(), e);
        }
    }
}
