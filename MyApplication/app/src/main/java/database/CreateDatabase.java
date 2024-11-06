package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {

//link tham khao: https://www.youtube.com/watch?v=b5Iaq1W-UW0&list=PLwjYpXF3o7v8o-dIpIXwahYTeUcXGqLpW&index=7

    private static final String DATABASE_NAME = "Employees_Manager";// Database Name
    private static final int DATABASE_VERSION = 1;


    public CreateDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tbDepartments.CREATE_TABLE);
        db.execSQL(tbEducationLevel.CREATE_TABLE);
        db.execSQL(tbEmployee_RewardsDiscipline.CREATE_TABLE);
        db.execSQL(tbEmployee_Shift.CREATE_TABLE);
        db.execSQL(tbEmployees.CREATE_TABLE);
        db.execSQL(tbLeavePermissionLetter.CREATE_TABLE);
        db.execSQL(tbPosition.CREATE_TABLE);
        db.execSQL(tbRewardsDiscipline.CREATE_TABLE);
        db.execSQL(tbRoles.CREATE_TABLE);
        db.execSQL(tbSalary.CREATE_TABLE);
        db.execSQL(tbSection.CREATE_TABLE);
        db.execSQL(tbSession.CREATE_TABLE);
        db.execSQL(tbShift.CREATE_TABLE);
        db.execSQL(tbTimekeeping.CREATE_TABLE);
        db.execSQL(tbUsers.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Departments");
        db.execSQL("DROP TABLE IF EXISTS EducationLevel");
        db.execSQL("DROP TABLE IF EXISTS Employee_RewardsDiscipline");
        db.execSQL("DROP TABLE IF EXISTS Employee_Shift");
        db.execSQL("DROP TABLE IF EXISTS Employees");
        db.execSQL("DROP TABLE IF EXISTS LeavePermissionLetter");
        db.execSQL("DROP TABLE IF EXISTS Position");
        db.execSQL("DROP TABLE IF EXISTS RewardsDiscipline");
        db.execSQL("DROP TABLE IF EXISTS Roles");
        db.execSQL("DROP TABLE IF EXISTS Salary");
        db.execSQL("DROP TABLE IF EXISTS Section");
        db.execSQL("DROP TABLE IF EXISTS Session");
        db.execSQL("DROP TABLE IF EXISTS Shift");
        db.execSQL("DROP TABLE IF EXISTS Timekeeping");
        db.execSQL("DROP TABLE IF EXISTS Users");

        onCreate(db);
    }
}
