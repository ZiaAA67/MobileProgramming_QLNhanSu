package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDate;

@Entity(tableName = "Employee_RewardDiscipline",
        primaryKeys = {"EmployeeID", "RewardDisciplineID", "Date"},
        foreignKeys = {
                @ForeignKey(entity = Employee.class,
                        parentColumns = "EmployeeID",
                        childColumns = "EmployeeID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = RewardDiscipline.class,
                        parentColumns = "RewardDisciplineID",
                        childColumns = "RewardDisciplineID",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Employee_RewardDiscipline {

    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "RewardDisciplineID")
    private int rewardDisciplineId;

    @ColumnInfo(name = "Date")
    private LocalDate date;

    @ColumnInfo(name = "Bonus")
    private Float bonus;


    // Constructor
    public Employee_RewardDiscipline(int rewardDisciplineId, LocalDate date, Float bonus) {
        this.rewardDisciplineId = rewardDisciplineId;
        this.date = date;
        this.bonus = bonus;
    }


    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getRewardDisciplineId() {
        return rewardDisciplineId;
    }

    public void setRewardDisciplineId(int rewardDisciplineId) {
        this.rewardDisciplineId = rewardDisciplineId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }
}