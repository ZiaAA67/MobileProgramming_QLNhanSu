package database;

public class tbEducationLevel {
    public static final String TABLE_NAME = "EducationLevel";

    // Column names
    public static final String COLUMN_EDUCATION_ID = "EducationID";
    public static final String COLUMN_EDUCATION_LEVEL_NAME = "EducationLevelName";
    public static final String COLUMN_MAJOR = "Major";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_EDUCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_EDUCATION_LEVEL_NAME + " VARCHAR NOT NULL, " +
            COLUMN_MAJOR + " VARCHAR, " +
            COLUMN_NOTE + " TEXT);";
}
