package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.EducationLevel;

import java.util.List;

@Dao
public interface EducationLevelDAO {

    @Insert
    void insert(EducationLevel... educationLevels);

    @Insert
    long insertReturnId(EducationLevel educationLevel);

    @Update
    void update(EducationLevel educationLevel);

    @Delete
    void delete(EducationLevel educationLevel);

    @Query("SELECT * FROM EducationLevel")
    List<EducationLevel> getAll();

    @Query("SELECT * FROM EducationLevel WHERE EducationID = :educationId")
    EducationLevel getById(int educationId);
}
