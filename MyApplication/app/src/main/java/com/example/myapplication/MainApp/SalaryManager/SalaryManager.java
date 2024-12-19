package com.example.myapplication.MainApp.SalaryManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SalaryManager extends AppCompatActivity {

    private Button btnBack;
    private LinearLayout exportExcel;
    private LinearLayout salary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_salary);

        initUI();

        btnBack.setOnClickListener(v -> finish());
        exportExcel.setOnClickListener(v -> {
            Intent intent = new Intent(this, SalaryExportExcel.class);
            startActivity(intent);
        });
        salary.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmployeeSalaryManager.class);
            startActivity(intent);
        });

    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        exportExcel = findViewById(R.id.excel_salary_manager);
        salary = findViewById(R.id.employee_salary_manager);
    }
}
