package com.example.myapplication.main_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRequestActivity extends AppCompatActivity {

    ListView mListView;
    Button btnBack;
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

        // Lấy list ds đơn cần duyệt
        mlist = AppDatabase.getInstance(this).employeeDao().getInactiveEmployees();
        if(mlist.isEmpty()) {
            Toast.makeText(this, "Không có đơn yêu cầu nào cần được duyệt!", Toast.LENGTH_SHORT).show();
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

        btnBack.setOnClickListener(v -> {
            finish();
        });

    }

    private void handleApproveClick(Employee employee) {
        String to = employee.getEmail();
        String sub = "Đăng ký thông tin thành công!!!";

        String username = Configuration.makeUsername(employee.getFullName(), employee.getPhoneNumber());
        String password = Configuration.randomString(10);

        String content =
                "Chúc mừng bạn đã đăng ký thông tin thành công, đây là tài khoản và mật khẩu của bạn. \n" +
                        "Vui lòng đổi mật khẩu trong lần đăng nhập đầu tiên! \n\n" +
                        "Tên tài khoản: " + username + "\n" +
                        "Mật khẩu: " + password + "\n";

        // Gửi mail chứa tài khoản và mật khẩu
        Configuration.sendMail(this, to, sub, content);

        try {
            // Lưu user xuống db
            Role EmployeeRole = AppDatabase.getInstance(this).roleDao().getRoleByName("Employee");
            // check có role là Employee hay chưa, nếu chưa có thì tạo role
            if(EmployeeRole == null) {
                EmployeeRole = new Role("Employee", "Nhân viên quèn");
                AppDatabase.getInstance(this).roleDao().insert(EmployeeRole);
                EmployeeRole = AppDatabase.getInstance(this).roleDao().getRoleByName("Employee");
            }

            // Thêm user vào db
            User user = new User(username, Configuration.md5(password), Configuration.STRING_TODAY, EmployeeRole.getRoleId());
            AppDatabase.getInstance(this).userDao().insert(user);
            user = AppDatabase.getInstance(this).userDao().getUserByUsername(username);

            // Set active và gán user cho employee
            employee.setActive(1);
            employee.setUserId(user.getUserId());
            AppDatabase.getInstance(this).employeeDao().update(employee);

            // Xoá khỏi list chờ duyệt và lưu thay đổi
            mlist.remove(employee);
            profileAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisapproveClick(Employee employee) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cảnh báo!");
        dialog.setMessage("Bạn có chắc chắn muốn từ chối?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mlist.remove(employee);
                AppDatabase.getInstance(EmployeeRequestActivity.this).employeeDao().delete(employee);
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
    }
}