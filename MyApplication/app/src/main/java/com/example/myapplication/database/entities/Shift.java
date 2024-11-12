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
    private LocalTime timeStart;

    @ColumnInfo(name = "TimeEnd")
    private LocalTime timeEnd;


    // Constructor
    public Shift(String shiftType, LocalTime timeStart, LocalTime timeEnd) {
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

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}
