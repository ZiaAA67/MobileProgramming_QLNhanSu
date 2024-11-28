package com.example.myapplication.MainApp.Salary;

public class SalarySlipItem {
    private int month;
    private int year;

    public SalarySlipItem(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Phiếu lương tháng " + month + " năm " + year;
    }
}
