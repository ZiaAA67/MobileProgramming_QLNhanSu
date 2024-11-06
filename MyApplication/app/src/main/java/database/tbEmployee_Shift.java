package database;

public class tbEmployee_Shift {
        public static final String TABLE_NAME = "Employee_Shift";

        // Column names
        public static final String COLUMN_EMPLOYEE_ID = "EmployeeID";
        public static final String COLUMN_SHIFT_ID = "ShiftID";

        // SQL statement to create the table
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EMPLOYEE_ID + " INTEGER NOT NULL REFERENCES Employees(EmployeeID), " +
                COLUMN_SHIFT_ID + " INTEGER NOT NULL REFERENCES Shift(ShiftID), " +
                "PRIMARY KEY (" + COLUMN_EMPLOYEE_ID + ", " + COLUMN_SHIFT_ID + "));";
}
