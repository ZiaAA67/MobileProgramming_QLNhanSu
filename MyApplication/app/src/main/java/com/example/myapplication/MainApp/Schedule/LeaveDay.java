package com.example.myapplication.MainApp.Schedule;

public class LeaveDay {
    private int day;
    private int month;
    private int year;
    private String employeeName;
    private String leaveType; // "short" hoặc "long"

    public LeaveDay(int day, int month, int year, String employeeName, String leaveType) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getLeaveType() {
        return leaveType;
    }
}
