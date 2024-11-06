package database;

public class tbTimekeeping {
    public static final String TABLE_NAME = "Timekeeping";

    // Column names
    public static final String COLUMN_TIMEKEEPING_ID = "TimekeepingID";
    public static final String COLUMN_SESSION_ID = "SessionID";
    public static final String COLUMN_TIME_IN = "TimeIn";
    public static final String COLUMN_TIME_OUT = "TimeOut";
    public static final String COLUMN_STATUS = "Status";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_TIMEKEEPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_SESSION_ID + " INTEGER REFERENCES Session(SessionID), " +
            COLUMN_TIME_IN + " TIME, " +
            COLUMN_TIME_OUT + " TIME, " +
            COLUMN_STATUS + " INTEGER);";
}
