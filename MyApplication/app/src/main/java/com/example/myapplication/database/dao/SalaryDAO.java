package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Salary;

import java.util.List;

@Dao
public interface SalaryDAO {

    @Insert
    long insert(Salary salary);

    @Update
    void update(Salary salary);

    @Delete
    void delete(Salary salary);

    @Query("SELECT * FROM SalarySlip")
    List<Salary> getAllSalaries();

    @Query("SELECT * FROM SalarySlip WHERE SalaryID = :salaryId")
    Salary getSalaryById(int salaryId);
}
