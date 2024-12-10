package com.example.myapplication.MainApp.Department;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainApp.Employee.EmployeeAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;

import java.util.ArrayList;
import java.util.List;

public class DepartmentUpdateItem extends AppCompatActivity {
    private EditText edtDepartmentName;
    private EditText edtEdtDepartmentDesc;
    private Button btnUpdate;
    private Button btnBack;
    private SearchView searchView;
    private RecyclerView rcvEmployee;
    private List<Employee> listEmployee;
    private EmployeeInDepartmentAdapter employeeAdapter;
    private Department department;
    private String oldName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_department_update_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        department = (Department) getIntent().getSerializableExtra("departmentKey");

        bindingView();

        loadDepartmentInfo();

        // Setup list employee in department
        listEmployee = AppDatabase.getInstance(this).employeeDao().getByDepartmentId(department.getDepartmentId());
        employeeAdapter = new EmployeeInDepartmentAdapter(listEmployee);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);
        rcvEmployee.setAdapter(employeeAdapter);


        btnUpdate.setOnClickListener(v -> handleClickUpdateInfo());

        btnBack.setOnClickListener(v -> finish());
    }

    private void handleClickUpdateInfo() {
        String name = edtDepartmentName.getText().toString().trim();
        String description = edtEdtDepartmentDesc.getText().toString().trim();
        if(name.isEmpty()) {
            edtDepartmentName.setError("Vui lòng nhập thông tin!");
            edtDepartmentName.requestFocus();
            return;
        }

        if(name.equals(oldName)) {
            department.setDescription(description);
            AppDatabase.getInstance(this).departmentDao().update(department);
        } else {
            if(!checkDepartmentExist(name)) {
                department.setDepartmentName(name);
                department.setDescription(description);
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                AppDatabase.getInstance(this).departmentDao().update(department);
            } else {
                edtDepartmentName.setError("Phòng ban đã tồn tại");
                edtDepartmentName.requestFocus();
                Toast.makeText(this, "loi", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultKey", "success");
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private boolean checkDepartmentExist(String name) {
        List<Department> list = AppDatabase.getInstance(this).departmentDao().getListDepartmentByName(name);

        if(list.isEmpty()) return false;

        long count = list.stream().filter(Department::isActive).count();
        if(count == 0) return false;

        return true;
    }

    private void loadDepartmentInfo() {
        edtDepartmentName.setText(department.getDepartmentName());
        edtEdtDepartmentDesc.setText(department.getDescription());
        oldName = department.getDepartmentName();
    }

    private void bindingView() {
        edtDepartmentName = findViewById(R.id.edt_department_name);
        edtEdtDepartmentDesc = findViewById(R.id.edt_department_desc);
        btnUpdate = findViewById(R.id.btn_update);
        btnBack = findViewById(R.id.btn_back);
        searchView = findViewById(R.id.search_view);
        rcvEmployee = findViewById(R.id.rcv_employee_in_department);
    }
}