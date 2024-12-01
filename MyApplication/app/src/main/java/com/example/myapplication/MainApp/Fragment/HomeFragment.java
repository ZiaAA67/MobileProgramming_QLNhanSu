package com.example.myapplication.MainApp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainApp.AttendanceHistory.AttendanceHistory;
import com.example.myapplication.MainApp.EmployeeProfile.EmployeeProfile;
import com.example.myapplication.MainApp.EmployeeRequest.EmployeeRequestActivity;
import com.example.myapplication.MainApp.LeaveRequest.LeaveRequestHistory;
import com.example.myapplication.MainApp.LeaveRequest.LeaveRequestManager;
import com.example.myapplication.MainApp.Manager;
import com.example.myapplication.MainApp.RewardsDiscipline.RewardsDiscipline;
import com.example.myapplication.MainApp.Salary.SalarySlip;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;

public class HomeFragment extends Fragment {

    //    private int userId;
    private User user;
    private TextView employeeNameTextView;
    private TextView positionTextView;
    private Button btnEmployeeRequest;
    private Button btnLeaveRequestHistory;
    private Button btnLeaveRequestManager;
    private Button btnEmployeeProfile;
    private Button btnRewardDiscipline;
    private Button btnSalarySlip;
    private Button btnManager;
    private Button btnHistory;
    private Button btnCalender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);

//        userId = intent.getIntExtra("UserID", -1);
//        user = (User) requireActivity().getIntent().getSerializableExtra("User");

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
        }

        showEmployeeInfo();

        adminButton(user.getUserId(), "Admin", btnEmployeeRequest, btnLeaveRequestManager, btnRewardDiscipline);

        btnEmployeeRequest.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EmployeeRequestActivity.class);
            startActivity(intent);
        });

        btnLeaveRequestHistory.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LeaveRequestHistory.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnEmployeeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EmployeeProfile.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnLeaveRequestManager.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LeaveRequestManager.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnRewardDiscipline.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), RewardsDiscipline.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnSalarySlip.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SalarySlip.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnManager.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Manager.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AttendanceHistory.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });


        return view;
    }

    private void showEmployeeInfo() {
        try {
            if (user != null) {
                AppDatabase db = AppDatabase.getInstance(getContext());
                Employee employee = db.employeeDao().getEmployeeByUserId(user.getUserId());
                if (employee != null) {
                    employeeNameTextView.setText(employee.getFullName());

                    Integer posId = employee.getPositionId();
                    if (posId != null) {
                        Position position = db.positionDao().getPositionById(employee.getPositionId());
                        positionTextView.setText(position.getPositionName());
                    }
//                    else {
//                        positionTextView.setText("Không chức vụ");
//                    }

                }
//                else {
//                    employeeNameTextView = employeeNameTextView.findViewById(R.id.tv_emloyeename);
//                    positionTextView = positionTextView.findViewById(R.id.tv_position);
//                    employeeNameTextView.setText("Không tìm thấy nhân viên");
//                    positionTextView.setText("");
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adminButton(int userId, String rolename, Button... buttons) {
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

    private void initUI(View view) {
        employeeNameTextView = view.findViewById(R.id.tv_emloyeename);
        positionTextView = view.findViewById(R.id.tv_position);
        btnEmployeeRequest = view.findViewById(R.id.btn_employee_request);
        btnLeaveRequestHistory = view.findViewById(R.id.btn_asked_leave_request);
        btnLeaveRequestManager = view.findViewById(R.id.btn_leave_request_manager);
        btnEmployeeProfile = view.findViewById(R.id.btn_employee_profile);
        btnRewardDiscipline = view.findViewById(R.id.btn_reward_discipline);
        btnSalarySlip = view.findViewById(R.id.btn_salary_slip);
        btnManager = view.findViewById(R.id.btn_manager);
        btnHistory = view.findViewById(R.id.btn_history);
        btnCalender = view.findViewById(R.id.btn_user_avatar);
    }
}
