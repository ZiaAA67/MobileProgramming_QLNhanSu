package database;

public class tbPosition {
    public static final String TABLE_NAME = "Position";

    // Column names
    public static final String COLUMN_POSITION_ID = "PositionID";
    public static final String COLUMN_POSITION_NAME = "PositionName";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_POSITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_POSITION_NAME + " VARCHAR NOT NULL, " +
            COLUMN_NOTE + " TEXT);";
}
