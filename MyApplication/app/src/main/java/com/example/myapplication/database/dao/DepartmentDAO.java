package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.myapplication.database.entities.Department;


@Dao
public interface DepartmentDAO {

    @Insert
    void insertDepartment(Department department);

    @Query("SELECT * FROM Department")
    List<Department> getDepartmentList();

}
