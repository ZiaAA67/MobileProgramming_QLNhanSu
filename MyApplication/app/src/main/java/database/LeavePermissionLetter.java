package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "LeavePermissionLetter",
        foreignKeys = @ForeignKey(entity = Employee.class,
                parentColumns = "EmployeeID",
                childColumns = "EmployeeID")
)
public class LeavePermissionLetter {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "LeavePermissionLetterID")
    private int leavePermissionLetterId;

    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "Reason")
    private String reason;

    @ColumnInfo(name = "SendDate")
    private String sendDate;

    @ColumnInfo(name = "OffDate")
    private String offDate;

    @ColumnInfo(name = "Status")
    private Integer status;

    // Constructor
    public LeavePermissionLetter(int employeeId, String reason, String sendDate, String offDate, Integer status) {
        this.employeeId = employeeId;
        this.reason = reason;
        this.sendDate = sendDate;
        this.offDate = offDate;
        this.status = status;
    }

    // Getters and Setters
    public int getLeavePermissionLetterId() {
        return leavePermissionLetterId;
    }

    public void setLeavePermissionLetterId(int leavePermissionLetterId) {
        this.leavePermissionLetterId = leavePermissionLetterId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getOffDate() {
        return offDate;
    }

    public void setOffDate(String offDate) {
        this.offDate = offDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
