package com.ctnapps.suvelatakipisi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "frequent_medication")
public class FrequentMedication {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String medicationName;

    public FrequentMedication(String medicationName) {
        this.medicationName = medicationName;
    }
}
