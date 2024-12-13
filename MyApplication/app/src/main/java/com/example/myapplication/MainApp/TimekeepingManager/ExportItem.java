package com.example.myapplication.MainApp.TimekeepingManager;

public class ExportItem {

    private int employeeID;
    private String fullname;
    private String department;
    private String position;
    private String date;
    private String timeIn;
    private String timeOut;
    private String overTime;
    private String shiftName;

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public ExportItem(int employeeID, String fullname, String department, String position, String date, String timeIn, String timeOut, String overTime, String shiftName) {
        this.employeeID = employeeID;
        this.fullname = fullname;
        this.department = department;
        this.position = position;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.overTime = overTime;
        this.shiftName = shiftName;
    }

    public ExportItem() {
    }
}
