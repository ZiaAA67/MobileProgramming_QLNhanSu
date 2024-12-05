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

import com.example.myapplication.MainApp.Employee.EmployeeManagement;
import com.example.myapplication.MainApp.UserAccount.UserAccountManagement;
import com.example.myapplication.R;

public class Manager extends AppCompatActivity {
    private LinearLayout userManager;
    private LinearLayout employeeManager;
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

        btnBack.setOnClickListener(v -> finish());
    }

    private void bindingView() {
        userManager = findViewById(R.id.user_manager);
        employeeManager = findViewById(R.id.employee_manager);
        btnBack = findViewById(R.id.btn_back);
    }
}