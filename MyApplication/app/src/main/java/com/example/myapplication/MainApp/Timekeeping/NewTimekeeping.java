package com.example.myapplication.MainApp.Timekeeping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;
import com.example.myapplication.database.entities.Workplace;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewTimekeeping extends AppCompatActivity {

    private int userId;
    private int employeeId;

    private Button btnBack;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private String currentMode = "In";
    private TextView tvWorkplace;

    private Timekeeping timekeeping;
    private static Integer DEFAULT_SHIFT = 1;// Ca hành chính

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
        employeeId = getEmployeeId(userId);

        showWorkplace();

        timekeeping = new Timekeeping();

        // Khôi phục trạng thái Timekeeping từ SharedPreferences
        restoreTimekeepingState();

        // Xử lý sự kiện chấm công
        btnCheckIn.setOnClickListener(v -> checkIn());
        btnCheckOut.setOnClickListener(v -> checkOut());
        btnBack.setOnClickListener(v -> finish());
    }

    private void createSessionIfNeeded() {
        try {
            // Kiểm tra nếu đã có session cho ngày hôm nay
            Session existingSession = getSessionForToday(day, month, year);
            int sessionId;

            if (existingSession == null) {
                // Tạo session mới nếu không có
                Session session = new Session(day, month, year, false, DEFAULT_SHIFT); // shiftId = 3 (ca hành chính)
                long insertedId = AppDatabase.getInstance(this).sessionDao().insert(session);

                if (insertedId == -1) {
                    Toast.makeText(this, "Không thể tạo session mới!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Cập nhật sessionId từ kết quả chèn
                sessionId = (int) insertedId;
            } else {
                // Nếu đã có session thì sử dụng sessionId của session hiện tại
                sessionId = existingSession.getSessionId();
            }

            Log.d("DEBUG", "employeeId: " + employeeId + ", sessionId: " + sessionId);

            // Kiểm tra nếu Employee_Session đã tồn tại
            List<Integer> existingEmployeeSessions = AppDatabase.getInstance(this)
                    .employeeSessionDao()
                    .getSessionIdsByEmployeeId(employeeId);

            if (existingEmployeeSessions.contains(sessionId)) {
                Toast.makeText(this, "Phiên làm viêc hôm nay đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo Employee_Session liên kết n-n nhân viên và session
            Employee_Session employeeSession = new Employee_Session(employeeId, sessionId);
            AppDatabase.getInstance(this).employeeSessionDao().insert(employeeSession);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tạo mới: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
        createSessionIfNeeded();
        if (currentMode.equals("In")) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            timekeeping.setTimeIn(now);
            makeToast("Chấm công vào thành công lúc ");
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

                    // Xóa data nếu ra không được
                    SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    return;
                }
            }

            try {
                // Lưu thời gian chấm công vào cơ sở dữ liệu
                timekeeping.setIsAbsent(0);
                timekeeping.setOvertime(calculateOvertime());
                AppDatabase.getInstance(this).timekeepingDao().insert(timekeeping);
                makeToast("Chấm công ra thành công lúc ");
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

    private int calculateOvertime() {
        try {
            String timeOut = timekeeping.getTimeOut();

            if (timeOut == null) {
                Toast.makeText(this, "Chưa có dữ liệu thời gian chấm công ra!", Toast.LENGTH_SHORT).show();
                return 0;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime timeOutParsed = LocalTime.parse(timeOut, formatter);

            Shift shift = AppDatabase.getInstance(this).shiftDao().getShiftById(DEFAULT_SHIFT);
            LocalTime endOfShift = LocalTime.parse(shift.getTimeEnd());

            // Ra sớm
            if (timeOutParsed.isBefore(endOfShift)) {
                return 0;
            }

            // Ra trễ
            long overtime = Duration.between(endOfShift, timeOutParsed).toMinutes();

            return (int) overtime;
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tính overtime: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return 0;
        }
    }

    private void makeToast(String toast) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Toast.makeText(this, toast + now, Toast.LENGTH_SHORT).show();
    }

    private void showWorkplace() {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);
        if (employee.getWorkplaceId() != null) {
            Workplace workplace = AppDatabase.getInstance(this).workplaceDao().getWorkplaceById(employee.getWorkplaceId());
            tvWorkplace.setText(workplace.getWorkplaceName() + " - " + workplace.getAddress());
        } else
            tvWorkplace.setText("Không có");
    }

    private int getEmployeeId(int userId) {
        Employee employee = null;
        try {
            employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);

            if (employee == null) {
                Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
                finish();
                return -1;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
            return -1;
        }

        if (employee.isApprove() == false) {
            Toast.makeText(this, "Nhân viên chưa được châp nhận!", Toast.LENGTH_SHORT).show();
            finish();
        }

        return employee.getEmployeeId();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnCheckIn = findViewById(R.id.btn_checkin);
        btnCheckOut = findViewById(R.id.btn_checkout);
        tvWorkplace = findViewById(R.id.tv_workplace);
    }
}