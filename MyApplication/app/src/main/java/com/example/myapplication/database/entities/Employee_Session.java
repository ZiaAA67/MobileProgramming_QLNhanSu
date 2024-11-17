package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Employee_Session",
        primaryKeys = {"EmployeeID", "SessionID"},
        foreignKeys = {
                @ForeignKey(entity = Employee.class,
                        parentColumns = "EmployeeID",
                        childColumns = "EmployeeID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Session.class,
                        parentColumns = "SessionID",
                        childColumns = "SessionID",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Employee_Session {

    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "SessionID")
    private int sessionID;


    // Constructor
    public Employee_Session(int employeeId, int sessionID) {
        this.employeeId = employeeId;
        this.sessionID = sessionID;
    }


    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }
}
