package database;

public class tbRewardsDiscipline {
    public static final String TABLE_NAME = "RewardsDiscipline";

    // Column names
    public static final String COLUMN_REWARDS_DISCIPLINE_ID = "RewardsDisciplineID";
    public static final String COLUMN_REWARDS_DISCIPLINE_NAME = "RewardsDisciplineName";
    public static final String COLUMN_TYPE = "Type";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_REWARDS_DISCIPLINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_REWARDS_DISCIPLINE_NAME + " VARCHAR NOT NULL, " +
            COLUMN_TYPE + " INTEGER);";
}
