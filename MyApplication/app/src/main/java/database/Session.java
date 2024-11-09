package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Session",
        foreignKeys = @ForeignKey(entity = Shift.class,
                parentColumns = "ShiftID",
                childColumns = "ShiftID")
)
public class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SessionID")
    private int sessionId;

    @ColumnInfo(name = "ShiftID")
    private int shiftId;

    @ColumnInfo(name = "Day")
    private int day;

    @ColumnInfo(name = "Month")
    private int month;

    @ColumnInfo(name = "Year")
    private int year;

    @ColumnInfo(name = "isHoliday")
    private int isHoliday;

    // Constructor
    public Session(int shiftId, int day, int month, int year, int isHoliday) {
        this.shiftId = shiftId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.isHoliday = isHoliday;
    }

    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(int isHoliday) {
        this.isHoliday = isHoliday;
    }
}
