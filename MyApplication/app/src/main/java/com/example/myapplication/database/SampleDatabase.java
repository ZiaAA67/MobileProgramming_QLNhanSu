package com.example.myapplication.database;

import android.content.Context;

import com.example.myapplication.Configuration;
import com.example.myapplication.database.entities.Department;
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

public class SampleDatabase {

    public static void populate(AppDatabase db, Context context) {

        Role role1 = new Role("Admin", "System admin with full permission");
        Role role2 = new Role("Manager", "CRUD users, employees, department, ....");
        Role role3 = new Role("Public", "View only");
        AppDatabase.getInstance(context).roleDao().insert(role1);
        AppDatabase.getInstance(context).roleDao().insert(role2);
        AppDatabase.getInstance(context).roleDao().insert(role3);

        User user1 = new User("ADMIN", Configuration.md5("123"), Configuration.STRING_TODAY, true, false, 1);
        User user2 = new User("user", Configuration.md5("123"), Configuration.STRING_TODAY, true, false, 3);
        AppDatabase.getInstance(context).userDao().insert(user1);
        AppDatabase.getInstance(context).userDao().insert(user2);

        Department dept1 = new Department("Nhân sự", true, "Quản lý nhân sự trong công ty");
        Department dept2 = new Department("Marketing", true, "Tiếp thị v quảng cáo");
        Department dept3 = new Department("IT", true, "Bảo trì và nâng cấp hệ thống");
        AppDatabase.getInstance(context).departmentDao().insert(dept1);
        AppDatabase.getInstance(context).departmentDao().insert(dept2);
        AppDatabase.getInstance(context).departmentDao().insert(dept3);

        Position pos1 = new Position("Quản lý", "Điều hành và quản lý nhân sự, công việc trong phòng ban");
        Position pos2 = new Position("Nhân viên", "Thực hiện công việc hàng ngày theo chỉ dẫn");
        AppDatabase.getInstance(context).positionDao().insert(pos1);
        AppDatabase.getInstance(context).positionDao().insert(pos2);

        RewardDiscipline reward1 = new RewardDiscipline("EmployeeManagement of the Month", 1, "Excellent performence");
        RewardDiscipline reward2 = new RewardDiscipline("Warning", 0, "Too lazy");
        AppDatabase.getInstance(context).rewardDisciplineDao().insert(reward1);
        AppDatabase.getInstance(context).rewardDisciplineDao().insert(reward2);

        Workplace workplace1 = new Workplace("Cơ sở 1 - Nguyễn Kiệm", true, 10.761506, 106.707841);
        AppDatabase.getInstance(context).workplaceDao().insert(workplace1);

        Salary salary1 = new Salary((float) 80000000, (float) 7000000, (float) 1.5);
        Salary salary2 = new Salary((float) 5000000, (float) 120000, (float) 1.0);
        AppDatabase.getInstance(context).salaryDao().insert(salary1);
        AppDatabase.getInstance(context).salaryDao().insert(salary2);

        Shift shift1 = new Shift("Morning", "08:00", "13:00");
        Shift shift2 = new Shift("Afternoon", "13:00", "18:00");
        Shift shift3 = new Shift("Full", "08:00", "18:00");
        AppDatabase.getInstance(context).shiftDao().insert(shift1);
        AppDatabase.getInstance(context).shiftDao().insert(shift2);
        AppDatabase.getInstance(context).shiftDao().insert(shift3);

        Session session1 = new Session(16, 11, 2024, false, 1);
        Session session2 = new Session(15, 11, 2024, false, 2);
        Session session3 = new Session(14, 11, 2024, true, 3);
        AppDatabase.getInstance(context).sessionDao().insert(session1);
        AppDatabase.getInstance(context).sessionDao().insert(session2);
        AppDatabase.getInstance(context).sessionDao().insert(session3);

        Timekeeping time1 = new Timekeeping("07:47", "13:05", 1, null, 1);
        Timekeeping time2 = new Timekeeping("12:49", "18:02", 1, null, 2);
        Timekeeping time3 = new Timekeeping("08:00", "18:00", 0, null, 3);
        AppDatabase.getInstance(context).timekeepingDao().insert(time1);
        AppDatabase.getInstance(context).timekeepingDao().insert(time2);
        AppDatabase.getInstance(context).timekeepingDao().insert(time3);

        Employee employee1 = new Employee("Thái Đỗ Đỉnh", 0, "21/02/2004", "089204014523", "Đỉnh Everest", "0921343540", "2251010086thinh@ou.edu.vn", true, true, "https://res.cloudinary.com/dbmwgavqz/image/upload/v1732299242/Sample_User_Icon_n52rlr.png", 1, 1, 1, null, 1, 1);
        Employee employee2 = new Employee("Dách 5 tiệu", 0, "22/02/1997", "119204014523", "Mariana", "0921113549", "5trieujack@gmail.com", true, true, "https://res.cloudinary.com/dbmwgavqz/image/upload/v1732299242/Sample_User_Icon_n52rlr.png", 2, 2, 2, null, 2, 1);
        AppDatabase.getInstance(context).employeeDao().insert(employee1, employee2);

        Employee_Session empSession1 = new Employee_Session(1, 1);
        Employee_Session empSession2 = new Employee_Session(2, 2);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession1);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession2);

        Employee_RewardDiscipline empReward1 = new Employee_RewardDiscipline(1, 1, "16/11/2024", 500F);
        Employee_RewardDiscipline empReward2 = new Employee_RewardDiscipline(2, 1, "16/11/2024", 300.0F);
        AppDatabase.getInstance(context).employeeRewardDisciplineDao().insert(empReward1);
        AppDatabase.getInstance(context).employeeRewardDisciplineDao().insert(empReward2);

        LeaveRequest leave1 = new LeaveRequest("Date with girlfriend", "15/11/2024", "15/11/2024", "16/11/2024", 1, 1);
        LeaveRequest leave2 = new LeaveRequest("A car hit me", "13/11/2024", "12/11/2024", "31/12/2024", 0, 2);
        AppDatabase.getInstance(context).leaveRequestDao().insert(leave1);
        AppDatabase.getInstance(context).leaveRequestDao().insert(leave2);
    }
}