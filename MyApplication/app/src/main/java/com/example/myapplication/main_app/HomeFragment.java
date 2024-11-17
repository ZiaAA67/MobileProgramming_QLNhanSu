package com.example.myapplication.main_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;

public class HomeFragment extends Fragment {

    private int userId;
    private TextView employeeNameTextView;
    private TextView positionTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(view);

        // Lấy userId từ Bundle
        if (getArguments() != null) {
            userId = getArguments().getInt("UserID", -1);
        }

        showEmployeeInfo();

        return view;
    }

    private void showEmployeeInfo() {
        if (userId != -1) {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Employee employee = db.employeeDao().getEmployeeByUserId(userId);

            if (employee != null) {
                Position position = db.positionDao().getPositionById(employee.getPositionId());

                employeeNameTextView = employeeNameTextView.findViewById(R.id.tv_emloyeename);
                positionTextView = positionTextView.findViewById(R.id.tv_position);

                employeeNameTextView.setText(employee.getFullName());
                positionTextView.setText(position != null ? position.getPositionName() : "Không chức vụ");
            } else {
                employeeNameTextView = employeeNameTextView.findViewById(R.id.tv_emloyeename);
                positionTextView = positionTextView.findViewById(R.id.tv_position);
                employeeNameTextView.setText("Không tìm thấy nhân viên");
                positionTextView.setText("");
            }
        }
    }

    private void initUI(View view) {
        employeeNameTextView = view.findViewById(R.id.tv_emloyeename);
        positionTextView = view.findViewById(R.id.tv_position);
    }
}
