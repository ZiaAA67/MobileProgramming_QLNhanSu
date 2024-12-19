package com.example.myapplication.MainApp.SalaryManager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSalaryManager extends AppCompatActivity {

    private Button btnBack;
    private SearchView searchView;
    private RecyclerView rcvItem;

    private EmployeeSalaryManagerAdapter employeeSalaryManagerAdapter;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_salary);

        initUI();

        btnBack.setOnClickListener(v -> finish());

        setupRecycleview();
        setupSearchView();
    }

    private void setupRecycleview() {
        try {
            employeeList = AppDatabase.getInstance(this).employeeDao().getAllEmployees();

            employeeSalaryManagerAdapter = new EmployeeSalaryManagerAdapter(this, employee -> {
                DialogAddSalary dialogAddSalary = new DialogAddSalary(this, employee);

                dialogAddSalary.setOnDismissListener(dialog -> {
                    employeeList = AppDatabase.getInstance(this).employeeDao().getAllEmployees(); // Làm mới dữ liệu
                    employeeSalaryManagerAdapter.setData(this, employeeList); // Cập nhật adapter
                    employeeSalaryManagerAdapter.notifyDataSetChanged(); // Làm mới RecyclerView
                });

                dialogAddSalary.show();
            });

            employeeSalaryManagerAdapter.setData(this, employeeList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvItem.setLayoutManager(linearLayoutManager);
            rcvItem.setAdapter(employeeSalaryManagerAdapter);

        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEmployeeList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEmployeeList(newText);
                return true;
            }
        });
    }

    private void filterEmployeeList(String query) {
        if (employeeList == null || employeeList.isEmpty()) {
            return;
        }

        List<Employee> filteredList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getFullName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(employee);
            }
        }

        employeeSalaryManagerAdapter.setData(this, filteredList);
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        searchView = findViewById(R.id.search_view);
        rcvItem = findViewById(R.id.rcv_employee);
    }
}
