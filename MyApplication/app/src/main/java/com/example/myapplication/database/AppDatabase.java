package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.database.dao.DepartmentDAO;
import com.example.myapplication.database.dao.EducationLevelDAO;
import com.example.myapplication.database.dao.EmployeeDAO;
import com.example.myapplication.database.dao.Employee_RewardDisciplineDAO;
import com.example.myapplication.database.dao.Employee_SessionDAO;
import com.example.myapplication.database.dao.LeaveRequestDAO;
import com.example.myapplication.database.dao.PositionDAO;
import com.example.myapplication.database.dao.RewardDisciplineDAO;
import com.example.myapplication.database.dao.RoleDAO;
import com.example.myapplication.database.dao.SalaryDAO;
import com.example.myapplication.database.dao.SessionDAO;
import com.example.myapplication.database.dao.ShiftDAO;
import com.example.myapplication.database.dao.TimekeepingDAO;
import com.example.myapplication.database.dao.UserDAO;
import com.example.myapplication.database.dao.WorkplaceDAO;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.EducationLevel;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_RewardDiscipline;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.LeaveRequest;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.RewardDiscipline;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.Salary;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;

import java.util.concurrent.Executors;

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
        User.class,
        Workplace.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "HumanResourceManagement.db";
    private static AppDatabase instance;

    // synchronzied ngan chan chay cac luong khac nhau tao ra nhieu the hien
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                SampleDatabase.populate(instance, context);
                            });
                        }
                    })
                    .allowMainThreadQueries() // Query in main thread
                    .build();
        }
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

    public abstract RoleDAO roleDao();

    public abstract RewardDisciplineDAO rewardDisciplineDao();

    public abstract Employee_RewardDisciplineDAO employeeRewardDisciplineDao();

    public abstract LeaveRequestDAO leaveRequestDao();

    public abstract Employee_SessionDAO employeeSessionDao();

    public abstract UserDAO userDao();

    public abstract WorkplaceDAO workplaceDao();
}