package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Timekeeping",
        foreignKeys = @ForeignKey(
                entity = Session.class,
                parentColumns = "SessionID",
                childColumns = "SessionID",
                onDelete = ForeignKey.SET_NULL
        )
)
public class Timekeeping {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "TimekeepingID")
    private int timekeepingId;

    @ColumnInfo(name = "TimeIn")
    private String timeIn;

    @ColumnInfo(name = "TimeOut")
    private String timeOut;

    @ColumnInfo(name = "IsAbsent")
    private Integer isAbsent;

    @ColumnInfo(name = "Overtime")
    private int overtime;

    @ColumnInfo(name = "SessionID")
    private int sessionId;

    // Constructor
    public Timekeeping(String timeIn, String timeOut, Integer isAbsent, int overtime, int sessionId) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.isAbsent = isAbsent;
        this.overtime = overtime;
        this.sessionId = sessionId;
    }

    public Timekeeping() {
    }

    // Getters and Setters
    public int getTimekeepingId() {
        return timekeepingId;
    }

    public void setTimekeepingId(int timekeepingId) {
        this.timekeepingId = timekeepingId;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getIsAbsent() {
        return isAbsent;
    }

    public void setIsAbsent(Integer isAbsent) {
        this.isAbsent = isAbsent;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}