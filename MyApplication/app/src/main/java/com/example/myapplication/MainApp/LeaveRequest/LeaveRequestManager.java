package com.example.myapplication.MainApp.LeaveRequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

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

        leaveRequestManagerAdapter = new LeaveRequestManagerAdapter(new LeaveRequestManagerAdapter.IClickItem() {
            @Override
            public void approvedLeaveRequest(LeaveRequest leaveRequest) {
                clickApprovedLeaveRequest(leaveRequest);
            }

            @Override
            public void dissapprovedLeaveRequest(LeaveRequest leaveRequest) {
                clickDissapprovedLeaveRequest(leaveRequest);
            }
        });
        leaveRequestManagerAdapter.setData(getLeaveRequests());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvItem.setLayoutManager(linearLayoutManager);

        rcvItem.setAdapter(leaveRequestManagerAdapter);

        btnBack.setOnClickListener(view ->{finish();});
    }

    private List<LeaveRequest> getLeaveRequests() {
        List<LeaveRequest> list = AppDatabase.getInstance(this)
                .leaveRequestDao()
                .getByStatus(0);
        // Reverse the list
        Collections.reverse(list);
        return list;
    }

    private void initUI(){
        rcvItem = findViewById(R.id.rcv_item);
        btnBack = findViewById(R.id.btn_back);
    }

    private void clickDissapprovedLeaveRequest(LeaveRequest leaveRequest){
        new AlertDialog.Builder(this)
                .setTitle("Từ chối")
                .setMessage("Bạn chắc ko?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leaveRequest.setStatus(2);
                        AppDatabase.getInstance(LeaveRequestManager.this).leaveRequestDao().update(leaveRequest);
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private  void clickApprovedLeaveRequest(LeaveRequest leaveRequest){
        new AlertDialog.Builder(this)
                .setTitle("Chấp nhận")
                .setMessage("Bạn chắc ko?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leaveRequest.setStatus(1);
                        AppDatabase.getInstance(LeaveRequestManager.this).leaveRequestDao().update(leaveRequest);
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadData(){
        mListItems = getLeaveRequests();
        leaveRequestManagerAdapter.setData(mListItems);
    }
}
