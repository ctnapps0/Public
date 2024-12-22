package com.ctnapps.suvelatakipisi;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaterIntakeDao {

    @Insert
    void insert(WaterIntake waterIntake);

    @Query("SELECT * FROM water_intake WHERE timestamp BETWEEN :startDate AND :endDate")
    List<WaterIntake> getWaterIntakeBetweenDates(long startDate, long endDate);
}
