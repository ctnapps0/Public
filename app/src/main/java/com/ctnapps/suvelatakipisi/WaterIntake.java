package com.ctnapps.suvelatakipisi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "water_intake")
public class WaterIntake {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long timestamp;
    public int amount;

    public WaterIntake(long timestamp, int amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }
}
