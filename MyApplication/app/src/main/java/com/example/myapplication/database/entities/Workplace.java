package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Workplace")
public class Workplace implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "WorkplaceID")
    private int workplaceId;

    @ColumnInfo(name = "WorkplaceName")
    private String workplaceName;

    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "Active")
    private boolean active;

    @ColumnInfo(name = "Latitude")
    private Double latitude;

    @ColumnInfo(name = "Longitude")
    private Double longitude;


    public Workplace(String workplaceName, String address, boolean active, Double latitude, Double longitude) {
        this.workplaceName = workplaceName;
        this.address = address;
        this.active = active;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
