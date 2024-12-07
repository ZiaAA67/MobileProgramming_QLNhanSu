package com.example.myapplication.MainApp.LeaveRequest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.LeaveRequest;

import java.util.Collections;
import java.util.List;

public class LeaveRequestManager extends AppCompatActivity {

    private int userId;

    private Button btnBack;
    private RecyclerView rcvItem;

    private LeaveRequestManagerAdapter leaveRequestManagerAdapter;
    private List<LeaveRequest> mListItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_manager);

        userId = getIntent().getIntExtra("UserID", -1);

        initUI();
        setupRecyclerView();

        btnBack.setOnClickListener(view -> finish());
    }

    private void initUI() {
        rcvItem = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupRecyclerView() {
        leaveRequestManagerAdapter = new LeaveRequestManagerAdapter(new LeaveRequestManagerAdapter.IClickItem() {
            @Override
            public void approvedLeaveRequest(LeaveRequest leaveRequest) {
                showConfirmationDialog("Chấp nhận", "Bạn chắc chắn muốn chấp nhận yêu cầu nghỉ?", 1, leaveRequest);
            }

            @Override
            public void dissapprovedLeaveRequest(LeaveRequest leaveRequest) {
                showConfirmationDialog("Từ chối", "Bạn chắc chắn muốn từ chối yêu cầu nghỉ?", 2, leaveRequest);
            }
        });

        mListItems = getLeaveRequests();
        leaveRequestManagerAdapter.setData(mListItems);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvItem.setLayoutManager(linearLayoutManager);
        rcvItem.setAdapter(leaveRequestManagerAdapter);
    }

    private List<LeaveRequest> getLeaveRequests() {
        List<LeaveRequest> list = AppDatabase.getInstance(this)
                .leaveRequestDao()
                .getByStatus(0);
        Collections.reverse(list);
        return list;
    }

    private void showConfirmationDialog(String title, String message, int status, LeaveRequest leaveRequest) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    leaveRequest.setStatus(status);
                    AppDatabase.getInstance(LeaveRequestManager.this).leaveRequestDao().update(leaveRequest);
                    loadData();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadData() {
        mListItems = getLeaveRequests();
        leaveRequestManagerAdapter.setData(mListItems);
    }
}
