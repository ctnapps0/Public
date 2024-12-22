package com.ctnapps.suvelatakipisi;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WaterIntake.class, Medication.class, FrequentMedication.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WaterIntakeDao waterIntakeDao();
    public abstract MedicationDao medicationDao();
    public abstract FrequentMedicationDao frequentMedicationDao();

    // Migration tanımlama
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Buraya yeni tablo ekleyebilir veya mevcut tablolarda değişiklik yapabilirsiniz.
        }
    };
}
