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

    @Update
    void update(Timekeeping timekeeping);

    @Delete
    void delete(Timekeeping timekeeping);

    @Query("SELECT * FROM Timekeeping")
    List<Timekeeping> getAllTimekeeping();

    @Query("SELECT * FROM Timekeeping WHERE TimekeepingID = :timekeepingId")
    Timekeeping getTimekeepingById(int timekeepingId);

    @Query("SELECT * FROM Timekeeping WHERE SessionID = :sessionId")
    List<Timekeeping> getTimekeepingBySessionId(int sessionId);

    @Query("SELECT * FROM Timekeeping WHERE IsAbsent = 1")
    List<Timekeeping> getAbsentTimekeeping();
}
