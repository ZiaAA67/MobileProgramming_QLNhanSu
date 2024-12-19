package com.example.myapplication.MainApp.LeaveRequest;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainApp.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.LeaveRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LeaveRequestForm extends AppCompatActivity {
    private int userId;
    private Employee employee;

    private EditText edtLeaveReason, edtLeaveFromDate, edtLeaveToDate;
    private TextView tvSendDate;
    private Button btnSubmitLeaveRequest, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_form);

        initUI();
        loadEmployeeData();

        tvSendDate.setText(Configuration.STRING_TODAY);

        edtLeaveFromDate.setOnClickListener(view -> showDatePickerDialog(edtLeaveFromDate));
        edtLeaveToDate.setOnClickListener(view -> showDatePickerDialog(edtLeaveToDate));

        btnSubmitLeaveRequest.setOnClickListener(view -> sendLeaveRequest());
        btnBack.setOnClickListener(view -> finish());
    }

    private void loadEmployeeData() {
        userId = getIntent().getIntExtra("UserID", -1);
        try {
            employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
            if (employee == null) {
                throw new RuntimeException("Nhân viên không tồn tại!");
            }
        } catch (Exception e) {
            showToastAndExit("Lỗi: " + e.getMessage());
        }
    }

    private void sendLeaveRequest() {
        String reason = edtLeaveReason.getText().toString();
        String fromDate = edtLeaveFromDate.getText().toString();
        String toDate = edtLeaveToDate.getText().toString();

        if (!validateInput(reason, fromDate, toDate)) return;

        try {
            LeaveRequest leaveRequest = new LeaveRequest(
                    reason, Configuration.STRING_TODAY, fromDate, toDate, 0, employee.getEmployeeId()
            );
            AppDatabase.getInstance(this).leaveRequestDao().insert(leaveRequest);
            showToastAndExit("Yêu cầu nghỉ đã được gửi!");
        } catch (Exception e) {
            showToast("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }

    private boolean validateInput(String reason, String fromDate, String toDate) {
        if (isEmpty(edtLeaveReason, reason, "Vui lòng nhập lý do nghỉ!")) return false;
        if (isEmpty(edtLeaveFromDate, fromDate, "Vui lòng nhập ngày nghỉ từ!")) return false;
        if (isEmpty(edtLeaveToDate, toDate, "Vui lòng nhập ngày nghỉ đến!")) return false;

        if (!isValidDateRange(fromDate, toDate)) {
            edtLeaveToDate.setError("Ngày nghỉ từ phải nhỏ hơn hoặc bằng ngày nghỉ đến!");
            edtLeaveToDate.requestFocus();
            return false;
        }

        if (!isValidDateRange(Configuration.STRING_TODAY, fromDate)) {
            edtLeaveFromDate.setError("Ngày nghỉ từ phải lớn hơn hoặc bằng ngày hiện tại!");
            edtLeaveFromDate.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText, String value, String errorMessage) {
        if (TextUtils.isEmpty(value)) {
            editText.setError(errorMessage);
            editText.requestFocus();
            return true;
        }
        return false;
    }

    private boolean isValidDateRange(String fromDate, String toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom = sdf.parse(fromDate);
            Date dateTo = sdf.parse(toDate);
            return dateFrom != null && dateTo != null && !dateTo.before(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long minDate = calendar.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            editText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }

    private void initUI() {
        edtLeaveReason = findViewById(R.id.edt_leave_reason);
        tvSendDate = findViewById(R.id.tv_send_date);
        edtLeaveFromDate = findViewById(R.id.edt_leave_from_date);
        edtLeaveToDate = findViewById(R.id.edt_leave_to_date);
        btnSubmitLeaveRequest = findViewById(R.id.btn_submit_leave_request);
        btnBack = findViewById(R.id.btn_back);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToastAndExit(String message) {
        showToast(message);
        finish();
    }
}
