package database;

public class tbUsers {
    public static final String TABLE_NAME = "Users";

    // Column names
    public static final String COLUMN_USERS_ID = "UsersID";
    public static final String COLUMN_EMPLOYEE_ID = "EmployeeID";
    public static final String COLUMN_ROLE_ID = "RoleID";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_CREATE_DATE = "CreateDate";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_EMPLOYEE_ID + " INTEGER UNIQUE REFERENCES Employees(EmployeeID), " +
            COLUMN_ROLE_ID + " INTEGER REFERENCES Roles(RoleID), " +
            COLUMN_USERNAME + " VARCHAR NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " VARCHAR NOT NULL, " +
            COLUMN_STATUS + " INTEGER, " +
            COLUMN_CREATE_DATE + " DATE);";
}
