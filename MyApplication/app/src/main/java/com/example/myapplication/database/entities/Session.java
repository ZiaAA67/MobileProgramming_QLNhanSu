package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Session",
        foreignKeys = @ForeignKey(
                entity = Shift.class,
                parentColumns = "ShiftID",
                childColumns = "ShiftID",
                onDelete = ForeignKey.SET_NULL
        )
)
public class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SessionID")
    private int sessionId;

    @ColumnInfo(name = "Day")
    private int day;

    @ColumnInfo(name = "Month")
    private int month;

    @ColumnInfo(name = "Year")
    private int year;

    @ColumnInfo(name = "isHoliday")
    private boolean isHoliday;

    @ColumnInfo(name = "ShiftID")
    private int shiftId;


    // Constructor
    public Session(int day, int month, int year, boolean isHoliday, int shiftId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isHoliday = isHoliday;
        this.shiftId = shiftId;
    }


    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }
}
