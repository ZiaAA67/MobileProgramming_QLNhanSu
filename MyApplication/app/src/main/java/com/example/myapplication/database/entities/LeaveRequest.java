package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
    private String offDateFrom;

    @ColumnInfo(name = "OffDateTo")
    private String offDateTo;

    @ColumnInfo(name = "Status")
    private int status;

    @ColumnInfo(name = "EmployeeID")
    private Integer employeeId;

    //Constructor
    public LeaveRequest(String reason, String sendDate, String offDateFrom, String offDateTo, int status, Integer employeeId) {
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

    public String getOffDateFrom() {
        return offDateFrom;
    }

    public void setOffDateFrom(String offDateFrom) {
        this.offDateFrom = offDateFrom;
    }

    public String getOffDateTo() {
        return offDateTo;
    }

    public void setOffDateTo(String offDateTo) {
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

    public long calculateLeaveDays() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate = LocalDate.parse(offDateFrom, formatter);
            LocalDate endDate = LocalDate.parse(offDateTo, formatter);

            // Nếu startDate bằng endDate, trả về 1 ngày
            if (startDate.isEqual(endDate)) {
                return 1;
            }

            // Tính số ngày nghỉ, bao gồm cả ngày bắt đầu và ngày kết thúc
            return ChronoUnit.DAYS.between(startDate, endDate) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Trả về 0 nếu có lỗi
        }
    }

}
