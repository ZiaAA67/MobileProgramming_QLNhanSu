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
    long insert(Session session);

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);

    @Query("SELECT * FROM Session")
    List<Session> getAllSessions();

    @Query("SELECT * FROM Session WHERE SessionID = :sessionId")
    Session getSessionById(int sessionId);

    @Query("SELECT * FROM Session WHERE Day = :day AND Month = :month AND Year = :year")
    List<Session> getSessionByDayMonthYear(int day, int month, int year);

    @Query("SELECT * FROM Session WHERE Month = :month AND Year = :year")
    List<Session> getSessionsByMonth(int month, int year);

    @Query("SELECT * FROM session WHERE (year > :yearFrom OR (year = :yearFrom AND month > :monthFrom) OR (year = :yearFrom AND month = :monthFrom AND day >= :dayFrom)) " +
            "AND (year < :yearTo OR (year = :yearTo AND month < :monthTo) OR (year = :yearTo AND month = :monthTo AND day <= :dayTo))")
    List<Session> getSessionsBetweenDates(int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo);

}
