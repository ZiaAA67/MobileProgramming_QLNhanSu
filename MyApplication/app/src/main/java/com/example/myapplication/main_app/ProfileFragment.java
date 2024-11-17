package com.example.myapplication.main_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ChangePassword;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Employee;

public class ProfileFragment extends Fragment {

    private int userId;
    private TextView tvEmployeeName;
    private TextView tvUserName;
    private TextView tvPosition;
    private Button btnChangePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            userId = getArguments().getInt("UserID", -1);
        }

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(view);

        showEmployeeInfo();

//        btnChangePassword.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), ChangePassword.class);
//            intent.putExtra("UserID", userId);
//            startActivity(intent);
//        });

        return view;
    }

    private void initUI(View view) {
        tvEmployeeName = view.findViewById(R.id.tv_employee_name);
        tvUserName = view.findViewById(R.id.tv_username);
        tvPosition = view.findViewById(R.id.tv_position);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
    }

    private void showEmployeeInfo() {
        if (userId != -1) {
            AppDatabase db = AppDatabase.getInstance(getActivity());

            User user = db.userDao().getUserById(userId);
            if (user != null) {
                tvUserName.setText(user.getUsername());

                Employee employee = db.employeeDao().getEmployeeByUserId(userId);
                if (employee != null) {
                    tvEmployeeName.setText(employee.getFullName());

                    Position position = db.positionDao().getPositionById(employee.getPositionId());
                    tvPosition.setText(position != null ? position.getPositionName() : "Không chức vụ");
                } else {
                    tvEmployeeName.setText("Không tìm thấy nhân viên");
                    tvPosition.setText("");
                }
            } else {
                tvUserName.setText("Không tìm thấy người dùng");
                tvEmployeeName.setText("");
                tvPosition.setText("");
            }
        } else {
            tvUserName.setText("Không tìm thấy người dùng");
            tvEmployeeName.setText("");
            tvPosition.setText("");
        }
    }
}
