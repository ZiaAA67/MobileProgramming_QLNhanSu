package com.example.myapplication.MainApp.LeaveRequest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.LeaveRequest;

import java.util.Collections;
import java.util.List;

public class LeaveRequestHistory extends AppCompatActivity {

    private int userId;
    private int employeeId;

    private RecyclerView rcvItem;
    private Button btnBack;
    private Button btnAddLeaveRequest;

    private LeaveRequestHistoryAdapter leaveRequestHistoryAdapter;
    private List<LeaveRequest> mListItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_history);

        userId = getIntent().getIntExtra("UserID", -1);
        employeeId = getEmployeeId(userId);

        initUI();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        leaveRequestHistoryAdapter = new LeaveRequestHistoryAdapter();
        mListItems = getLeaveRequests();
        leaveRequestHistoryAdapter.setData(mListItems);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvItem.setLayoutManager(linearLayoutManager);
        rcvItem.setAdapter(leaveRequestHistoryAdapter);

        btnBack.setOnClickListener(view -> finish());

        btnAddLeaveRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewLeaveRequest.class);
            intent.putExtra("UserID", userId);
            startActivity(intent);
        });
    }

    private List<LeaveRequest> getLeaveRequests() {
        List<LeaveRequest> list = AppDatabase.getInstance(this)
                .leaveRequestDao()
                .getByEmployeeId(employeeId);
        Collections.reverse(list);
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        try {
            mListItems = getLeaveRequests();
            leaveRequestHistoryAdapter.setData(mListItems);
            leaveRequestHistoryAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể tải lại dữ liệu! Lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }
    }

    private int getEmployeeId(int userId) {
        Employee employee = null;
        try {
            employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);

            if (employee == null) {
                Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
                finish();
                return -1;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
            return -1;
        }

        return employee.getEmployeeId();
    }

    private void initUI() {
        rcvItem = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
        btnAddLeaveRequest = findViewById(R.id.btn_request_leave_request);
    }
}
