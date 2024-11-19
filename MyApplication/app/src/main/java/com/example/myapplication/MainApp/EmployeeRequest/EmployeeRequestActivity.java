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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Configuration;
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
        // Setup dialong phân công công việc cho nhân viên mới
        Dialog dialog = new Dialog(EmployeeRequestActivity.this);
        showDialogAssignment(dialog);

        // Department spinner
        Spinner spinnerDepartment = dialog.findViewById(R.id.spinner_department);
        setupSpinnerDepartment(spinnerDepartment);

        // Position spinner
        Spinner spinnerPosition = dialog.findViewById(R.id.spinner_position);
        setupSpinnerPosition(spinnerPosition);

        // Workplace spinner
        Spinner spinnerWorkplace = dialog.findViewById(R.id.spinner_workplace);
        setupSpinnerWorkplace(spinnerWorkplace);

        // Ánh xạ btn duyệt và từ chối
        btnAssignmentApprove = dialog.findViewById(R.id.btn_assignment_approve);
        btnAssignmentDisapprove = dialog.findViewById(R.id.btn_assignment_disapprove);

        // Btn duyệt sau khi phân công
        btnAssignmentApprove.setOnClickListener(view -> {
            int departmentId = spinnerDepartment.getSelectedItemPosition();
            int positionId = spinnerPosition.getSelectedItemPosition();
            int workplaceId = spinnerWorkplace.getSelectedItemPosition();

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

                // Set active và gán thông tin cho employee
                employee.setActive(1);
                employee.setUserId(user.getUserId());
                employee.setDepartmentId( departmentId == 0 ? null : departmentId );
                employee.setPositionId( positionId == 0 ? null : positionId );
                employee.setWorkplaceId( workplaceId == 0 ? null : workplaceId );

                AppDatabase.getInstance(this).employeeDao().update(employee);

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

    private void setupSpinnerDepartment(Spinner spinner) {
        List<String> data = new ArrayList<>();
        List<Department> listDepartment = AppDatabase.getInstance(EmployeeRequestActivity.this).departmentDao().getAll();
        if(listDepartment != null) {
            data.add("Chọn phòng ban");
            listDepartment.forEach(d -> data.add(d.getDepartmentName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(EmployeeRequestActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupSpinnerPosition(Spinner spinner) {
        List<String> data = new ArrayList<>();
        List<Position> listPosition = AppDatabase.getInstance(EmployeeRequestActivity.this).positionDao().getAll();
        if(listPosition != null) {
            data.add("Chọn vị trí của nhân viên");
            listPosition.forEach(d -> data.add(d.getPositionName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(EmployeeRequestActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupSpinnerWorkplace(Spinner spinner) {
        List<String> data = new ArrayList<>();
        List<Workplace> listWorkplace = AppDatabase.getInstance(EmployeeRequestActivity.this).workplaceDao().getAll();
        if(listWorkplace != null) {
            data.add("Chọn cơ sở làm việc");
            listWorkplace.forEach(d -> data.add(d.getWorkplaceName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(EmployeeRequestActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showDialogAssignment(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_assignment_layout);
        dialog.setCancelable(true); // có thể bấm ra ngoài để đóng dialog

        Window window = dialog.getWindow();
        if(window == null) return;

        // Set kích thước và màu nền
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set gravity center
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.show();
    }

    private void bindingView() {
        mListView = findViewById(R.id.list_view);
        btnBack = findViewById(R.id.btn_back);
    }
}