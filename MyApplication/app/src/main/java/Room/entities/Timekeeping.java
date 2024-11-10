package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "Timekeeping",
        foreignKeys = @ForeignKey(entity = Session.class,
                parentColumns = "SessionID",
                childColumns = "SessionID")
)
public class Timekeeping {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TimekeepingID")
    private int timekeepingId;

    @ColumnInfo(name = "SessionID")
    private int sessionId;

    @ColumnInfo(name = "TimeIn")
    private String timeIn;

    @ColumnInfo(name = "TimeOut")
    private String timeOut;

    @ColumnInfo(name = "Status")
    private Integer status;

    // Constructor
    public Timekeeping(int sessionId, String timeIn, String timeOut, Integer status) {
        this.sessionId = sessionId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
    }

    // Getters and Setters
    public int getTimekeepingId() {
        return timekeepingId;
    }

    public void setTimekeepingId(int timekeepingId) {
        this.timekeepingId = timekeepingId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
