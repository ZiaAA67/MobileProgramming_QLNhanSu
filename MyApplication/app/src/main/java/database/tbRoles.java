package database;

public class tbRoles {
    public static final String TABLE_NAME = "Roles";

    // Column names
    public static final String COLUMN_ROLE_ID = "RoleID";
    public static final String COLUMN_ROLE_NAME = "RoleName";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ROLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_ROLE_NAME + " VARCHAR NOT NULL UNIQUE, " +
            COLUMN_NOTE + " TEXT);";
}
