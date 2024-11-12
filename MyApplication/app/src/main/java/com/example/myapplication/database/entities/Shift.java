package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "Shift")
public class Shift {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ShiftID")
    private int shiftId;

    @ColumnInfo(name = "ShiftType")
    private String shiftType;

    @ColumnInfo(name = "TimeStart")
    private String timeStart;

    @ColumnInfo(name = "TimeEnd")
    private String timeEnd;


    // Constructor
    public Shift(String shiftType, String timeStart, String timeEnd) {
        this.shiftType = shiftType;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }


    // Getters and Setters
    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
