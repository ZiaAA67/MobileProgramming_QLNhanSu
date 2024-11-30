package com.example.myapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.RewardDiscipline;

import java.util.List;

@Dao
public interface RewardDisciplineDAO {

    @Insert
    void insert(RewardDiscipline rewardDiscipline);

    @Update
    void update(RewardDiscipline rewardDiscipline);

    @Delete
    void delete(RewardDiscipline rewardDiscipline);

    @Query("SELECT * FROM RewardDiscipline")
    List<RewardDiscipline> getAllRewardDisciplines();

    @Query("SELECT * FROM RewardDiscipline WHERE RewardDisciplineID = :rewardDisciplineId")
    RewardDiscipline getRewardDisciplineById(int rewardDisciplineId);

    @Query("SELECT * FROM RewardDiscipline WHERE type = :type")
    List<RewardDiscipline> getRewardDisciplineByType(int type);

    @Query("SELECT * FROM RewardDiscipline WHERE RewardDisciplineName = :name")
    List<RewardDiscipline> getRewardDisciplineByName(String name);
}
