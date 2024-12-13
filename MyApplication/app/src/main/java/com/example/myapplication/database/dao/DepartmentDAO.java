package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.myapplication.database.entities.Department;


@Dao
public interface DepartmentDAO {

    @Insert
    void insert(Department... departments);

    @Update
    void update(Department department);

    @Delete
    void delete(Department department);

    @Query("SELECT * FROM Department WHERE Active = 1")
    List<Department> getActiveDepartment();

    @Query("SELECT * FROM Department WHERE DepartmentID = :departmentId")
    Department getById(int departmentId);

    @Query("SELECT * FROM Department WHERE DepartmentName = :departmentName LIMIT 1")
    Department getByName(String departmentName);

    @Query("SELECT * FROM Department WHERE LOWER(DepartmentName) = LOWER(:departmentName)")
    List<Department> getListDepartmentByName(String departmentName);

    @Query("SELECT COUNT(*) FROM Department WHERE Active = 1")
    long getQuantityDepartment();
}
