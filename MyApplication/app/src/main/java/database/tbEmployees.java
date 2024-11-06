package database;

public class tbEmployees {
    public static final String TABLE_NAME = "Employees";

    // Column names
    public static final String COLUMN_EMPLOYEE_ID = "EmployeeID";
    public static final String COLUMN_FULL_NAME = "FullName";
    public static final String COLUMN_SEX = "Sex";
    public static final String COLUMN_BIRTH = "Birth";
    public static final String COLUMN_IDENTITY_NUMBER = "IdentityNumber";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_JOIN_DATE = "JoinDate";
    public static final String COLUMN_IMAGE = "Image";
    public static final String COLUMN_SALARY_ID = "SalaryID";
    public static final String COLUMN_DEPARTMENT_ID = "DepartmentID";
    public static final String COLUMN_POSITION_ID = "PositionID";
    public static final String COLUMN_SECTION_ID = "SectionID";
    public static final String COLUMN_EDUCATION_ID = "EducationID";
    public static final String COLUMN_NOTE = "Note";

    // SQL statement to create the table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_FULL_NAME + " VARCHAR NOT NULL, " +
            COLUMN_SEX + " INTEGER NOT NULL, " +
            COLUMN_BIRTH + " DATE NOT NULL, " +
            COLUMN_IDENTITY_NUMBER + " VARCHAR NOT NULL CHECK (LENGTH(" + COLUMN_IDENTITY_NUMBER + ") = 10 AND " + COLUMN_IDENTITY_NUMBER + " GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'), " +
            COLUMN_ADDRESS + " TEXT, " +
            COLUMN_PHONE_NUMBER + " VARCHAR UNIQUE CHECK (LENGTH(" + COLUMN_PHONE_NUMBER + ") = 10 AND " + COLUMN_PHONE_NUMBER + " GLOB '[0][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'), " +
            COLUMN_EMAIL + " VARCHAR UNIQUE CHECK (" + COLUMN_EMAIL + " LIKE '%@%'), " +
            COLUMN_STATUS + " INTEGER, " +
            COLUMN_JOIN_DATE + " DATE, " +
            COLUMN_IMAGE + " BLOB, " +
            COLUMN_SALARY_ID + " INTEGER REFERENCES Salary(" + COLUMN_SALARY_ID + "), " +
            COLUMN_DEPARTMENT_ID + " INTEGER REFERENCES Departments(" + COLUMN_DEPARTMENT_ID + "), " +
            COLUMN_POSITION_ID + " INTEGER REFERENCES Position(" + COLUMN_POSITION_ID + "), " +
            COLUMN_SECTION_ID + " INTEGER REFERENCES Section(" + COLUMN_SECTION_ID + "), " +
            COLUMN_EDUCATION_ID + " INTEGER REFERENCES EducationLevel(" + COLUMN_EDUCATION_ID + "), " +
            COLUMN_NOTE + " TEXT);";
}
