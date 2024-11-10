package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Shift")
public class Shift {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ShiftID")
    private int shiftId;

    @ColumnInfo(name = "ShiftName")
    private String shiftName;

    @ColumnInfo(name = "TimeStart")
    private String timeStart;

    @ColumnInfo(name = "TimeEnd")
    private String timeEnd;

    // Constructor
    public Shift(String shiftName, String timeStart, String timeEnd) {
        this.shiftName = shiftName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    // Getters and Setters
    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
