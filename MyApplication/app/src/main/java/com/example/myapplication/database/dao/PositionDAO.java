package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Position;

import java.util.List;

@Dao
public interface PositionDAO {

    @Insert
    void insert(Position position);

    @Update
    void update(Position position);

    @Delete
    void delete(Position position);

    @Query("SELECT * FROM Position WHERE PositionID = :positionId")
    Position getPositionById(int positionId);

    @Query("SELECT * FROM Position")
    List<Position> getAll();
}
