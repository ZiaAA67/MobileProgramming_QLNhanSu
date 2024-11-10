package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Employee_Shift",
        primaryKeys = {"EmployeeID", "ShiftID"},
        foreignKeys = {
                @ForeignKey(entity = Employee.class,
                        parentColumns = "EmployeeID",
                        childColumns = "EmployeeID"),
                @ForeignKey(entity = Shift.class,
                        parentColumns = "ShiftID",
                        childColumns = "ShiftID")
        }
)
public class Employee_Shift {

    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "ShiftID")
    private int shiftId;

    // Constructor
    public Employee_Shift(int employeeId, int shiftId) {
        this.employeeId = employeeId;
        this.shiftId = shiftId;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }
}
