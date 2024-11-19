package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Workplace;

import java.util.List;

@Dao
public interface WorkplaceDAO {
        @Insert
        void insert(Workplace workplace);

        @Update
        void update(Workplace workplace);

        @Delete
        void delete(Workplace workplace);

        @Query("SELECT * FROM Workplace")
        List<Workplace> getAll();
}
