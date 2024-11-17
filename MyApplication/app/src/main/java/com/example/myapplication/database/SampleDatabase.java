package com.example.myapplication.database;

import android.content.Context;

import com.example.myapplication.Configuration;
import com.example.myapplication.database.entities.*;

public class SampleDatabase {

    public static void populate(AppDatabase db, Context context) {

        Role role1 = new Role("admin", "sysAdmin");
        Role role2 = new Role("manager", "deleted Employee");
        Role role3 = new Role("public", "No permission");
        AppDatabase.getInstance(context).roleDao().insert(role1);
        AppDatabase.getInstance(context).roleDao().insert(role2);
        AppDatabase.getInstance(context).roleDao().insert(role3);

        User user1 = new User("ADMIN", Configuration.md5("123"), Configuration.STRING_TODAY, 1);
        User user2 = new User("user1", Configuration.md5("123"), Configuration.STRING_TODAY, 2);
        User user3 = new User("user2", Configuration.md5("123"), Configuration.STRING_TODAY, 3);
        AppDatabase.getInstance(context).userDao().insert(user1);
        AppDatabase.getInstance(context).userDao().insert(user2);
        AppDatabase.getInstance(context).userDao().insert(user3);

        Department dept1 = new Department("Administration", "ADMIN");
        Department dept2 = new Department("Accounting", "Rich kid");
        Department dept3 = new Department("IT", "Colder and coder like IT");
        AppDatabase.getInstance(context).departmentDao().insert(dept1);
        AppDatabase.getInstance(context).departmentDao().insert(dept2);
        AppDatabase.getInstance(context).departmentDao().insert(dept3);

        EducationLevel edu1 = new EducationLevel("Doctor of Philosophy", "HRM", "HUST");
        EducationLevel edu2 = new EducationLevel("Grand Master", "Computer Science", "OU");
        EducationLevel edu3 = new EducationLevel("No School", "Computer Science", "OU");
        AppDatabase.getInstance(context).educationLevelDao().insert(edu1);
        AppDatabase.getInstance(context).educationLevelDao().insert(edu2);
        AppDatabase.getInstance(context).educationLevelDao().insert(edu3);

        Position pos1 = new Position("Manager", "A BIG DREAM");
        Position pos2 = new Position("Employee", "NO DREAM");
        AppDatabase.getInstance(context).positionDao().insert(pos1);
        AppDatabase.getInstance(context).positionDao().insert(pos2);

        RewardDiscipline reward1 = new RewardDiscipline("Employee of the Month", 1, "Excellent performence");
        RewardDiscipline reward2 = new RewardDiscipline("Warning", 0, "Too lazy");
        AppDatabase.getInstance(context).rewardDisciplineDao().insert(reward1);
        AppDatabase.getInstance(context).rewardDisciplineDao().insert(reward2);

        Workplace workplace1 = new Workplace("Circle K", 10.761506, 106.707841);
        AppDatabase.getInstance(context).workplaceDao().insert(workplace1);

        Salary salary1 = new Salary((float) 50000000, (float) 2000000, (float) 1.5);
        Salary salary2 = new Salary((float) 30000000, (float) 2000000, (float) 1.0);
        Salary salary3 = new Salary((float) 5000000, (float) 120000, (float) 1.0);
        AppDatabase.getInstance(context).salaryDao().insert(salary1);
        AppDatabase.getInstance(context).salaryDao().insert(salary2);
        AppDatabase.getInstance(context).salaryDao().insert(salary3);

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

        Employee employee1 = new Employee("Thai Do Dinh", 0, "21/07/2004", "089204014523", "Dinh Everest", "0921343540", "2251010086thinh@ou.edu.vn", 1, null, 1, 1, 1, 1, 1, 1);
        Employee employee2 = new Employee("Minh Nhat", 0, "21/02/2004", "089204014526", "HCM", "0921343541", "minhnhat@gmail.com", 1, null, 2, 2, 1, 2, 2, 1);
        Employee employee3 = new Employee("Nghia ga", 0, "11/05/2004", "089204014565", "Vuc Mariana", "0799504271", "nghianofuture@gmail.com", 1, null, 3, 3, 2, 3, 3, 1);
        AppDatabase.getInstance(context).employeeDao().insert(employee1, employee2, employee3);

        Employee_Session empSession1 = new Employee_Session(1, 1);
        Employee_Session empSession2 = new Employee_Session(2, 2);
        Employee_Session empSession3 = new Employee_Session(3, 3);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession1);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession2);
        AppDatabase.getInstance(context).employeeSessionDao().insert(empSession3);

        Employee_RewardDiscipline empReward1 = new Employee_RewardDiscipline(1, 1, "16/11/2024", 500F);
        Employee_RewardDiscipline empReward2 = new Employee_RewardDiscipline(2, 1, "16/11/2024", 300.0F);
        Employee_RewardDiscipline empReward3 = new Employee_RewardDiscipline(3, 2, "16/11/2024", -970.0F);
        AppDatabase.getInstance(context).employeeRewardDisciplineDao().insert(empReward1);
        AppDatabase.getInstance(context).employeeRewardDisciplineDao().insert(empReward2);
        AppDatabase.getInstance(context).employeeRewardDisciplineDao().insert(empReward3);

        LeaveRequest leave1 = new LeaveRequest("Date with girlfriend", "15/11/2024", "15/11/2024", "16/11/2024", 1, 1);
        LeaveRequest leave2 = new LeaveRequest("A car hit me", "13/11/2024", "12/11/2024", "31/12/2024", 0, 3);
        AppDatabase.getInstance(context).leaveRequestDao().insert(leave1);
        AppDatabase.getInstance(context).leaveRequestDao().insert(leave2);
    }
}