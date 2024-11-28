package com.example.myapplication.MainApp.EmployeeProfile;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.myapplication.database.entities.Salary;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;

import java.text.DecimalFormat;

public class EmployeeProfile extends AppCompatActivity {
    private int userId;
    private int employeeId;

    private ImageView imgEmployee;
    private TextView tvFullName, tvID, tvGender, tvBirth, tvIdentityNumber, tvAddress, tvPhoneNumber,
            tvEmail, tvSalary, tvDepartment, tvPosition, tvEducationLevel, tvWorkplace, tvUserName;
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        userId = getIntent().getIntExtra("UserID", -1);

        initUI();

        try {
            employeeId = fetchEmployeeId(userId);
            displayEmployeeInfo(employeeId);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể xin thông tin! Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(view -> finish());
    }

    private void displayEmployeeInfo(int employeeId) {
        try {
            Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);

            if (employee == null) throw new RuntimeException("Nhân viên không tồn tại!");

            if (!TextUtils.isEmpty(employee.getImagePath())) {
                Glide.with(this)
                        .load(employee.getImagePath())
                        .apply(new RequestOptions().circleCrop())
                        .into(imgEmployee);
            }

            safeTextViewSetText(tvFullName, employee.getFullName(), "Không tìm thấy tên!");
            safeTextViewSetText(tvID, "Mã nhân viên: " + employee.getEmployeeId());
            safeTextViewSetText(tvGender, "Giới tính: " + parseGender(employee.getGender()));
            safeTextViewSetText(tvBirth, "Ngày sinh: " + optionalValue(employee.getBirth(), "Không rõ"));
            safeTextViewSetText(tvIdentityNumber, "CCCD: " + optionalValue(employee.getIdentityNumber(), "Không rõ"));
            safeTextViewSetText(tvAddress, "Địa chỉ: " + optionalValue(employee.getAddress(), "Không rõ"));
            safeTextViewSetText(tvPhoneNumber, "Số điện thoại: " + optionalValue(employee.getPhoneNumber(), "Không rõ"));
            safeTextViewSetText(tvEmail, "Email: " + optionalValue(employee.getEmail(), "Không rõ"));
            safeTextViewSetText(tvSalary, "Lương cứng: " + getSalary(employee.getSalaryId()));
            safeTextViewSetText(tvDepartment, "Phòng ban: " + fetchDepartmentName(employee.getDepartmentId()));
            safeTextViewSetText(tvPosition, "Chức vụ: " + fetchPositionName(employee.getPositionId()));
            safeTextViewSetText(tvEducationLevel, "Học vấn: " + fetchEducationName(employee.getEducationId()));
            safeTextViewSetText(tvWorkplace, "Nơi làm việc: " + fetchWorkplaceName(employee.getWorkplaceId()));
            safeTextViewSetText(tvUserName, "Tên user: " + fetchUserName(employee.getUserId()));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hiển thị thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getSalary(Integer salaryId) {
        Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(salaryId);
        if (salary != null && salary.getBasicSalary() != null) {
            float basicSalary = salary.getBasicSalary();
            DecimalFormat formatter = new DecimalFormat("#,###.###");
            return formatter.format(basicSalary);
        } else {
            return "N/A";
        }
    }

    private int fetchEmployeeId(int userId) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
        if (employee == null)
            throw new RuntimeException("Không tìm thấy nhân viên với UserID: " + userId);
        return employee.getEmployeeId();
    }

    private String fetchDepartmentName(Integer departmentId) {
        if (departmentId != null) {
            Department department = AppDatabase.getInstance(this).departmentDao().getById(departmentId);
            return department != null ? department.getDepartmentName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String fetchPositionName(Integer positionId) {
        if (positionId != null) {
            Position position = AppDatabase.getInstance(this).positionDao().getPositionById(positionId);
            return position != null ? position.getPositionName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String fetchEducationName(Integer educationId) {
        if (educationId != null) {
            EducationLevel educationLevel = AppDatabase.getInstance(this).educationLevelDao().getById(educationId);
            return educationLevel != null ? educationLevel.getEducationLevelName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String fetchWorkplaceName(Integer workplaceId) {
        if (workplaceId != null) {
            Workplace workplace = AppDatabase.getInstance(this).workplaceDao().getWorkplaceById(workplaceId);
            return workplace != null ? workplace.getWorkplaceName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String fetchUserName(Integer userId) {
        if (userId != null) {
            User user = AppDatabase.getInstance(this).userDao().getUserById(userId);
            return user != null ? user.getUsername() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String parseGender(int genderId) {
        switch (genderId) {
            case 0:
                return "Nam";
            case 1:
                return "Nữ";
            default:
                return "Khác";
        }
    }

    private String optionalValue(Object value, String defaultValue) {
        if (value instanceof Integer) {
            return value != null ? String.valueOf(value) : defaultValue;
        }
        return value != null ? value.toString() : defaultValue;
    }

    private void safeTextViewSetText(TextView textView, String text, String defaultValue) {
        textView.setText(TextUtils.isEmpty(text) ? defaultValue : text);
    }

    private void safeTextViewSetText(TextView textView, String text) {
        safeTextViewSetText(textView, text, "Không rõ");
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
        tvSalary = findViewById(R.id.tv_salary);
        tvDepartment = findViewById(R.id.tv_department);
        tvPosition = findViewById(R.id.tv_position);
        tvEducationLevel = findViewById(R.id.tv_education);
        tvWorkplace = findViewById(R.id.tv_workplace);
        tvUserName = findViewById(R.id.tv_userId);
        btnBack = findViewById(R.id.btn_back);
    }
}
