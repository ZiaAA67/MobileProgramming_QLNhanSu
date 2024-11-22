package com.example.myapplication.MainApp.LeaveRequest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.EmployeeRequest.EmployeeRequestActivity;
import com.example.myapplication.MainApp.HomeFragment;
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

    private EditText edtLeaveReason, edtLeaveFromDate, edtLeaveToDate;
    private TextView tvSendDate;
    private Button btnSubmitLeaveRequest;
    private Button btnBack;

    Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_form);

        try {
            userId = getIntent().getIntExtra("UserID", -1);
            employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Nhân viên không tồn tại!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        initUI();

        tvSendDate.setText(Configuration.STRING_TODAY);
        edtLeaveFromDate.setOnClickListener(view -> showDatePickerDialog(edtLeaveFromDate));
        edtLeaveToDate.setOnClickListener(view -> showDatePickerDialog(edtLeaveToDate));

        btnSubmitLeaveRequest.setOnClickListener(view -> {
            sendLeaveRequest();
        });

        btnBack.setOnClickListener(view ->{finish();});
    }

    private void sendLeaveRequest() {
        String reason = edtLeaveReason.getText().toString();
        String fromDate = edtLeaveFromDate.getText().toString();
        String toDate = edtLeaveToDate.getText().toString();
        String sendDate = Configuration.STRING_TODAY;

        int employeeid = employee.getEmployeeId();
        if (!checkValidData(reason, fromDate, toDate, edtLeaveReason, edtLeaveFromDate, edtLeaveToDate)) {
            return;
        }

        try {
            LeaveRequest leaveRequest = new LeaveRequest(reason, sendDate, fromDate, toDate, 0, employeeid);
            AppDatabase.getInstance(this).leaveRequestDao().insert(leaveRequest);
            Toast.makeText(this, "Yêu cầu nghỉ đã được gửi!", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidData(String reason, String fromDate, String toDate,
                                   EditText editReason, EditText editFromDate, EditText editToDate) {

        if (TextUtils.isEmpty(reason)) {
            editReason.setError("Vui lòng nhập lý do nghỉ!");
            editReason.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(fromDate)) {
            editFromDate.setError("Vui lòng nhập ngày nghỉ từ!");
            editFromDate.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(toDate)) {
            editToDate.setError("Vui lòng nhập ngày nghỉ đến!");
            editToDate.requestFocus();
            return false;
        }

        if (!isValidDateRange(fromDate, toDate)) {
            editToDate.setError("Ngày nghỉ từ phải nhỏ hơn hoặc bằng ngày nghỉ đến!");
            editToDate.requestFocus();
            return false;
        }

        if (!isValidDateRange(Configuration.STRING_TODAY, fromDate)) {
            editFromDate.setError("Ngày nghỉ từ phải lớn hơn hoặc bằng ngày hiện tại!");
            editFromDate.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidDateRange(String fromDate, String toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom = sdf.parse(fromDate);
            Date dateTo = sdf.parse(toDate);

            // Kiểm tra ngày nghỉ từ và ngày nghỉ đến
            return   dateFrom != null && dateTo != null && !dateTo.before(dateFrom);
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

        calendar.set(year, month, day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long minDate = calendar.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
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
}
