package com.example.myapplication.MainApp.RewardsDiscipline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class RewardsDiscipline extends AppCompatActivity {
    private int userId;

    private Button btnBack;
    private Button btnAddRewardDiscipline;
    private EditText edtSearch;

    private RecyclerView rcvEmployee;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> mListEmployee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_discipline);

        userId = getIntent().getIntExtra("UserID", -1);
        initUI();

        employeeAdapter = new EmployeeAdapter();
        mListEmployee = new ArrayList<>();
        employeeAdapter.setData(mListEmployee, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);

        rcvEmployee.setAdapter(employeeAdapter);

        getList();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnAddRewardDiscipline.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRewardsDiscipline.class);
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
        employeeAdapter.setData(mListEmployee, this);
    }

    private void handleSearchEmployee() {
        String strKeyWord = edtSearch.getText().toString().trim();
        mListEmployee = new ArrayList<>();
        mListEmployee = AppDatabase.getInstance(this).employeeDao().searchEmployeeName(strKeyWord);
        employeeAdapter.setData(mListEmployee, this);
        //hideOfKeyBoard();
    }

    private void hideOfKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void initUI() {
        rcvEmployee = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
        btnAddRewardDiscipline = findViewById(R.id.btn_add_reward_discipline);
        edtSearch = findViewById(R.id.edt_search);
    }
}
