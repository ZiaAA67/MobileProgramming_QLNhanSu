package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RewardsDiscipline")
public class RewardsDiscipline {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "RewardsDisciplineID")
    private int rewardsDisciplineId;

    @ColumnInfo(name = "RewardsDisciplineName")
    private String rewardsDisciplineName;

    @ColumnInfo(name = "Type")
    private int type;

    // Constructor
    public RewardsDiscipline(String rewardsDisciplineName, int type) {
        this.rewardsDisciplineName = rewardsDisciplineName;
        this.type = type;
    }

    // Getters and Setters
    public int getRewardsDisciplineId() {
        return rewardsDisciplineId;
    }

    public void setRewardsDisciplineId(int rewardsDisciplineId) {
        this.rewardsDisciplineId = rewardsDisciplineId;
    }

    public String getRewardsDisciplineName() {
        return rewardsDisciplineName;
    }

    public void setRewardsDisciplineName(String rewardsDisciplineName) {
        this.rewardsDisciplineName = rewardsDisciplineName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
