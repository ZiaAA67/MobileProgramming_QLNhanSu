package com.example.myapplication.database;

import android.content.Context;

import com.example.myapplication.Configuration;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
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
        User user2 = new User("user", Configuration.md5("123"), "10/9/2024", true, false, 3);
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

        Shift shift1 = new Shift("Office hours", "08:00", "18:00");
        AppDatabase.getInstance(context).shiftDao().insert(shift1);

        Session session1 = new Session(1, 11, 2024, false, 1);
        Session session2 = new Session(2, 11, 2024, false, 1);
        Session session3 = new Session(3, 11, 2024, false, 1);
        Session session4 = new Session(4, 11, 2024, false, 1);
        Session session5 = new Session(5, 11, 2024, false, 1);
        Session session6 = new Session(6, 11, 2024, false, 1);
        Session session7 = new Session(7, 11, 2024, false, 1);
        Session session8 = new Session(8, 11, 2024, false, 1);
        Session session9 = new Session(9, 11, 2024, false, 1);
        Session session10 = new Session(10, 11, 2024, false, 1);
        AppDatabase.getInstance(context).sessionDao().insert(session1);
        AppDatabase.getInstance(context).sessionDao().insert(session2);
        AppDatabase.getInstance(context).sessionDao().insert(session3);
        AppDatabase.getInstance(context).sessionDao().insert(session4);
        AppDatabase.getInstance(context).sessionDao().insert(session5);
        AppDatabase.getInstance(context).sessionDao().insert(session6);
        AppDatabase.getInstance(context).sessionDao().insert(session7);
        AppDatabase.getInstance(context).sessionDao().insert(session8);
        AppDatabase.getInstance(context).sessionDao().insert(session9);
        AppDatabase.getInstance(context).sessionDao().insert(session10);

        Timekeeping time1 = new Timekeeping("07:47", "18:05", 0, 0, 1);
        Timekeeping time2 = new Timekeeping("07:49", "18:02", 0, 0, 2);
        Timekeeping time3 = new Timekeeping("08:00", "18:07", 0, 0, 3);
        Timekeeping time4 = new Timekeeping("07:55", "18:11", 0, 0, 4);
        Timekeeping time5 = new Timekeeping("07:54", "17:50", 0, 0, 5);
        Timekeeping time6 = new Timekeeping("07:36", "17:57", 0, 0, 6);
        Timekeeping time7 = new Timekeeping("07:59", "18:10", 0, 0, 7);
        Timekeeping time8 = new Timekeeping("07:48", "18:15", 0, 0, 8);
        Timekeeping time9 = new Timekeeping("07:51", "18:21", 0, 0, 9);
        Timekeeping time10 = new Timekeeping("07:52", "18:22", 0, 0, 10);

        AppDatabase.getInstance(context).timekeepingDao().insert(time1);
        AppDatabase.getInstance(context).timekeepingDao().insert(time2);
        AppDatabase.getInstance(context).timekeepingDao().insert(time3);
        AppDatabase.getInstance(context).timekeepingDao().insert(time4);
        AppDatabase.getInstance(context).timekeepingDao().insert(time5);
        AppDatabase.getInstance(context).timekeepingDao().insert(time6);
        AppDatabase.getInstance(context).timekeepingDao().insert(time7);
        AppDatabase.getInstance(context).timekeepingDao().insert(time8);
        AppDatabase.getInstance(context).timekeepingDao().insert(time9);
        AppDatabase.getInstance(context).timekeepingDao().insert(time10);

        Employee employee1 = new Employee("Thái Đỗ Đỉnh", 0, "21/02/2004", "089204014523", "Đỉnh Everest", "0921343540", "2251010086thinh@ou.edu.vn", true, true, "https://res.cloudinary.com/dbmwgavqz/image/upload/v1732299242/Sample_User_Icon_n52rlr.png", 1, 1, 1, null, 1, 1);
        Employee employee2 = new Employee("Dách 5 tiệu", 0, "22/02/1997", "119204014523", "Mariana", "0921113549", "5trieujack@gmail.com", true, true, "https://res.cloudinary.com/dbmwgavqz/image/upload/v1732299242/Sample_User_Icon_n52rlr.png", 2, 2, 2, null, 2, 1);
        AppDatabase.getInstance(context).employeeDao().insert(employee1, employee2);

        Employee_Session empSession1 = new Employee_Session(1, 1);
        Employee_Session empSession2 = new Employee_Session(2, 2);
        Employee_Session empSession3 = new Employee_Session(2, 3);
        Employee_Session empSession4 = new Employee_Session(2, 4);
        Employee_Session empSession5 = new Employee_Session(2, 5);
        Employee_Session empSession6 = new Employee_Session(2, 6);
        Employee_Session empSession7 = new Employee_Session(2, 7);
        Employee_Session empSession8 = new Employee_Session(2, 8);
        Employee_Session empSession9 = new Employee_Session(2, 9);
        Employee_Session empSession10 = new Employee_Session(2, 10);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession1);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession2);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession3);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession4);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession5);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession6);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession7);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession8);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession9);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession10);
    }
}