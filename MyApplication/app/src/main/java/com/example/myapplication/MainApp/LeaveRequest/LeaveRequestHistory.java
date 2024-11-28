package com.example.myapplication.MainApp.LeaveRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private RecyclerView rcvItem;
    private Button btnBack;
    private Button btnAddLeaveRequest;

    private LeaveRequestAdapter leaveRequestAdapter;
    private List<LeaveRequest> mListItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_history);

        userId = getIntent().getIntExtra("UserID", -1);
        initUI();

        if (userId != -1) {
            setupRecyclerView();
        } else {
            showToastAndFinish("Không thể xin nghỉ! Nhân viên không tồn tại!");
        }
    }

    private void setupRecyclerView() {
        try {
            leaveRequestAdapter = new LeaveRequestAdapter();
            mListItems = getLeaveRequests(userId);
            leaveRequestAdapter.setData(mListItems);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvItem.setLayoutManager(linearLayoutManager);
            rcvItem.setAdapter(leaveRequestAdapter);

            btnBack.setOnClickListener(view -> finish());

            btnAddLeaveRequest.setOnClickListener(v -> {
                Intent intent = new Intent(this, LeaveRequestForm.class);
                intent.putExtra("UserID", userId);
                startActivity(intent);
            });
        } catch (Exception e) {
            showToastAndFinish("Không thể xin nghỉ! Nhân viên không tồn tại!");
        }
    }

    private void showToastAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    private List<LeaveRequest> getLeaveRequests(int userId) {
        try {
            int employeeId = getEmployeeID(userId);
            List<LeaveRequest> list = AppDatabase.getInstance(this)
                    .leaveRequestDao()
                    .getByEmployeeId(employeeId);
            Collections.reverse(list);
            return list;
        } catch (Exception e) {
            showToastAndFinish("Đã có lỗi khi lấy danh sách!");
            return Collections.emptyList();
        }
    }

    private int getEmployeeID(int userId) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
        return employee != null ? employee.getEmployeeId() : -1;  // Return -1 if employee not found
    }

    private void initUI() {
        rcvItem = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
        btnAddLeaveRequest = findViewById(R.id.btn_request_leave_request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        try {
            mListItems = getLeaveRequests(userId);
            leaveRequestAdapter.setData(mListItems);
            leaveRequestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể tải lại dữ liệu! Lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }
    }
}
