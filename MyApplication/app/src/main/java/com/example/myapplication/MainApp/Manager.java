package com.example.myapplication.MainApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.MainApp.Department.DepartmentManagement;
import com.example.myapplication.MainApp.Employee.EmployeeManagement;
import com.example.myapplication.MainApp.EmployeeRequest.EmployeeRequestActivity;
import com.example.myapplication.MainApp.LeaveRequest.LeaveRequestManager;
import com.example.myapplication.MainApp.RewardsDiscipline.RewardsDiscipline;
import com.example.myapplication.MainApp.SalaryManager.SalaryExportExcel;
import com.example.myapplication.MainApp.TimekeepingManager.TimekeepingExportExcel;
import com.example.myapplication.MainApp.UserAccount.UserAccountManagement;
import com.example.myapplication.MainApp.Workplace.WorkplaceManagement;
import com.example.myapplication.R;

public class Manager extends AppCompatActivity {
    private LinearLayout userManager;
    private LinearLayout employeeManager;
    private LinearLayout departmentManager;
    private LinearLayout workplaceManager;
    private LinearLayout timekeepingManager;
    private LinearLayout requestEmployeeManager;
    private LinearLayout rewardDiscipline;
    private LinearLayout leaveRequestManager;
    private LinearLayout salaryManager;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        userManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserAccountManagement.class);
            startActivity(intent);
        });

        employeeManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmployeeManagement.class);
            startActivity(intent);
        });

        departmentManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, DepartmentManagement.class);
            startActivity(intent);
        });

        workplaceManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkplaceManagement.class);
            startActivity(intent);
        });

        timekeepingManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimekeepingExportExcel.class);
            startActivity(intent);
        });

        requestEmployeeManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmployeeRequestActivity.class);
            startActivity(intent);
        });

        leaveRequestManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaveRequestManager.class);
            startActivity(intent);
        });

        rewardDiscipline.setOnClickListener(v -> {
            Intent intent = new Intent(this, RewardsDiscipline.class);
            startActivity(intent);
        });

        salaryManager.setOnClickListener(v -> {
            Intent intent = new Intent(this, SalaryExportExcel.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void bindingView() {
        userManager = findViewById(R.id.user_manager);
        employeeManager = findViewById(R.id.employee_manager);
        departmentManager = findViewById(R.id.depaerment_manager);
        workplaceManager = findViewById(R.id.workplace_manager);
        timekeepingManager = findViewById(R.id.timekeeping_manager);
        requestEmployeeManager = findViewById(R.id.request_employee_manager);
        leaveRequestManager = findViewById(R.id.leave_request_manager);
        rewardDiscipline = findViewById(R.id.reward_discipline);
        salaryManager = findViewById(R.id.salary_manager);
        btnBack = findViewById(R.id.btn_back);
    }
}