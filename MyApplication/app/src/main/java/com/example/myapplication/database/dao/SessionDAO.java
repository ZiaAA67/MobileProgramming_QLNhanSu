package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Session;

import java.util.List;

@Dao
public interface SessionDAO {

    @Insert
    void insert(Session session);

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);

    @Query("SELECT * FROM Session")
    List<Session> getAllSessions();

    @Query("SELECT * FROM Session WHERE SessionID = :sessionId")
    Session getSessionById(int sessionId);

    @Query("SELECT * FROM Session WHERE Month = :month AND Year = :year")
    List<Session> getSessionsByMonth(int month, int year);
}
