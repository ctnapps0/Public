package com.ctnapps.suvelatakipisi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FrequentMedicationDao {

    @Insert
    void insert(FrequentMedication medication);

    @Delete
    void delete(FrequentMedication medication);

    @Query("SELECT * FROM frequent_medication")
    List<FrequentMedication> getAllFrequentMedications();

    @Query("SELECT * FROM frequent_medication WHERE medicationName = :name LIMIT 1")
    FrequentMedication findByName(String name);
}
