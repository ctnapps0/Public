package com.ctnapps.suvelatakipisi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private Button waterButton100ml, waterButton250ml, waterButton500ml, waterButton1ltr, medicationButton, statsButton;
    private EditText medicationNameEditText;
    private ListView medicationListView;
    private AppDatabase db;
    private List<String> medicationList;
    private ArrayAdapter<String> medicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        waterButton100ml = findViewById(R.id.waterButton100ml);
        waterButton250ml = findViewById(R.id.waterButton250ml);
        waterButton500ml = findViewById(R.id.waterButton500ml);
        waterButton1ltr = findViewById(R.id.waterButton1ltr);
        medicationButton = findViewById(R.id.medicationButton);
        statsButton = findViewById(R.id.statsButton); // İstatistik butonu
        medicationNameEditText = findViewById(R.id.medicationNameEditText);
        medicationListView = findViewById(R.id.medicationListView);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "suvelatakipisi.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // Sık kullanılan ilaçları yükleyin ve listeye ekleyin
        loadMedications();

        waterButton100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordWaterIntake(100);
            }
        });

        waterButton250ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordWaterIntake(250);
            }
        });

        waterButton500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordWaterIntake(500);
            }
        });

        waterButton1ltr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordWaterIntake(1000);
            }
        });

        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicationName = medicationNameEditText.getText().toString();
                if (!medicationName.isEmpty()) {
                    recordMedication(medicationName);
                } else {
                    Toast.makeText(FeedbackActivity.this, "Lütfen ilaç adını girin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        medicationListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String medicationName = medicationList.get(position);
            deleteMedication(medicationName);
            return true;
        });
    }

    private void recordWaterIntake(int amount) {
        long currentTime = System.currentTimeMillis();
        WaterIntake waterIntake = new WaterIntake(currentTime, amount);
        db.waterIntakeDao().insert(waterIntake);
        Toast.makeText(this, "Su içme kaydı eklendi", Toast.LENGTH_SHORT).show();
    }

    private void recordMedication(String medicationName) {
        long currentTime = System.currentTimeMillis();
        Medication medication = new Medication(currentTime, medicationName);
        db.medicationDao().insert(medication);

        // İlaç listesine ekle, ancak tekrarları önlemek için
        if (db.frequentMedicationDao().findByName(medicationName) == null) {
            medicationList.add(medicationName);
            medicationAdapter.notifyDataSetChanged();
            // Sık kullanılan ilaçlar tablosuna ekleyin
            FrequentMedication frequentMedication = new FrequentMedication(medicationName);
            db.frequentMedicationDao().insert(frequentMedication);
        }

        Toast.makeText(this, "İlaç kaydı eklendi", Toast.LENGTH_SHORT).show();
    }

    private void loadMedications() {
        medicationList = new ArrayList<>();
        List<FrequentMedication> frequentMedications = db.frequentMedicationDao().getAllFrequentMedications();

        for (FrequentMedication medication : frequentMedications) {
            medicationList.add(medication.medicationName);
        }

        medicationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationList);
        medicationListView.setAdapter(medicationAdapter);
    }

    private void deleteMedication(String medicationName) {
        FrequentMedication medicationToDelete = db.frequentMedicationDao().findByName(medicationName);
        if (medicationToDelete != null) {
            db.frequentMedicationDao().delete(medicationToDelete);
            medicationList.remove(medicationName);
            medicationAdapter.notifyDataSetChanged();
            Toast.makeText(this, "İlaç kaydı silindi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "İlaç bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }
}
