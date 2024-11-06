package database;

public class tbLeavePermissionLetter {
    public static final String TABLE_NAME = "LeavePermissionLetter";

    // Column names
    public static final String COLUMN_LEAVE_PERMISSION_LETTER_ID = "LeavePermissionLetterID";
    public static final String COLUMN_EMPLOYEE_ID = "EmployeeID";
    public static final String COLUMN_REASON = "Reason";
    public static final String COLUMN_SEND_DATE = "SendDate";
    public static final String COLUMN_OFF_DATE = "OffDate";
    public static final String COLUMN_STATUS = "Status";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_LEAVE_PERMISSION_LETTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_EMPLOYEE_ID + " INTEGER REFERENCES Employees(EmployeeID), " +
            COLUMN_REASON + " TEXT, " +
            COLUMN_SEND_DATE + " DATE, " +
            COLUMN_OFF_DATE + " DATE, " +
            COLUMN_STATUS + " INTEGER);";
}
