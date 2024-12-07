package com.example.myapplication.MainApp.RewardsDiscipline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EmployeeRewardDiscipline extends AppCompatActivity {

    private int userId;

    private Button btnBack;
    private Button btnAddRewardDiscipline;
    private EditText edtSearch;

    private RecyclerView rcvEmployee;
    private EmployeeRewardDisciplineAdapter employeeRewardDisciplineAdapter;
    private List<Employee> mListEmployee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_discipline);

        userId = getIntent().getIntExtra("UserID", -1);
        initUI();

        employeeRewardDisciplineAdapter = new EmployeeRewardDisciplineAdapter();
        mListEmployee = new ArrayList<>();
        employeeRewardDisciplineAdapter.setData(mListEmployee, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);

        rcvEmployee.setAdapter(employeeRewardDisciplineAdapter);

        getList();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnAddRewardDiscipline.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRewardDiscipline.class);
            intent.putExtra("UserID", userId);
            startActivity(intent);
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = edtSearch.getText().toString();
                if (!searchQuery.isEmpty()) {
                    handleSearchEmployee();
                    Toast.makeText(getApplicationContext(), "Tìm kiếm: " + searchQuery, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getList() {
        mListEmployee = AppDatabase.getInstance(this).employeeDao().getApproveEmployees();
        if (mListEmployee.isEmpty()) {
            Log.e("RewardsDiscipline", "Danh sách nhân viên trống!");
        }
        employeeRewardDisciplineAdapter.setData(mListEmployee, this);
    }

    private void handleSearchEmployee() {
        String strKeyWord = edtSearch.getText().toString().trim();
        mListEmployee = new ArrayList<>();
        mListEmployee = AppDatabase.getInstance(this).employeeDao().searchEmployeeName(strKeyWord);
        employeeRewardDisciplineAdapter.setData(mListEmployee, this);
    }

    private void initUI() {
        rcvEmployee = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
        btnAddRewardDiscipline = findViewById(R.id.btn_add_reward_discipline);
        edtSearch = findViewById(R.id.edt_search);
    }
}
