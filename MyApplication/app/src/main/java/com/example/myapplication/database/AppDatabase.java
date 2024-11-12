package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.database.entities.*;
import com.example.myapplication.database.dao.*;

// Entities list that insert to database
@Database(entities = {
        Employee.class,
        Department.class,
        Position.class,
        EducationLevel.class,
        Salary.class,
        Timekeeping.class,
        Session.class,
        Shift.class,
        Role.class,
        RewardDiscipline.class,
        Employee_RewardDiscipline.class,
        LeaveRequest.class,
        Employee_Session.class,
        User.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "HumanResourceManagement.db";
    private static AppDatabase instance;

    // synchronzied ngan chan chay cac luong khac nhau tao ra nhieu the hien
    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries() // Query in main thread
                    .build();
        return instance;
    }

    public abstract EmployeeDAO employeeDao();
    public abstract DepartmentDAO departmentDao();
    public abstract PositionDAO positionDao();
    public abstract EducationLevelDAO educationLevelDao();
    public abstract SalaryDAO salaryDao();
    public abstract TimekeepingDAO timekeepingDao();
    public abstract SessionDAO sessionDao();
    public abstract ShiftDAO shiftDao();
    public abstract RoleDAO rolesDao();
    public abstract RewardDisciplineDAO rewardsDisciplineDao();
    public abstract Employee_RewardDisciplineDAO employeeRewardsDisciplineDao();
    public abstract LeaveRequestDAO leavePermissionLetterDao();
    public abstract Employee_SessionDAO employeeShiftDao();
    public abstract UserDAO userDao();
}
