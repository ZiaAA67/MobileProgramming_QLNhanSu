package database;

public class tbSection {
    public static final String TABLE_NAME = "Section";

    // Column names
    public static final String COLUMN_SECTION_ID = "SectionID";
    public static final String COLUMN_SECTION_NAME = "SectionName";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_SECTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_SECTION_NAME + " VARCHAR NOT NULL, " +
            COLUMN_NOTE + " TEXT);";
}
