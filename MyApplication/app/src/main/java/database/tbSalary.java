package database;

public class tbSalary {
    public static final String TABLE_NAME = "Salary";

    // Column names
    public static final String COLUMN_SALARY_ID = "SalaryID";
    public static final String COLUMN_BASIC_SALARY = "BasicSalary";
    public static final String COLUMN_SUPPLEMENTAL_PAY = "SuplementalPay";
    public static final String COLUMN_COEFFICIENT = "Coefficient";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_SALARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_BASIC_SALARY + " FLOAT, " +
            COLUMN_SUPPLEMENTAL_PAY + " FLOAT, " +
            COLUMN_COEFFICIENT + " FLOAT);";
}
