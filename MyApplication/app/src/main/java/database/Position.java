package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Position")
public class Position {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PositionID")
    private int positionId;

    @ColumnInfo(name = "PositionName")
    private String positionName;

    // Constructor
    public Position(String positionName, String note) {
        this.positionName = positionName;
    }

    // Getters and Setters
    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
