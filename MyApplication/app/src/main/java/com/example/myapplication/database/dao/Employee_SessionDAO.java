package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.database.entities.Employee_Session;

import java.util.List;

@Dao
public interface Employee_SessionDAO {

    @Insert
    void insert(Employee_Session... employeeSessions);

    @Delete
    void delete(Employee_Session employeeSession);

    @Query("SELECT * FROM Employee_Session")
    List<Employee_Session> getAll();

    @Query("SELECT * FROM Employee_Session WHERE EmployeeID = :employeeId AND SessionID = :sessionId")
    Employee_Session getByEmployeeAndSession(int employeeId, int sessionId);

    // Lấy danh sách các EmployeeID theo SessionID
    @Query("SELECT EmployeeID FROM Employee_Session WHERE SessionID = :sessionId")
    List<Integer> getEmployeeIdsBySessionId(int sessionId);

    // Lấy danh sách các SessionID theo EmployeeID
    @Query("SELECT SessionID FROM Employee_Session WHERE EmployeeID = :employeeId")
    List<Integer> getSessionIdsByEmployeeId(int employeeId);

    @Query("SELECT * FROM Employee_Session WHERE EmployeeID = :employeeId")
    List<Employee_Session> getSessionByEmployeeId(int employeeId);
}
