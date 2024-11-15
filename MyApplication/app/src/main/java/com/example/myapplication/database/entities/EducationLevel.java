package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EducationLevel")
public class EducationLevel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "EducationID")
    private int educationId;

    @ColumnInfo(name = "EducationLevelName")
    private String educationLevelName;

    @ColumnInfo(name = "Major")
    private String major;

    @ColumnInfo(name = "Institute")
    private String institute;


    // Constructor
    public EducationLevel(String educationLevelName, String major, String institute) {
        this.educationLevelName = educationLevelName;
        this.major = major;
        this.institute = institute;
    }

    // Getters and Setters
    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int educationId) {
        this.educationId = educationId;
    }

    public String getEducationLevelName() {
        return educationLevelName;
    }

    public void setEducationLevelName(String educationLevelName) {
        this.educationLevelName = educationLevelName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
