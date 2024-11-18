package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RewardDiscipline")
public class RewardDiscipline {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "RewardDisciplineID")
    private int rewardDisciplineId;

    @ColumnInfo(name = "RewardDisciplineName")
    private String rewardDisciplineName;

    @ColumnInfo(name = "Type")
    private int type;

    @ColumnInfo(name = "Content")
    private String content;


    // Constructor
    public RewardDiscipline(String rewardDisciplineName, int type, String content) {
        this.rewardDisciplineName = rewardDisciplineName;
        this.type = type;
        this.content = content;
    }


    // Getters and Setters
    public int getRewardDisciplineId() {
        return rewardDisciplineId;
    }

    public void setRewardDisciplineId(int rewardDisciplineId) {
        this.rewardDisciplineId = rewardDisciplineId;
    }

    public String getRewardDisciplineName() {
        return rewardDisciplineName;
    }

    public void setRewardDisciplineName(String rewardDisciplineName) {
        this.rewardDisciplineName = rewardDisciplineName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
