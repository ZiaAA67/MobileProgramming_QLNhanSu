package com.example.myapplication.MainApp.AttendanceHistory;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

    private RecyclerView rcvItem;
    private Button btnBack;
    private Button btnNextWeek;
    private Button btnPreviousWeek;
    private TextView tvCurrentWeek;
    private int currentWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

    private AttendanceHistoryAdapter attendanceHistoryAdapter;
    private List<Timekeeping> mListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        userId = getIntent().getIntExtra("UserID", -1);

        initUI();
        setupRecyclerView();

        btnPreviousWeek.setOnClickListener(view -> {
            currentWeek--;
            tvCurrentWeek.setText("Tuần " + currentWeek);
            updateAttendanceData();
        });

        btnNextWeek.setOnClickListener(view -> {
            currentWeek++;
            tvCurrentWeek.setText("Tuần " + currentWeek);
            updateAttendanceData();
        });

        btnBack.setOnClickListener(view -> finish());
    }

    private void setupRecyclerView() {
        attendanceHistoryAdapter = new AttendanceHistoryAdapter();

        updateAttendanceData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvItem.setLayoutManager(linearLayoutManager);
        rcvItem.setAdapter(attendanceHistoryAdapter);
    }

    private void updateAttendanceData() {
        mListItems = getEmployeeSession(userId, currentWeek);
        attendanceHistoryAdapter.setData(mListItems, this);
    }

    private List<Timekeeping> getEmployeeSession(int userId, int weekNumber) {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);

        List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                .employeeSessionDao().getSessionByEmployeeId(employee.getEmployeeId());

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
            List<Timekeeping> timekeepings = AppDatabase.getInstance(this).timekeepingDao()
                    .getTimekeepingBySessionId(session.getSessionId());
            timekeepingList.addAll(timekeepings);
        }
        return timekeepingList;
    }

    private int getWeekOfYear(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private void initUI() {
        rcvItem = findViewById(R.id.rcv_item);
        tvCurrentWeek = findViewById(R.id.tv_week);
        btnNextWeek = findViewById(R.id.btn_next_week);
        btnPreviousWeek = findViewById(R.id.btn_previous_week);
        btnBack = findViewById(R.id.btn_back);

        tvCurrentWeek.setText("Tuần " + currentWeek);
    }
}
