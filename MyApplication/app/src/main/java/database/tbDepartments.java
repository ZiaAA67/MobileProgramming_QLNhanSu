package database;

public class tbDepartments {
    public static final String TABLE_NAME = "Departments";

    // Column names
    public static final String COLUMN_DEPARTMENT_ID = "DepartmentID";
    public static final String COLUMN_DEPARTMENT_NAME = "DepartmentName";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_DEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_DEPARTMENT_NAME + " VARCHAR NOT NULL, " +
            COLUMN_NOTE + " TEXT);";
}
