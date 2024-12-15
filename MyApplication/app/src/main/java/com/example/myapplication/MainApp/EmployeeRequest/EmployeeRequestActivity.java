package com.example.myapplication.MainApp.EmployeeRequest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.ShowSpinner;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRequestActivity extends AppCompatActivity {

    ListView mListView;
    Button btnBack;
    Button btnAssignmentApprove;
    Button btnAssignmentDisapprove;
    Button btnApproveAll;
    Button btnDisapproveAll;
    List<Employee> mlist;
    RequestEmployeeAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        bindingView();

        // Lấy list ds đơn cần duyệt ( chưa được duyệt )
        mlist = AppDatabase.getInstance(this).employeeDao().getDisapproveEmployees();
        if(mlist.isEmpty()) {
            Toast.makeText(this, "Không có yêu cầu nào cần duyệt!", Toast.LENGTH_SHORT).show();
        }

        // Set adapter
        profileAdapter = new RequestEmployeeAdapter(EmployeeRequestActivity.this, mlist, new RequestEmployeeAdapter.IClickItemEmployee() {
            @Override
            public void approveEmployee(Employee employee) {
                handleApproveClick(employee);
            }

            @Override
            public void disApproveEmployee(Employee employee) {
                handleDisapproveClick(employee);
            }
        });

        mListView.setAdapter(profileAdapter);

        btnApproveAll.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Cảnh báo!");
            dialog.setMessage("Bạn có chắc chắn muốn duyệt tất cả?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // check có role là User hay chưa, nếu chưa có thì tạo role
                    Role userRole = AppDatabase.getInstance(EmployeeRequestActivity.this).roleDao().getRoleByName("User");
                    if(userRole == null) {
                        userRole = new Role("User", "Can access to the system, have a payslip and can request leave");
                        int roleId = (int)AppDatabase.getInstance(EmployeeRequestActivity.this).roleDao().insertReturnId(userRole);
                        userRole.setRoleId(roleId);
                    }

                    Role finalUserRole = userRole;
                    mlist.forEach(employee -> {
                        employee.setApprove(true);
                        AppDatabase.getInstance(EmployeeRequestActivity.this).employeeDao().update(employee);

                        // thay đổi role user từ public -> user
                        User user = AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().getUserById(employee.getUserId());
                        user.setRoleId(finalUserRole.getRoleId());
                        AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().update(user);
                    });
                    Toast.makeText(EmployeeRequestActivity.this, "Duyệt tất cả thành công!", Toast.LENGTH_SHORT).show();
                    mlist.clear();
                    profileAdapter.notifyDataSetChanged();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.create().show();
        });

        btnDisapproveAll.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Cảnh báo!");
            dialog.setMessage("Bạn có chắc chắn muốn từ chối tất cả?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mlist.forEach(employee -> {
                        employee.setActive(false);
                        AppDatabase.getInstance(EmployeeRequestActivity.this).employeeDao().update(employee);

                        User user = AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().getUserById(employee.getUserId());
                        user.setActive(false);
                        AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().update(user);
                    });
                    Toast.makeText(EmployeeRequestActivity.this, "Từ chối tất cả thành công!", Toast.LENGTH_SHORT).show();
                    mlist.clear();
                    profileAdapter.notifyDataSetChanged();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.create().show();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }


    private void handleApproveClick(Employee employee) {
        // Setup dialong phân công công việc cho nhân viên mới
        Dialog dialog = new Dialog(EmployeeRequestActivity.this);
        int layout = R.layout.dialog_assignment_layout;
        Configuration.showDialog(dialog, layout);

        // Department spinner
        Spinner spinnerDepartment = dialog.findViewById(R.id.spinner_department);
        ShowSpinner.setupSpinnerDepartment(spinnerDepartment, EmployeeRequestActivity.this);

        // Position spinner
        Spinner spinnerPosition = dialog.findViewById(R.id.spinner_position);
        ShowSpinner.setupSpinnerPosition(spinnerPosition, EmployeeRequestActivity.this);

        // Workplace spinner
        Spinner spinnerWorkplace = dialog.findViewById(R.id.spinner_workplace);
        ShowSpinner.setupSpinnerWorkplace(spinnerWorkplace, EmployeeRequestActivity.this);

        // Ánh xạ btn duyệt và từ chối
        btnAssignmentApprove = dialog.findViewById(R.id.btn_assignment_approve);
        btnAssignmentDisapprove = dialog.findViewById(R.id.btn_assignment_disapprove);

        // Btn duyệt sau khi phân công
        btnAssignmentApprove.setOnClickListener(view -> {
            int departmentId = spinnerDepartment.getSelectedItemPosition();
            int positionId = spinnerPosition.getSelectedItemPosition();
            int workplaceId = spinnerWorkplace.getSelectedItemPosition();

            String to = employee.getEmail();
            String sub = "Đơn Đăng Ký Của Bạn Đã Được Duyệt!";
            String content = "<span><strong> Chúc mừng, đơn đăng ký của bạn đã được ban quản trị duyệt thành công &#127881;&#127881;&#127881; </strong></span>\n" +
                            "<p> Bạn có thể bắt đầu công việc ngay bây giờ!!!</p>\n" +
                            "<img src=\"https://res.cloudinary.com/dbmwgavqz/image/upload/v1733932636/congratulations-beautiful-greeting-card-poster-banner_mcbrqu.jpg\"/>";

            // Gửi mail đăng ký thành công
            Configuration.sendMail(this, to, sub, content);

            // Lưu xuống db
            try {
                // update dữ liệu nhân viên
                employee.setApprove(true);
                employee.setDepartmentId( departmentId == 0 ? null : departmentId );
                employee.setPositionId( positionId == 0 ? null : positionId );
                employee.setWorkplaceId( workplaceId == 0 ? null : workplaceId );
                AppDatabase.getInstance(this).employeeDao().update(employee);

                // check có role là User hay chưa, nếu chưa có thì tạo role
                Role userRole = AppDatabase.getInstance(this).roleDao().getRoleByName("User");
                if(userRole == null) {
                    userRole = new Role("User", "Can access to the system, have a payslip and can request leave");
                    int roleId = (int)AppDatabase.getInstance(this).roleDao().insertReturnId(userRole);
                    userRole.setRoleId(roleId);
                }

                // thay đổi role user từ public -> user
                User user = AppDatabase.getInstance(this).userDao().getUserById(employee.getUserId());
                user.setRoleId(userRole.getRoleId());
                AppDatabase.getInstance(this).userDao().update(user);

                // Xoá khỏi list chờ duyệt và lưu thay đổi
                mlist.remove(employee);
                profileAdapter.notifyDataSetChanged();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Đóng dialog
            dialog.dismiss();
        });

        // Btn huỷ phân công
        btnAssignmentDisapprove.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    private void handleDisapproveClick(Employee employee) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cảnh báo!");
        dialog.setMessage("Bạn có chắc chắn muốn từ chối?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                employee.setActive(false);
                AppDatabase.getInstance(EmployeeRequestActivity.this).employeeDao().update(employee);

                User user = AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().getUserById(employee.getUserId());
                user.setActive(false);
                AppDatabase.getInstance(EmployeeRequestActivity.this).userDao().update(user);

                mlist.remove(employee);
                profileAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        dialog.create().show();
    }

    private void bindingView() {
        mListView = findViewById(R.id.list_view);
        btnBack = findViewById(R.id.btn_back);
        btnApproveAll = findViewById(R.id.btn_approve_all);
        btnDisapproveAll = findViewById(R.id.btn_disapprove_all);
    }
}