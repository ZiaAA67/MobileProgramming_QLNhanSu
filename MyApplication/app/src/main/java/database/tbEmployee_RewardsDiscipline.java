package database;

public class tbEmployee_RewardsDiscipline {
    public static final String TABLE_NAME = "Employee_RewardsDiscipline";

    // Column names
    public static final String COLUMN_EMPLOYEE_ID = "EmployeeID";
    public static final String COLUMN_REWARDS_DISCIPLINE_ID = "RewardsDisciplineID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_CONTENT = "Content";
    public static final String COLUMN_BONUS = "Bonus";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL, " +
            COLUMN_REWARDS_DISCIPLINE_ID + " INTEGER NOT NULL, " +
            COLUMN_DATE + " DATE NOT NULL, " +
            COLUMN_CONTENT + " TEXT, " +
            COLUMN_BONUS + " FLOAT, " +
            "PRIMARY KEY (" + COLUMN_EMPLOYEE_ID + ", " + COLUMN_REWARDS_DISCIPLINE_ID + ", " + COLUMN_DATE + "), " +
            "FOREIGN KEY (" + COLUMN_EMPLOYEE_ID + ") REFERENCES Employees(EmployeeID), " +
            "FOREIGN KEY (" + COLUMN_REWARDS_DISCIPLINE_ID + ") REFERENCES RewardsDiscipline(RewardsDisciplineID));";
}
