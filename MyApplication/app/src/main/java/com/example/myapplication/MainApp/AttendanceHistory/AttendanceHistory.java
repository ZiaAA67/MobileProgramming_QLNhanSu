package com.example.myapplication.MainApp.AttendanceHistory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Timekeeping;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

public class AttendanceHistory extends AppCompatActivity {

    private int userId;
    private int employeeId;

    private String currentMode = "week"; // Default
    private int currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    private int currentMonth = LocalDate.now().getMonthValue();
    private int currentYear = LocalDate.now().getYear();

    private RecyclerView recyclerViewWeek;
    private LinearLayout linearLayoutMonth;
    private LinearLayout linearLayoutYear;
    private GridView gridViewMonth;
    private TextView tvCurrentTime;
    private Button btnNext, btnPrevious, btnBack, btnMonth, btnYear, btnWeek;

    private AttendanceHistoryWeekAdapter attendanceHistoryWeekAdapter;
    private List<Timekeeping> mListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        userId = getIntent().getIntExtra("UserID", -1);
        employeeId = getEmployeeId(userId);

        initUI();
        updateWeekView();

        btnPrevious.setOnClickListener(view -> handlePrevious());
        btnNext.setOnClickListener(view -> handleNext());
        btnBack.setOnClickListener(view -> finish());

        btnWeek.setOnClickListener(view -> switchToWeekMode());
        btnMonth.setOnClickListener(view -> switchToMonthMode());
        btnYear.setOnClickListener(view -> switchToYearMode());
    }

    private void updateWeekView() {
        attendanceHistoryWeekAdapter = new AttendanceHistoryWeekAdapter();
        recyclerViewWeek.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWeek.setAdapter(attendanceHistoryWeekAdapter);

        mListItems = getEmployeeTimekeeping(employeeId, currentWeek);
        attendanceHistoryWeekAdapter.setData(mListItems, this);
        tvCurrentTime.setText("Tuần " + currentWeek);
    }

    private void updateMonthView() {
        ArrayList<String> days = generateDaysForMonth(currentMonth, currentYear);
        List<Integer> markedDays = getDaysWithTimekeeping(employeeId, currentMonth, currentYear);

        AttendanceHistoryMonthAdapter adapter = new AttendanceHistoryMonthAdapter(this, days, currentMonth, currentYear, markedDays);
        gridViewMonth.setAdapter(adapter);

        tvCurrentTime.setText("Tháng " + currentMonth);
    }

    private void updateYearView() {
        int totalDays = getTotalWorkDaysForYear(userId, currentYear);

        TextView textViewTotalDays = findViewById(R.id.text_view_total_days);
        TextView textViewYear = findViewById(R.id.text_view_display_year);

        tvCurrentTime.setText("Năm " + currentYear);
        textViewYear.setText("Năm: " + currentYear);
        textViewTotalDays.setText("Tổng số ngày công: " + totalDays);
    }

    private List<Timekeeping> getEmployeeTimekeeping(int employeeId, int weekNumber) {
        // Get all Session of this employee
        List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                .employeeSessionDao().getSessionByEmployeeId(employeeId);

        List<Session> sessionsForWeek = new ArrayList<>();
        for (Employee_Session employeeSession : employeeSessions) {
            Session session = AppDatabase.getInstance(this).sessionDao()
                    .getSessionById(employeeSession.getSessionID());
            int sessionWeek = getWeekOfYear(session.getYear(), session.getMonth(), session.getDay());
            if (sessionWeek == weekNumber) {
                sessionsForWeek.add(session);
            }
        }

        List<Timekeeping> timekeepingList = new ArrayList<>();
        for (Session session : sessionsForWeek) {
            timekeepingList.addAll(AppDatabase.getInstance(this)
                    .timekeepingDao().getTimekeepingBySessionId(session.getSessionId()));
        }
        return timekeepingList;
    }

    private List<Integer> getDaysWithTimekeeping(int employeeId, int month, int year) {

        List<Integer> markedDays = new ArrayList<>();

        List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                .employeeSessionDao().getSessionByEmployeeId(employeeId);

        for (Employee_Session employeeSession : employeeSessions) {
            Session session = AppDatabase.getInstance(this).sessionDao()
                    .getSessionById(employeeSession.getSessionID());

            if (session.getMonth() == month && session.getYear() == year) {
                markedDays.add(session.getDay());
            }
        }

        return markedDays;
    }

    private int getTotalWorkDaysForYear(int employeeId, int year) {
        int totalDays = 0;

        // Lấy danh sách Employee_Session của nhân viên này
        List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                .employeeSessionDao().getSessionByEmployeeId(employeeId);

        // Lọc các phiên thuộc năm cần kiểm tra
        for (Employee_Session employeeSession : employeeSessions) {
            Session session = AppDatabase.getInstance(this).sessionDao()
                    .getSessionById(employeeSession.getSessionID());

            if (session.getYear() == year) {
                totalDays += 1;
            }
        }

        return totalDays;
    }

    private void switchToWeekMode() {
        currentMode = "week";
        recyclerViewWeek.setVisibility(View.VISIBLE);
        linearLayoutMonth.setVisibility(View.GONE);
        linearLayoutYear.setVisibility(View.GONE);
        currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        updateWeekView();
    }

    private void switchToMonthMode() {
        currentMode = "month";
        recyclerViewWeek.setVisibility(View.GONE);
        linearLayoutMonth.setVisibility(View.VISIBLE);
        linearLayoutYear.setVisibility(View.GONE);
        currentMonth = LocalDate.now().getMonthValue();
        updateMonthView();
    }

    private void switchToYearMode() {
        currentMode = "year";
        recyclerViewWeek.setVisibility(View.GONE);
        linearLayoutMonth.setVisibility(View.GONE);
        linearLayoutYear.setVisibility(View.VISIBLE);
        currentYear = LocalDate.now().getYear();
        updateYearView();
    }

    private void handleNext() {
        if (currentMode.equals("week")) {
            currentWeek++;
            updateWeekView();
        } else if (currentMode.equals("month")) {
            if (currentMonth < 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            updateMonthView();
        } else if (currentMode.equals("year")) {
            currentYear++;
            updateYearView();
        }
    }

    private void handlePrevious() {
        if (currentMode.equals("week")) {
            currentWeek--;
            updateWeekView();
        } else if (currentMode.equals("month")) {
            if (currentMonth > 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            updateMonthView();
        } else if (currentMode.equals("year")) {
            currentYear--;
            updateYearView();
        }
    }

    // Tính tuần của năm từ ngày, tháng, năm
    private int getWeekOfYear(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private ArrayList<String> generateDaysForMonth(int month, int year) {
        ArrayList<String> days = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);

        int daysInMonth = date.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        return days;
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

        return employee.getEmployeeId();
    }

    private void initUI() {
        recyclerViewWeek = findViewById(R.id.recycler_view_week);
        gridViewMonth = findViewById(R.id.grid_view_calendar);
        linearLayoutMonth = findViewById(R.id.linear_layout_month);
        linearLayoutYear = findViewById(R.id.linear_layout_year);
        tvCurrentTime = findViewById(R.id.tv_time);
        btnNext = findViewById(R.id.btn_next_week);
        btnPrevious = findViewById(R.id.btn_previous_week);
        btnBack = findViewById(R.id.btn_back);
        btnMonth = findViewById(R.id.btn_month);
        btnYear = findViewById(R.id.btn_year);
        btnWeek = findViewById(R.id.btn_week);
        tvCurrentTime.setText("Tuần " + currentWeek);
    }
}