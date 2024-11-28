package com.example.myapplication.MainApp.Salary;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_RewardDiscipline;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Salary;

import java.util.List;

public class SalarySlipInformation extends AppCompatActivity {

    private int userId;
    private int month;
    private int year;
    private Button btnBack;
    private TextView tvEmployeeName;
    private TextView tvPosition;
    private TextView tvPosition2;
    private TextView tvDepartment;
    private TextView tvBaseSalary;
    private TextView tvAllowance;
    private TextView tvRewardDiscipline;
    private TextView tvTax;
    private TextView tvTotalSalary;
    private TextView tvReceiveSalary;
    private TextView tvReceiveSalary2;
    private List<Employee_RewardDiscipline> employeeRewardDisciplines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salary_slip_information);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();

        userId = getIntent().getIntExtra("UserID", -1);
        month = getIntent().getIntExtra("Month", -1);
        year = getIntent().getIntExtra("Year", -1);

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showEmployeeSalary();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void showEmployeeSalary() {
        Employee employee = getEmployee(userId);
        if (employee == null) {
            Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String fullName = (employee.getFullName() != null) ? employee.getFullName() : "Không có tên";
        String positionName = (employee.getPositionId() != null) ? getPositionName(employee.getPositionId()) : "Không có chức vụ";
        String departmentName = (employee.getDepartmentId() != null) ? getDepartmentName(employee.getDepartmentId()) : "Không có phòng ban";
        Float baseSalary = (employee.getSalaryId() != null) ? getBaseSalary(employee.getSalaryId()) : 0;
        Float allowanceSalary = (employee.getSalaryId() != null) ? getAllowanceSalary(employee.getSalaryId()) : 0;
        Float rewardDisciplineMoney = (float) 0;//(getRewardDisciplineMoney(employee.getEmployeeId(), month, year));
        Float totalSalary = baseSalary + allowanceSalary + rewardDisciplineMoney;
        Float tax = (float) calculateTax(totalSalary);
        Float receiveMoney = totalSalary - tax;

        tvEmployeeName.setText(fullName);
        tvPosition.setText(positionName);
        tvPosition2.setText(positionName);
        tvDepartment.setText(departmentName);
        tvBaseSalary.setText(String.format("%,.2f VND", baseSalary));
        tvAllowance.setText(String.format("%,.2f VND", allowanceSalary));
        tvRewardDiscipline.setText(String.format("%,.2f VND", rewardDisciplineMoney));
        tvTotalSalary.setText(String.format("%,.2f VND", totalSalary));
        tvTax.setText(String.format("%,.2f VND", tax));
        tvReceiveSalary.setText(String.format("%,.2f VND", receiveMoney));
        tvReceiveSalary2.setText(String.format("%,.2f VND", receiveMoney));
    }

    private Employee getEmployee(int userId) {
        return (userId == -1) ? null : AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
    }

    private String getPositionName(int positionId) {
        Position position = AppDatabase.getInstance(this).positionDao().getPositionById(positionId);
        return position.getPositionName();
    }

    private String getDepartmentName(int departmentId) {
        Department department = AppDatabase.getInstance(this).departmentDao().getById(departmentId);
        return department.getDepartmentName();
    }

    private float getBaseSalary(int salaryId) {
        Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(salaryId);
        return salary.getBasicSalary() * salary.getCoefficient();
    }

    private float getAllowanceSalary(int salaryId) {
        Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(salaryId);
        return salary.getAllowance();
    }

//    private float getRewardDisciplineMoney(int employeeId, int month, int year) {
//        List<Employee_RewardDiscipline> employeeRewardDisciplines = AppDatabase
//                .getInstance(this)
//                .employeeRewardDisciplineDao()
//                .getByEmployeeIdAndMonthYear(employeeId, month, year);
//
//        if (employeeRewardDisciplines == null || employeeRewardDisciplines.isEmpty()) {
//            return 0.0f;
//        }
//
//        float totalReward = 0;
//        for (Employee_RewardDiscipline rewardDiscipline : employeeRewardDisciplines) {
//            if (rewardDiscipline.getBonus() != null) {
//                totalReward += rewardDiscipline.getBonus();
//            }
//        }
//
//        return totalReward;
//    }

    public static double calculateTax(double tntt) {
        double tax = 0;

        if (tntt <= 5) {
            tax = tntt * 0.05;
        } else if (tntt <= 10) {
            tax = tntt * 0.10 - 250000;
        } else if (tntt <= 18) {
            tax = tntt * 0.15 - 750000;
        } else if (tntt <= 32) {
            tax = tntt * 0.20 - 1650000;
        } else if (tntt <= 52) {
            tax = tntt * 0.25 - 3250000;
        } else if (tntt <= 80) {
            tax = tntt * 0.30 - 5850000;
        } else {
            tax = tntt * 0.35 - 9850000;
        }

        return tax;
    }

    private void initUI() {
        tvEmployeeName = findViewById(R.id.tv_employeename);
        tvPosition = findViewById(R.id.tv_position);
        tvPosition2 = findViewById(R.id.tv_position2);
        tvDepartment = findViewById(R.id.tv_department);
        tvBaseSalary = findViewById(R.id.tv_base_salary);
        tvAllowance = findViewById(R.id.tv_allowance);
        tvRewardDiscipline = findViewById(R.id.tv_employee_reward_discipline);
        tvTotalSalary = findViewById(R.id.tv_total_salary);
        tvTax = findViewById(R.id.tv_tax);
        tvReceiveSalary = findViewById(R.id.tv_receive_money);
        tvReceiveSalary2 = findViewById(R.id.tv_receive_money2);
        btnBack = findViewById(R.id.btn_back);
    }
}
