package com.example.myapplication.MainApp.Employee;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainApp.UserAccount.UserAccountManagement;
import com.example.myapplication.MainApp.UserAccount.UserAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeManagement extends AppCompatActivity {

    List<Employee> listEmployee;
    RecyclerView rcvUser;
    EmployeeAdapter employeeAdapter;
    Spinner spinnerType;
    SearchView searchView;
    ExtendedFloatingActionButton fabAdd;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();


    }

    private void loadData() {
        listEmployee = AppDatabase.getInstance(this).employeeDao().getActiveEmployees();
        employeeAdapter.setData(listEmployee);
    }

    private void setupSpinnerType() {
        List<String> data = new ArrayList<>(Arrays.asList(new String[]{"Lọc", "Theo tên", "Theo"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EmployeeManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void bindingView() {
        rcvUser = findViewById(R.id.rcv_user);
        spinnerType = findViewById(R.id.spinner_type);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
        btnBack = findViewById(R.id.btn_back);
    }
}