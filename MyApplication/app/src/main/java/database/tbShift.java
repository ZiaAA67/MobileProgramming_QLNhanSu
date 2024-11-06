package database;

public class tbShift {
    public static final String TABLE_NAME = "Shift";

    // Column names
    public static final String COLUMN_SHIFT_ID = "ShiftID";
    public static final String COLUMN_SHIFT_NAME = "ShiftName";
    public static final String COLUMN_TIME_START = "TimeStart";
    public static final String COLUMN_TIME_END = "TimeEnd";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_SHIFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_SHIFT_NAME + " VARCHAR NOT NULL, " +
            COLUMN_TIME_START + " TIME, " +
            COLUMN_TIME_END + " TIME);";
}
