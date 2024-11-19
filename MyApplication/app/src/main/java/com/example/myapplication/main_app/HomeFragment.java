package com.example.myapplication.main_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

public class HomeFragment extends Fragment {

    private int userId;
    private TextView employeeNameTextView;
    private TextView positionTextView;
    private Button btnEmployeeRequest;
    private Button btnEmployeeLeaveRequest;
    private Button btnLeaveRequestManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);

        getUserID();

        showEmployeeInfo();

        adminButton(userId, "admin", btnEmployeeRequest, btnLeaveRequestManager);

        btnEmployeeRequest.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EmployeeRequestActivity.class);
            startActivity(intent);
        });
        
        btnEmployeeLeaveRequest.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LeaveRequestForm.class);
            intent.putExtra("UserID", userId);
            startActivity(intent);
        });

        return view;
    }

    private void showEmployeeInfo() {
        try {
            if (userId != -1) {
                AppDatabase db = AppDatabase.getInstance(getContext());
                Employee employee = db.employeeDao().getEmployeeByUserId(userId);
                if (employee != null) {
                    employeeNameTextView.setText(employee.getFullName());

                    Integer posId = employee.getPositionId();
                    if(posId != null) {
                        Position position = db.positionDao().getPositionById(employee.getPositionId());
                        positionTextView.setText(position.getPositionName());
                    } else {
                        positionTextView.setText("Không chức vụ");
                    }

                } else {
                    employeeNameTextView = employeeNameTextView.findViewById(R.id.tv_emloyeename);
                    positionTextView = positionTextView.findViewById(R.id.tv_position);
                    employeeNameTextView.setText("Không tìm thấy nhân viên");
                    positionTextView.setText("");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adminButton(int userId, String rolename, Button ... buttons) {
        if (getContext() == null) {
            Log.e("Fragment", "Context is null.");
            return;
        }

        AppDatabase db = AppDatabase.getInstance(getContext());

        // Get RoleID form User table
        int roleId = db.userDao().getUserByRoleId(userId);

        // Get Role name
        Role role = db.roleDao().getRoleById(roleId);

        // VISIBLE admin buttion
        if (role != null && !rolename.equals(role.getRoleName())) {
            for (Button button : buttons) {
                button.setVisibility(View.GONE);
            }
        }
    }

    private void getUserID(){
        Intent intent = requireActivity().getIntent();
        userId = intent.getIntExtra("UserID", -1);
    }

    private void initUI(View view) {
        employeeNameTextView = view.findViewById(R.id.tv_emloyeename);
        positionTextView = view.findViewById(R.id.tv_position);
        btnEmployeeRequest = view.findViewById(R.id.btn_employee_request);
        btnEmployeeLeaveRequest = view.findViewById(R.id.btn_asked_leave_request);
        btnLeaveRequestManager = view.findViewById(R.id.btn_leave_request_manager);
    }
}
