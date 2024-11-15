package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Workplace")
public class Workplace {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "WorkplaceID")
    private int workplaceId;

    @ColumnInfo(name = "WorkplaceName")
    private String workplaceName;

    @ColumnInfo(name = "Latitude")
    private double latitude;

    @ColumnInfo(name = "Longitude")
    private double longitude;


    public Workplace(String workplaceName, double latitude, double longitude) {
        this.workplaceName = workplaceName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(int workplaceId) {
        this.workplaceId = workplaceId;
    }

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
