package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Timekeeping;

import java.util.List;

@Dao
public interface TimekeepingDAO {

    @Insert
    void insert(Timekeeping timekeeping);

    @Insert
    void insertAll(List<Timekeeping> timekeepings);

    @Update
    void update(Timekeeping timekeeping);

    @Delete
    void delete(Timekeeping timekeeping);

    @Query("DELETE FROM Timekeeping WHERE TimekeepingID = :timekeepingId")
    void deleteById(int timekeepingId);

    @Query("SELECT * FROM Timekeeping")
    List<Timekeeping> getAllTimekeeping();

    @Query("SELECT * FROM Timekeeping WHERE TimekeepingID = :timekeepingId")
    Timekeeping getTimekeepingById(int timekeepingId);

    @Query("SELECT * FROM Timekeeping WHERE SessionID = :sessionId")
    List<Timekeeping> getTimekeepingBySessionId(int sessionId);

    @Query("SELECT * FROM Timekeeping WHERE EmployeeID = :employeeId")
    List<Timekeeping> getTimekeepingByEmployeeId(int employeeId);

    @Query("SELECT * FROM Timekeeping WHERE IsAbsent = 1")
    List<Timekeeping> getAbsentTimekeeping();

    @Query("SELECT * FROM Timekeeping WHERE Overtime > :minOvertime")
    List<Timekeeping> getTimekeepingWithOvertime(int minOvertime);

    @Query("SELECT COUNT(*) FROM Timekeeping WHERE SessionID = :sessionId")
    int countTimekeepingBySessionId(int sessionId);

    @Query("SELECT COUNT(*) FROM Timekeeping WHERE EmployeeID = :employeeId AND IsAbsent = 1")
    int countAbsentByEmployeeId(int employeeId);

    @Query("SELECT * FROM Timekeeping WHERE TimeIn BETWEEN :startTime AND :endTime")
    List<Timekeeping> getTimekeepingInTimeRange(String startTime, String endTime);

    @Query("SELECT * FROM Timekeeping WHERE SessionID = :sessionId AND EmployeeID = :employeeId")
    List<Timekeeping> getTimekeepingBySessionIdAndEmployeeId(int sessionId, int employeeId);
}
