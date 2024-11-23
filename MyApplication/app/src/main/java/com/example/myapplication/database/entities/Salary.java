package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SalarySlip")
public class Salary {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "SalaryID")
    private int salaryId;

    // Luong co ban
    @ColumnInfo(name = "BasicSalary")
    private Float basicSalary;

    // Tro cap
    @ColumnInfo(name = "Allowance")
    private Float allowance;

    // He so
    @ColumnInfo(name = "Coefficient")
    private Float coefficient;

    // Constructor
    public Salary(Float basicSalary, Float allowance, Float coefficient) {
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.coefficient = coefficient;
    }

    // Getters and Setters
    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public Float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Float getAllowance() {
        return allowance;
    }

    public void setAllowance(Float allowance) {
        this.allowance = allowance;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }
}
