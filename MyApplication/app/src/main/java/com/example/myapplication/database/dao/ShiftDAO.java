package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Shift;

import java.util.List;

@Dao
public interface ShiftDAO {

    @Insert
    void insert(Shift shift);

    @Update
    void update(Shift shift);

    @Delete
    void delete(Shift shift);

    @Query("SELECT * FROM Shift")
    List<Shift> getAllShifts();

    @Query("SELECT * FROM Shift WHERE ShiftID = :shiftId")
    Shift getShiftById(int shiftId);

    @Query("SELECT * FROM Shift WHERE ShiftType = :shiftType")
    List<Shift> getShiftsByType(String shiftType);
}
