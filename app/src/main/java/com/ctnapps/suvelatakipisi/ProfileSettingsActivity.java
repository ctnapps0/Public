package com.ctnapps.suvelatakipisi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, ageEditText;
    private Button saveProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        ageEditText = findViewById(R.id.ageEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        loadProfileSettings();

        saveProfileButton.setOnClickListener(v -> {
            saveProfileSettings();
            Toast.makeText(this, "Profil bilgileri kaydedildi", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProfileSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        nameEditText.setText(sharedPreferences.getString("name", ""));
        emailEditText.setText(sharedPreferences.getString("email", ""));
        ageEditText.setText(sharedPreferences.getString("age", ""));
    }

    private void saveProfileSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("age", ageEditText.getText().toString());
        editor.apply();
    }
}
