package com.example.myapplication.main_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;

import java.util.List;

public class EmployeeRequestActivity extends AppCompatActivity {

    ListView mListView;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        bindingView();

        // Lấy list ds đơn cần duyệt
        List<Employee> mlist = AppDatabase.getInstance(this).employeeDao().getInactiveEmployees();

        // Set adapter
        RequestEmployeeAdapter profileAdapter = new RequestEmployeeAdapter(EmployeeRequestActivity.this, mlist);
        mListView.setAdapter(profileAdapter);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void bindingView() {
        mListView = findViewById(R.id.list_view);
        btnBack = findViewById(R.id.btn_back);
    }
}