package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "LeaveRequest",
        foreignKeys = @ForeignKey(
                entity = Employee.class,
                parentColumns = "EmployeeID",
                childColumns = "EmployeeID",
                onDelete = ForeignKey.SET_NULL
        )
)
public class LeaveRequest {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "LeaveRequestID")
    private int leaveRequestId;

    @ColumnInfo(name = "Reason")
    private String reason;

    @ColumnInfo(name = "SendDate")
    private String sendDate;

    @ColumnInfo(name = "OffDateFrom")
    private LocalDate offDateFrom;

    @ColumnInfo(name = "OffDateTo")
    private LocalDate offDateTo;

    @ColumnInfo(name = "Status")
    private int status;

    @ColumnInfo(name = "EmployeeID")
    private Integer employeeId;

    //Constructor
    public LeaveRequest(String reason, String sendDate, LocalDate offDateFrom, LocalDate offDateTo, int status, Integer employeeId) {
        this.reason = reason;
        this.sendDate = sendDate;
        this.offDateFrom = offDateFrom;
        this.offDateTo = offDateTo;
        this.status = status;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public int getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(int leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public LocalDate getOffDateFrom() {
        return offDateFrom;
    }

    public void setOffDateFrom(LocalDate offDateFrom) {
        this.offDateFrom = offDateFrom;
    }

    public LocalDate getOffDateTo() {
        return offDateTo;
    }

    public void setOffDateTo(LocalDate offDateTo) {
        this.offDateTo = offDateTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
