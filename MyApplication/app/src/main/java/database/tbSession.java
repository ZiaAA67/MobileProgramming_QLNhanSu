package database;

public class tbSession {
    public static final String TABLE_NAME = "Session";

    // Column names
    public static final String COLUMN_SESSION_ID = "SessionID";
    public static final String COLUMN_SHIFT_ID = "ShiftID";
    public static final String COLUMN_DAY = "Day";
    public static final String COLUMN_MONTH = "Month";
    public static final String COLUMN_YEAR = "Year";
    public static final String COLUMN_IS_HOLIDAY = "isHoliday";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_SHIFT_ID + " INTEGER REFERENCES Shift(ShiftID), " +
            COLUMN_DAY + " INTEGER, " +
            COLUMN_MONTH + " INTEGER, " +
            COLUMN_YEAR + " INTEGER, " +
            COLUMN_IS_HOLIDAY + " INTEGER);";
}
