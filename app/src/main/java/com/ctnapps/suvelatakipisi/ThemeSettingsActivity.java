package com.ctnapps.suvelatakipisi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeSettingsActivity extends AppCompatActivity {

    private static final String TAG = "ThemeSettingsActivity";
    private RadioGroup themeRadioGroup;
    private RadioButton lightThemeRadioButton, darkThemeRadioButton;
    private Button saveThemeButton;
    private int currentThemeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Temayı onCreate çağrısından önce uygula
        applySavedTheme();

        setContentView(R.layout.activity_theme_settings);

        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        lightThemeRadioButton = findViewById(R.id.lightThemeRadioButton);
        darkThemeRadioButton = findViewById(R.id.darkThemeRadioButton);
        saveThemeButton = findViewById(R.id.saveThemeButton);

        loadThemeSettings();

        saveThemeButton.setOnClickListener(v -> {
            int selectedThemeIndex = themeRadioGroup.getCheckedRadioButtonId() == R.id.darkThemeRadioButton ? 1 : 0;
            if (currentThemeIndex != selectedThemeIndex) {
                saveThemeSettings(selectedThemeIndex);
                Toast.makeText(this, "Tema ayarları kaydedildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Aynı tema zaten seçili", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadThemeSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        currentThemeIndex = sharedPreferences.getInt("selected_theme", 0);

        if (currentThemeIndex == 1) {
            darkThemeRadioButton.setChecked(true);
        } else {
            lightThemeRadioButton.setChecked(true);
        }

        Log.d(TAG, "Loaded theme index: " + currentThemeIndex);
    }

    private void saveThemeSettings(int selectedThemeIndex) {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selected_theme", selectedThemeIndex);
        editor.apply();

        applyTheme(selectedThemeIndex);
    }

    private void applyTheme(int themeIndex) {
        if (themeIndex != currentThemeIndex) {
            currentThemeIndex = themeIndex;
            Log.d(TAG, "Applying theme index: " + themeIndex);
            switch (themeIndex) {
                case 1:
                    setTheme(R.style.Theme_App_Dark);
                    break;
                default:
                    setTheme(R.style.Theme_App_Light);
                    break;
            }
            recreate(); // Aktiviteyi yeniden oluştur, böylece tema değişikliği uygulanır
        }
    }

    private void applySavedTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int themeIndex = sharedPreferences.getInt("selected_theme", 0);
        currentThemeIndex = themeIndex; // Mevcut temayı kaydet
        applyTheme(themeIndex);
    }
}
