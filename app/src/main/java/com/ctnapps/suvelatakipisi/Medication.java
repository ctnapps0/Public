package com.ctnapps.suvelatakipisi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medication")
public class Medication {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long timestamp;
    public String medicationName;

    public Medication(long timestamp, String medicationName) {
        this.timestamp = timestamp;
        this.medicationName = medicationName;
    }
}
