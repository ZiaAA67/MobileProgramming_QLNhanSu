package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Departments")
public class Department {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "DepartmentID")
    private int departmentId;

    @ColumnInfo(name = "DepartmentName")
    private String departmentName;

    // Constructor
    public Department(String departmentName, String note) {
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
