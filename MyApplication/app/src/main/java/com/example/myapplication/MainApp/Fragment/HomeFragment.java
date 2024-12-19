package com.example.myapplication.MainApp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainApp.AttendanceHistory.AttendanceHistory;
import com.example.myapplication.MainApp.EmployeeProfile.EmployeeProfile;
import com.example.myapplication.MainApp.LeaveRequest.LeaveRequestHistory;
import com.example.myapplication.MainApp.Manager;
import com.example.myapplication.MainApp.Salary.SalarySlip;
import com.example.myapplication.MainApp.Timekeeping.NewTimekeeping;
import com.example.myapplication.R;
import com.example.myapplication.MainApp.Stats.Stats;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

public class HomeFragment extends Fragment {

    //    private int userId;
    private User user;
    private TextView employeeNameTextView;
    private TextView positionTextView;
    private ImageView imgEmployee;
    private Button btnLeaveRequestHistory;
    private Button btnEmployeeProfile;
    private Button btnSalarySlip;
    private Button btnManager;
    private Button btnHistory;
    private Button btnTimekeeping;
    private Button btnStats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("User");
        }

        showEmployeeInfo();

        adminButton(user.getUserId(), "Admin", btnManager, btnStats);
        hideButtonInPublicRole(user, btnLeaveRequestHistory, btnSalarySlip, btnTimekeeping);

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

        btnTimekeeping.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NewTimekeeping.class);
            intent.putExtra("UserID", user.getUserId());
            startActivity(intent);
        });

        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Stats.class);
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

                    if (!TextUtils.isEmpty(employee.getImagePath())) {
                        Glide.with(this)
                                .load(employee.getImagePath())
                                .apply(new RequestOptions().circleCrop())
                                .into(imgEmployee);
                    }

                    Integer posId = employee.getPositionId();
                    if (posId != null) {
                        Position position = db.positionDao().getPositionById(employee.getPositionId());
                        positionTextView.setText(position.getPositionName());
                    } else {
                        positionTextView.setText("Chức vụ");
                    }

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
        if (role != null && (!rolename.equals(role.getRoleName()) && !role.getRoleName().equals("Manager"))) {
            for (Button button : buttons) {
                button.setVisibility(View.GONE);
            }
        }
    }

    private void hideButtonInPublicRole(User user, Button... buttons) {
        String roleName = AppDatabase.getInstance(getContext()).roleDao().getRoleById(user.getRoleId()).getRoleName();

        if (roleName.equals("Public")) {
            for (Button button : buttons) {
                button.setVisibility(View.GONE);
            }
        }
    }

    private void initUI(View view) {
        employeeNameTextView = view.findViewById(R.id.tv_emloyeename);
        positionTextView = view.findViewById(R.id.tv_position);
        imgEmployee = view.findViewById(R.id.img_image_employee);
        btnLeaveRequestHistory = view.findViewById(R.id.btn_asked_leave_request);
        btnEmployeeProfile = view.findViewById(R.id.btn_employee_profile);
        btnSalarySlip = view.findViewById(R.id.btn_salary_slip);
        btnManager = view.findViewById(R.id.btn_manager);
        btnHistory = view.findViewById(R.id.btn_history);
        btnTimekeeping = view.findViewById(R.id.btn_checkin);
        btnStats = view.findViewById(R.id.btn_stats);
    }
}
