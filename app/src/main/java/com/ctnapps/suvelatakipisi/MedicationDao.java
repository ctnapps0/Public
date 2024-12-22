package com.ctnapps.suvelatakipisi;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MedicationDao {

    @Insert
    void insert(Medication medication);

    @Query("SELECT * FROM medication WHERE timestamp BETWEEN :startDate AND :endDate")
    List<Medication> getMedicationIntakeBetweenDates(long startDate, long endDate);
}
