package com.ctnapps.suvelatakipisi;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticsActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView dailyWaterIntake, weeklyWaterIntake, monthlyWaterIntake, dailyMedicationIntake, weeklyMedicationIntake, monthlyMedicationIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dailyWaterIntake = findViewById(R.id.dailyWaterIntake);
        weeklyWaterIntake = findViewById(R.id.weeklyWaterIntake);
        monthlyWaterIntake = findViewById(R.id.monthlyWaterIntake);
        dailyMedicationIntake = findViewById(R.id.dailyMedicationIntake);
        weeklyMedicationIntake = findViewById(R.id.weeklyMedicationIntake);
        monthlyMedicationIntake = findViewById(R.id.monthlyMedicationIntake);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "suvelatakipisi.db")
                .allowMainThreadQueries()
                .build();

        displayStatistics();
    }

    private void displayStatistics() {
        long currentTime = System.currentTimeMillis();

        // Günlük, Haftalık, Aylık Su İçme Miktarları
        dailyWaterIntake.setText("Günlük Su Tüketimi: " + getWaterIntake(currentTime, 1) + " mL");
        weeklyWaterIntake.setText("Haftalık Su Tüketimi: " + getWaterIntake(currentTime, 7) + " mL");
        monthlyWaterIntake.setText("Aylık Su Tüketimi: " + getWaterIntake(currentTime, 30) + " mL");

        // Günlük, Haftalık, Aylık İlaç Tüketimleri
        dailyMedicationIntake.setText("Günlük İlaç Tüketimi: " + getMedicationIntake(currentTime, 1) + " adet");
        weeklyMedicationIntake.setText("Haftalık İlaç Tüketimi: " + getMedicationIntake(currentTime, 7) + " adet");
        monthlyMedicationIntake.setText("Aylık İlaç Tüketimi: " + getMedicationIntake(currentTime, 30) + " adet");
    }

    private int getWaterIntake(long currentTime, int days) {
        long pastTime = currentTime - TimeUnit.DAYS.toMillis(days);
        List<WaterIntake> waterIntakes = db.waterIntakeDao().getWaterIntakeBetweenDates(pastTime, currentTime);
        int totalIntake = 0;
        for (WaterIntake waterIntake : waterIntakes) {
            totalIntake += waterIntake.amount;
        }
        return totalIntake;
    }

    private int getMedicationIntake(long currentTime, int days) {
        long pastTime = currentTime - TimeUnit.DAYS.toMillis(days);
        List<Medication> medications = db.medicationDao().getMedicationIntakeBetweenDates(pastTime, currentTime);
        return medications.size();
    }
}
