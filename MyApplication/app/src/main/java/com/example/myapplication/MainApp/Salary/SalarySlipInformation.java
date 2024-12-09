package com.example.myapplication.MainApp.Salary;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_RewardDiscipline;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Salary;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Timekeeping;
import com.example.myapplication.database.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalarySlipInformation extends AppCompatActivity {

    private int userId;
    private int month;
    private int year;

    private ImageView imgEmployee;
    private Button btnBack;
    private TextView tvEmployeeName;
    private TextView tvMonthYear;
    private TextView tvPosition;
    private TextView tvPosition2;
    private TextView tvDepartment;
    private TextView tvOvertime;
    private TextView tvBaseSalary;
    private TextView tvAllowance;
    private TextView tvRewardDiscipline;
    private TextView tvTax;
    private TextView tvTotalSalary;
    private TextView tvReceiveSalary;
    private TextView tvReceiveSalary2;

    private List<Employee_RewardDiscipline> employeeRewardDisciplines;

    private int currentMonth = LocalDate.now().getMonthValue();
    private int currentYear = LocalDate.now().getYear();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salary_slip_information);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();

        userId = getIntent().getIntExtra("UserID", -1);
        month = getIntent().getIntExtra("Month", -1);
        year = getIntent().getIntExtra("Year", -1);

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showEmployeeSalary();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void showEmployeeSalary() {
        Employee employee = getEmployee(userId);
        if (employee == null) {
            Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy ngày gia nhập công ty, không xem trước khi gia nhập công ty
        User user = AppDatabase.getInstance(this).userDao().getUserById(userId);
        String joinDate = user.getCreateDate();

        if (joinDate != null) {
            String[] joinDateParts = joinDate.split("/");
            int joinDay = Integer.parseInt(joinDateParts[0]);
            int joinMonth = Integer.parseInt(joinDateParts[1]);
            int joinYear = Integer.parseInt(joinDateParts[2]);

            if ((year < joinYear) || (year == joinYear && month < joinMonth)) {
                Toast.makeText(this, "Lương chưa được cấp cho tháng này", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // Không xem lương tháng sau
        int currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        if ((year > currentYear) || (year == currentYear && month > currentMonth)) {
            Toast.makeText(this, "Không thể xem lương của tháng sau", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!TextUtils.isEmpty(employee.getImagePath())) {
            Glide.with(this)
                    .load(employee.getImagePath())
                    .apply(new RequestOptions().circleCrop())
                    .into(imgEmployee);
        }

        String fullName = (employee.getFullName() != null) ? employee.getFullName() : "Không có tên";
        String positionName = (employee.getPositionId() != null) ? getPositionName(employee.getPositionId()) : "Không có chức vụ";
        String departmentName = (employee.getDepartmentId() != null) ? getDepartmentName(employee.getDepartmentId()) : "Không có phòng ban";
        Float overtime = getEmployeeOvertime(employee.getEmployeeId(), month, year);
        Float baseSalary = (employee.getSalaryId() != null) ? getBaseSalary(employee.getSalaryId()) : 0;
        Float allowanceSalary = (employee.getSalaryId() != null) ? getAllowanceSalary(employee.getSalaryId()) : 0;
        Float rewardDisciplineMoney = (getRewardDisciplineMoney(employee.getEmployeeId(), month, year));
        Float totalSalary = baseSalary + allowanceSalary + rewardDisciplineMoney;
        Float tax = (float) calculateTax(totalSalary);
        Float receiveMoney = totalSalary - tax;

        tvEmployeeName.setText(fullName);
        tvMonthYear.setText(String.format("%02d/%d", month, year));
        tvPosition.setText(positionName);
        tvPosition2.setText(positionName);
        tvDepartment.setText(departmentName);
        tvOvertime.setText(String.format("%.1f giờ", overtime));
        tvBaseSalary.setText(String.format("%,.0f VND", baseSalary));
        tvAllowance.setText(String.format("%,.0f VND", allowanceSalary));
        tvRewardDiscipline.setText(String.format("%,.0f VND", rewardDisciplineMoney));
        tvTotalSalary.setText(String.format("%,.0f VND", totalSalary));
        tvTax.setText(String.format("%,.0f VND", tax));
        tvReceiveSalary.setText(String.format("%,.0f VND", receiveMoney));
        tvReceiveSalary2.setText(String.format("%,.0f VND", receiveMoney));
    }

    private Employee getEmployee(int userId) {
        return (userId == -1) ? null : AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
    }

    private String getPositionName(int positionId) {
        Position position = AppDatabase.getInstance(this).positionDao().getPositionById(positionId);
        return position.getPositionName();
    }

    private String getDepartmentName(int departmentId) {
        Department department = AppDatabase.getInstance(this).departmentDao().getById(departmentId);
        return department.getDepartmentName();
    }

    private float getBaseSalary(int salaryId) {
        Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(salaryId);
        return salary.getBasicSalary() * salary.getCoefficient();
    }

    private float getAllowanceSalary(int salaryId) {
        Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(salaryId);
        return salary.getAllowance();
    }

    private float getRewardDisciplineMoney(int employeeId, int month, int year) {
        String strMonthAndYear = month + "/" + year;
        List<Employee_RewardDiscipline> employeeRewardDisciplines = AppDatabase
                .getInstance(this)
                .employeeRewardDisciplineDao()
                .getByEmployeeIdAndMonthYear(employeeId, strMonthAndYear);

        if (employeeRewardDisciplines == null || employeeRewardDisciplines.isEmpty()) {
            return 0.0f;
        }

        float totalReward = 0;
        for (Employee_RewardDiscipline rewardDiscipline : employeeRewardDisciplines) {
            if (rewardDiscipline.getBonus() != null) {
                totalReward += rewardDiscipline.getBonus();
            }
        }

        return totalReward;
    }

    private float getEmployeeOvertime(int employeeId, int month, int year) {
        float totalOvertime = 0;

        try {
            List<Session> sessions = new ArrayList<>();
            List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                    .employeeSessionDao().getSessionByEmployeeId(employeeId);

            for (Employee_Session employeeSession : employeeSessions) {
                Session session = AppDatabase.getInstance(this).sessionDao()
                        .getSessionById(employeeSession.getSessionID());

                if (session.getMonth() == month && session.getYear() == year) {
                    sessions.add(session);
                }
            }

            for (Session session : sessions) {
                List<Timekeeping> timekeepings = AppDatabase.getInstance(this)
                        .timekeepingDao().getTimekeepingBySessionId(session.getSessionId());

                for (Timekeeping timekeeping : timekeepings) {
                    totalOvertime += timekeeping.getOvertime();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tính overtime~", Toast.LENGTH_SHORT).show();
        }

        return totalOvertime / 60;
    }


    // Tính thuế thu nhập
    public static double calculateTax(double tntt) {
        double tax = 0;

        if (tntt <= 5_000_000) {
            tax = tntt * 0.05;
        } else if (tntt <= 10_000_000) {
            tax = tntt * 0.10 - 250_000;
        } else if (tntt <= 18_000_000) {
            tax = tntt * 0.15 - 750_000;
        } else if (tntt <= 32_000_000) {
            tax = tntt * 0.20 - 1_650_000;
        } else if (tntt <= 52_000_000) {
            tax = tntt * 0.25 - 3_250_000;
        } else if (tntt <= 80_000_000) {
            tax = tntt * 0.30 - 5_850_000;
        } else {
            tax = tntt * 0.35 - 9_850_000;
        }

        return tax;
    }

    private void initUI() {
        imgEmployee = findViewById(R.id.img_image_employee);
        tvEmployeeName = findViewById(R.id.tv_employeename);
        tvMonthYear = findViewById(R.id.tv_month_and_year);
        tvPosition = findViewById(R.id.tv_position);
        tvPosition2 = findViewById(R.id.tv_position2);
        tvDepartment = findViewById(R.id.tv_department);
        tvOvertime = findViewById(R.id.tv_overtime);
        tvBaseSalary = findViewById(R.id.tv_base_salary);
        tvAllowance = findViewById(R.id.tv_allowance);
        tvRewardDiscipline = findViewById(R.id.tv_employee_reward_discipline);
        tvTotalSalary = findViewById(R.id.tv_total_salary);
        tvTax = findViewById(R.id.tv_tax);
        tvReceiveSalary = findViewById(R.id.tv_receive_money);
        tvReceiveSalary2 = findViewById(R.id.tv_receive_money2);
        btnBack = findViewById(R.id.btn_back);
    }
}
