package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Department")
public class Department {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "DepartmentID")
    private int departmentId;

    @ColumnInfo(name = "DepartmentName")
    private String departmentName;

    @ColumnInfo(name = "Active")
    private boolean active;

    @ColumnInfo(name = "Description")
    private String description;

    // Constructor
    public Department(String departmentName, boolean active, String description) {
        this.departmentName = departmentName;
        this.active = active;
        this.description = description;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
