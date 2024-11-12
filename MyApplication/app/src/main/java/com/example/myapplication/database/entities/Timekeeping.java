package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

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
    private LocalTime timeIn;

    @ColumnInfo(name = "TimeOut")
    private LocalTime timeOut;

    @ColumnInfo(name = "IsAbsent")
    private Integer isAbsent;

    @ColumnInfo(name = "Overtime")
    private LocalTime overtime;

    @ColumnInfo(name = "SessionID")
    private int sessionId;


    // Constructor
    public Timekeeping(LocalTime timeIn, LocalTime timeOut, Integer isAbsent, LocalTime overtime, int sessionId) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.isAbsent = isAbsent;
        this.overtime = overtime;
        this.sessionId = sessionId;
    }


    // Getters and Setters
    public int getTimekeepingId() {
        return timekeepingId;
    }

    public void setTimekeepingId(int timekeepingId) {
        this.timekeepingId = timekeepingId;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getIsAbsent() {
        return isAbsent;
    }

    public void setIsAbsent(Integer isAbsent) {
        this.isAbsent = isAbsent;
    }

    public LocalTime getOvertime() {
        return overtime;
    }

    public void setOvertime(LocalTime overtime) {
        this.overtime = overtime;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
