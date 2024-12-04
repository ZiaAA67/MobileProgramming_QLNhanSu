package com.example.myapplication.MainApp.Timekeeping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Timekeeping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewTimekeeping extends AppCompatActivity {

    private Button btnBack;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private String currentMode = "In";
    private int userId;
    private Timekeeping timekeeping;

    LocalDate today = LocalDate.now();
    int day = today.getDayOfMonth();
    int month = today.getMonthValue();
    int year = today.getYear();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);

        initUI();

        userId = getIntent().getIntExtra("UserID", -1);
        timekeeping = new Timekeeping();

        // Tạo session nếu cần thiết
        //createSessionIfNeeded();

        // Khôi phục trạng thái Timekeeping từ SharedPreferences
        restoreTimekeepingState();

        // Kiểm tra session cho ngày hôm nay
        String sessionIdStatus = (getSessionForToday(day, month, year) == null) ? "Không có!" : "Có!";
        Toast.makeText(this, "Session ID " + sessionIdStatus, Toast.LENGTH_SHORT).show();

        // Xử lý sự kiện chấm công
        btnCheckIn.setOnClickListener(v -> checkIn());
        btnCheckOut.setOnClickListener(v -> checkOut());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnCheckIn = findViewById(R.id.btn_checkin);
        btnCheckOut = findViewById(R.id.btn_checkout);
    }

    private void createSessionIfNeeded() {
        Session session = getSessionForToday(day, month, year);

        if (session == null) {
            // Tạo session mới nếu không có session cho ngày hôm nay
            session = new Session(day, month, year, false, 3); // shiftId là 3
            AppDatabase.getInstance(this).sessionDao().insert(session);

            // Tạo liên kết Employee_Session
            Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
            if (employee != null) {
                Employee_Session employeeSession = new Employee_Session(session.getSessionId(), employee.getEmployeeId());
                AppDatabase.getInstance(this).employeeSessionDao().insert(employeeSession);
            }
        }

        // Lưu sessionId vào đối tượng timekeeping
        timekeeping.setSessionId(session.getSessionId());
    }

    private Integer getEmployeeId(int userId) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
        return (employee != null) ? employee.getEmployeeId() : null;
    }

    private Session getSessionForToday(int day, int month, int year) {
        // Lấy danh sách sessionId của nhân viên
        List<Integer> employeeSessionIds = AppDatabase.getInstance(this)
                .employeeSessionDao()
                .getSessionIdsByEmployeeId(getEmployeeId(userId));

        // Lấy danh sách session của ngày hiện tại
        List<Session> sessionsForDate = AppDatabase.getInstance(this)
                .sessionDao()
                .getSessionByDayMonthYear(day, month, year);

        // Kiểm tra session của nhân viên trong ngày
        for (Session session : sessionsForDate) {
            if (employeeSessionIds.contains(session.getSessionId())) {
                return session;
            }
        }

        return null; // Không tìm thấy session
    }

    private void saveTimekeepingState() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentMode", currentMode); // Lưu trạng thái hiện tại (In hoặc Checkout)
        editor.putString("timeIn", timekeeping.getTimeIn()); // Lưu thời gian chấm công vào
        editor.putString("timeOut", timekeeping.getTimeOut()); // Lưu thời gian chấm công ra
        editor.apply();
    }

    private void restoreTimekeepingState() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
        currentMode = sharedPreferences.getString("currentMode", "In");
        String timeIn = sharedPreferences.getString("timeIn", null);
        String timeOut = sharedPreferences.getString("timeOut", null);

        timekeeping = new Timekeeping();
        timekeeping.setTimeIn(timeIn);
        timekeeping.setTimeOut(timeOut);

        updateUI();
    }

    private void updateUI() {
        if (currentMode.equals("In")) {
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.GONE);
        } else {
            btnCheckIn.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }

    private void checkIn() {
        if (currentMode.equals("In")) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            timekeeping.setTimeIn(now);
            currentMode = "Checkout";

            saveTimekeepingState();
            updateUI();
        }
    }

    private void checkOut() {
        if (currentMode.equals("Checkout")) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            timekeeping.setTimeOut(now); // Set thời gian chấm công ra

            // Kiểm tra và thiết lập sessionId nếu chưa có
            if (timekeeping.getSessionId() == 0) {
                Session session = getSessionForToday(LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear());
                if (session != null) {
                    timekeeping.setSessionId(session.getSessionId());
                } else {
                    Toast.makeText(this, "Không tìm thấy session cho ngày hôm nay", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            try {
                // Lưu thời gian chấm công vào cơ sở dữ liệu
                AppDatabase.getInstance(this).timekeepingDao().insert(timekeeping);
                Toast.makeText(this, "Chấm công ra thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            // Reset lại chế độ
            currentMode = "In";

            saveTimekeepingState();
            updateUI();
        }
    }
}