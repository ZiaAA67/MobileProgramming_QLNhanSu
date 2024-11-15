package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.LeaveRequest;

import java.util.List;

@Dao
public interface LeaveRequestDAO {

    @Insert
    void insert(LeaveRequest... leaveRequests);

    @Update
    void update(LeaveRequest leaveRequest);

    @Delete
    void delete(LeaveRequest leaveRequest);

    @Query("SELECT * FROM LeaveRequest")
    List<LeaveRequest> getAll();

    @Query("SELECT * FROM LeaveRequest WHERE LeaveRequestID = :leaveRequestId")
    LeaveRequest getById(int leaveRequestId);

    @Query("SELECT * FROM LeaveRequest WHERE EmployeeID = :employeeId")
    List<LeaveRequest> getByEmployeeId(int employeeId);

    @Query("SELECT * FROM LeaveRequest WHERE Status = :status")
    List<LeaveRequest> getByStatus(int status);

    @Query("SELECT * FROM LeaveRequest WHERE SendDate BETWEEN :startDate AND :endDate")
    List<LeaveRequest> getBySendDateRange(String startDate, String endDate);
}
