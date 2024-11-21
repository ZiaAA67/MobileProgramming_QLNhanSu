package com.example.myapplication.MainApp;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.myapplication.Login.ChangePassword;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.Login.GiaoDienLogin;

public class ProfileFragment extends Fragment {

    private int userId;
    private TextView tvEmployeeName;
    private TextView tvUserName;
    private TextView tvPosition;
    private Button btnChangePassword;
    private Button btnSetting;
    private Button btnLogout;
    private Button btnClose;
    private ImageView imageAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            userId = getArguments().getInt("UserID", -1);
        }

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(view);

        showEmployeeInfo();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                intent.putExtra("UserID", userId);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(v -> logout());
        btnClose.setOnClickListener(v -> closeApp());

        return view;
    }

    private void initUI(View view) {
        tvEmployeeName = view.findViewById(R.id.tv_employee_name);
        tvUserName = view.findViewById(R.id.tv_username);
        tvPosition = view.findViewById(R.id.tv_position);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnSetting = view.findViewById(R.id.btn_setting);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnClose = view.findViewById(R.id.btn_closeapp);
        imageAvatar = view.findViewById(R.id.image_avatar);
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

                    Integer posId = employee.getPositionId();
                    if(posId != null) {
                        Position position = db.positionDao().getPositionById(employee.getPositionId());
                        tvPosition.setText(position.getPositionName());
                    } else {
                        tvPosition.setText("Không chức vụ");
                    }

                    String imagePath = employee.getImagePath();
                    if(!imagePath.isEmpty()) {
                        RequestOptions options = new RequestOptions().circleCrop();
                        Glide.with(requireActivity()).load(imagePath).apply(options).into(imageAvatar);
                    }


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

    private void logout() {
        Intent intent = new Intent(requireContext(), GiaoDienLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp hoạt động
        startActivity(intent);
    }

    private void closeApp() {
        // Show dialog
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đóng ứng dụng")
                .setMessage("Bạn có chắc muốn đóng ứng dụng không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    // Close app
                    requireActivity().finishAffinity(); // Đóng toàn bộ các activity trong stack
                    System.exit(0); // Dừng tiến trình ứng dụng
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Close dialog
                    dialog.dismiss();
                })
                .show();
    }
}
