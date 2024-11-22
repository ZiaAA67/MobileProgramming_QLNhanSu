package com.example.myapplication.MainApp.EmployeeProfile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.EducationLevel;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;

public class EmployeeProfile extends AppCompatActivity {
    private int userId;
    private int employeeId;

    private ImageView imgEmployee;
    private TextView tvFullName;
    private TextView tvID;
    private TextView tvGender;
    private TextView tvBirth;
    private TextView tvIdentityNumber;
    private TextView tvAddress;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private TextView tvStatus;
    private TextView tvSalary;
    private TextView tvDepartment;
    private TextView tvPosition;
    private TextView tvEducationLevel;
    private TextView tvWorkplace;
    private TextView tvUserName;
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        userId = getIntent().getIntExtra("UserID", -1);

        try {
            employeeId = getEmployeeId(userId);

            initUI();

            showEmployeeInfo(getEmployeeId(userId));

            btnBack.setOnClickListener(view -> {
                finish();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Không thể xin thông tin! Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showEmployeeInfo(int employeeId) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);

        String imagePath = employee.getImagePath();
        if (!imagePath.isEmpty()) {
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(this).load(imagePath).apply(options).into(imgEmployee);
        }

        try {
            tvFullName.setText(employee.getFullName() != null ? employee.getFullName() : "Không tìm thấy tên!");
            tvID.setText("Mã nhân viên: " + employee.getEmployeeId());
            tvGender.setText("Giới tính: " + strGender(employee.getGender()));
            tvBirth.setText("Ngày sinh: " + (employee.getBirth() != null ? employee.getBirth() : "Không rõ"));
            tvIdentityNumber.setText("CCCD: " + (employee.getIdentityNumber() != null ? employee.getIdentityNumber() : "Không rõ"));
            tvAddress.setText("Địa chỉ: " + (employee.getAddress() != null ? employee.getAddress() : "Không rõ"));
            tvPhoneNumber.setText("Số điện thoại: " + (employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "Không rõ"));
            tvEmail.setText("Email: " + (employee.getEmail() != null ? employee.getEmail() : "Không rõ"));
            tvSalary.setText("Lương cứng: " + (employee.getSalaryId() != null ? employee.getSalaryId() : "N/A"));
            tvDepartment.setText("Phòng ban: " + (employee.getDepartmentId() != null ? strDepartment(employee.getDepartmentId()) : "Không rõ"));
            tvPosition.setText("Chức vụ: " + (employee.getPositionId() != null ? strPosition(employee.getPositionId()) : "Không rõ"));
            tvEducationLevel.setText("Học vấn: " + (employee.getEducationId() != null ? strEducation(employee.getEducationId()) : "Không rõ"));
            tvWorkplace.setText("Nơi làm việc: " + (employee.getWorkplaceId() != null ? strWorkplace(employee.getWorkplaceId()) : "Không rõ"));
            tvUserName.setText("Tên user: " + (employee.getUserId() != null ? strUserName(employee.getUserId()) : "Không rõ"));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hiển thị thông tin!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String strGender(int genderid) {
        if (genderid == 0) {
            return "Nam";
        } else if (genderid == 1) {
            return "Nữ";
        } else {
            return "Khác";
        }
    }

    private String strStatus(Integer status) {
        if (status != null) {
            if (status == 0) {
                return "Ngừng hoạt động";
            } else if (status == 1) {
                return "Hoạt động";
            } else {
                return "Không rõ";
            }
        }
        return "Lỗi trạng thái!";
    }

    private String strDepartment(Integer departmentId) {
        if (departmentId != null) {
            Department department = AppDatabase.getInstance(this).departmentDao().getById(departmentId);
            return department.getDepartmentName();
        }
        return "Không rõ!";
    }

    private String strPosition(Integer positionId) {
        if (positionId != null) {
            Position position = AppDatabase.getInstance(this).positionDao().getPositionById(positionId);
            return position.getPositionName();
        }
        return "Không rõ!";
    }

    private String strEducation(Integer educationId) {
        if (educationId != null) {
            EducationLevel educationLevel = AppDatabase.getInstance(this).educationLevelDao().getById(educationId);
            return educationLevel.getEducationLevelName();
        }
        return "Không rõ!";
    }

    private String strWorkplace(Integer workplaceId) {
        if (workplaceId != null) {
            Workplace workplace = AppDatabase.getInstance(this).workplaceDao().getWorkplaceById(workplaceId);
            return workplace.getWorkplaceName();
        }
        return "Không rõ";
    }

    private String strUserName(Integer userId) {
        if (userId != null) {
            User user = AppDatabase.getInstance(this).userDao().getUserById(userId);
            return user.getUsername();
        }
        return "Không rõ";
    }

    private int getEmployeeId(int userId) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
        if (employee == null) {
            throw new RuntimeException("Không tìm thấy: " + userId);
        }
        return employee.getEmployeeId();
    }

    private void initUI() {
        imgEmployee = findViewById(R.id.img_image_employee);
        tvFullName = findViewById(R.id.tv_full_name);
        tvID = findViewById(R.id.tv_employeeID);
        tvGender = findViewById(R.id.tv_gender);
        tvBirth = findViewById(R.id.tv_birth);
        tvIdentityNumber = findViewById(R.id.tv_identity_number);
        tvAddress = findViewById(R.id.tv_address);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvEmail = findViewById(R.id.tv_email);
        tvStatus = findViewById(R.id.tv_active);
        tvSalary = findViewById(R.id.tv_salary);
        tvDepartment = findViewById(R.id.tv_department);
        tvPosition = findViewById(R.id.tv_position);
        tvEducationLevel = findViewById(R.id.tv_education);
        tvWorkplace = findViewById(R.id.tv_workplace);
        tvUserName = findViewById(R.id.tv_userId);
        btnBack = findViewById(R.id.btn_back);
    }
}
