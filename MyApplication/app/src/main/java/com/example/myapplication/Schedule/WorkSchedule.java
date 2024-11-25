package com.example.myapplication.Schedule;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkSchedule extends AppCompatActivity {
    private TextView tvMonthYear;
    private ImageView btnPreviousMonth, btnNextMonth;
    private GridView gridViewCalendar;
    private Calendar calendar;
    private ArrayList<LeaveDay> leaveDays; // Danh sách ngày nghỉ phép
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_schedule);


        // Khởi tạo dữ liệu
        leaveDays = new ArrayList<>();
        leaveDays.add(new LeaveDay(25, 11, 2024, "Nghĩa", "short")); // Ví dụ ngày nghỉ
        leaveDays.add(new LeaveDay(27, 11, 2024, "Nhật", "long"));   // Ví dụ nghỉ dài hạn
        leaveDays.add(new LeaveDay(28, 11, 2024, "Nhật", "long"));
        leaveDays.add(new LeaveDay(29, 11, 2024, "Nhật", "long"));
        leaveDays.add(new LeaveDay(30, 11, 2024, "Nhật", "long"));

        tvMonthYear = findViewById(R.id.tvMonthYear);
        btnPreviousMonth = findViewById(R.id.btnPreviousMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        gridViewCalendar = findViewById(R.id.gridViewCalendar);

        calendar = Calendar.getInstance();
        updateCalendar();

        btnPreviousMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(1));
    }

    private void updateCalendar() {
        ArrayList<String> days = new ArrayList<>();

        // Đặt ngày đầu tiên của tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Tính toán ngày đầu tuần (bắt đầu từ Thứ Hai)
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Chủ Nhật là 0
        if (firstDayOfWeek == 0) {
            firstDayOfWeek = 7; // Đổi Chủ Nhật thành Thứ Bảy cuối tuần
        }

        // Tạo các ô trống trước ngày đầu tiên
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add(""); // Ô trống
        }

        // Tạo các ngày trong tháng
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        // Đặt adapter
        CalendarAdapter adapter = new CalendarAdapter(this, days, calendar, leaveDays);
        gridViewCalendar.setAdapter(adapter);
    }


    private void changeMonth(int value) {
        calendar.add(Calendar.MONTH, value);
        updateCalendar();
    }

}
