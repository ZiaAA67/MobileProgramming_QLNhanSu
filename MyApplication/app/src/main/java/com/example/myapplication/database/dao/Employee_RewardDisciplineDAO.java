package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Employee_RewardDiscipline;

import java.util.List;

@Dao
public interface Employee_RewardDisciplineDAO {

    @Insert
    void insert(Employee_RewardDiscipline... employeeRewardDisciplines);

    @Update
    void update(Employee_RewardDiscipline employeeRewardDiscipline);

    @Delete
    void delete(Employee_RewardDiscipline employeeRewardDiscipline);

    @Query("SELECT * FROM Employee_RewardDiscipline")
    List<Employee_RewardDiscipline> getAll();

    @Query("SELECT * FROM Employee_RewardDiscipline WHERE EmployeeID = :employeeId")
    List<Employee_RewardDiscipline> getByEmployeeId(int employeeId);

    @Query("SELECT * FROM Employee_RewardDiscipline WHERE EmployeeID = :employeeId AND substr(Date, 4, 7) = :monthYear")
    List<Employee_RewardDiscipline> getByEmployeeIdAndMonthYear(int employeeId, String monthYear);
}
