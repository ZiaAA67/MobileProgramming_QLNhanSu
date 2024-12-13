package com.example.myapplication.MainApp.TimekeepingManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TimekeepingExportExcel extends AppCompatActivity {

    private Button btnBack;
    private Button btnExport;
    private TextView tvFromDate, tvToDate;
    private Calendar fromDate, toDate;
    private Spinner spinnerType;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel);

        initUI();
        setupSpinnerType();

        tvFromDate.setOnClickListener(v -> showDatePickerDialog(tvFromDate, fromDate));
        tvToDate.setOnClickListener(v -> showDatePickerDialog(tvToDate, toDate));

        btnBack.setOnClickListener(v -> finish());

        btnExport.setOnClickListener(v -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                fromDate.setTime(dateFormat.parse(tvFromDate.getText().toString()));
                toDate.setTime(dateFormat.parse(tvToDate.getText().toString()));

                int dayFrom = fromDate.get(Calendar.DAY_OF_MONTH);
                int monthFrom = fromDate.get(Calendar.MONTH) + 1;
                int yearFrom = fromDate.get(Calendar.YEAR);

                int dayTo = toDate.get(Calendar.DAY_OF_MONTH);
                int monthTo = toDate.get(Calendar.MONTH) + 1;
                int yearTo = toDate.get(Calendar.YEAR);

                String employeeIdText = searchView.getQuery().toString();
                if (spinnerType.getSelectedItemPosition() == 1) {
                    if (!employeeIdText.isEmpty()) {
                        int employeeId = Integer.parseInt(employeeIdText);
                        getSessionsForEmployeeInDateRange(employeeId, dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo);
                    } else {
                        Toast.makeText(this, "Vui lòng nhập mã nhân viên!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    getAllTimekeeping(dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog(TextView textView, Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    textView.setText(dateFormat.format(calendar.getTime()));
                },
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void exportToExcel(List<ExportItem> list) {
        if (fromDate.getTimeInMillis() > toDate.getTimeInMillis()) {
            Toast.makeText(this, "Ngày bắt đầu không được sau ngày kết thúc", Toast.LENGTH_LONG).show();
            return;
        }

        if (list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu chấm công để export.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String currentDateAndTime = dateFormat.format(System.currentTimeMillis());

        String fileName = "Timekeeping_" + currentDateAndTime + ".xls";
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Timekeeping Data");

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Quản lí chấm công");

            Row dateRangeRow = sheet.createRow(1);
            dateRangeRow.createCell(0).setCellValue("Từ ngày: " + tvFromDate.getText().toString());
            dateRangeRow.createCell(1).setCellValue("Đến ngày: " + tvToDate.getText().toString());

            Row headerRow = sheet.createRow(2);
            headerRow.createCell(0).setCellValue("Employee ID");
            headerRow.createCell(1).setCellValue("Full Name");
            headerRow.createCell(2).setCellValue("Department");
            headerRow.createCell(3).setCellValue("Position");
            headerRow.createCell(4).setCellValue("Time In");
            headerRow.createCell(5).setCellValue("Time Out");
            headerRow.createCell(6).setCellValue("Overtime");
            headerRow.createCell(7).setCellValue("Date");
            headerRow.createCell(8).setCellValue("Shift");

            int rowIndex = 3;
            for (ExportItem exportItem : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(exportItem.getEmployeeID());
                row.createCell(1).setCellValue(exportItem.getFullname());
                row.createCell(2).setCellValue(exportItem.getDepartment());
                row.createCell(3).setCellValue(exportItem.getPosition());
                row.createCell(4).setCellValue(exportItem.getTimeIn());
                row.createCell(5).setCellValue(exportItem.getTimeOut());
                row.createCell(6).setCellValue(exportItem.getOverTime());
                row.createCell(7).setCellValue(exportItem.getDate());
                row.createCell(8).setCellValue(exportItem.getShiftName());
            }

            workbook.write(fos);
            workbook.close();

            Toast.makeText(this, "Xuất file thành công: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi xuất file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getSessionsForEmployeeInDateRange(int employeeId, int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo) {
        try {

            List<Employee_Session> employeeSessions = AppDatabase.getInstance(this)
                    .employeeSessionDao()
                    .getSessionByEmployeeId(employeeId);

            List<Session> allSessions = AppDatabase.getInstance(this)
                    .sessionDao()
                    .getSessionsBetweenDates(dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo);

            List<Session> filteredSessions = new ArrayList<>();
            for (Employee_Session employeeSession : employeeSessions) {
                for (Session session : allSessions) {
                    if (employeeSession.getSessionID() == session.getSessionId()) {
                        filteredSessions.add(session);
                    }
                }
            }

            List<Timekeeping> timekeepings = new ArrayList<>();
            for (Session session : filteredSessions) {
                List<Timekeeping> timekeepings1 = AppDatabase.getInstance(this).timekeepingDao()
                        .getTimekeepingBySessionId(session.getSessionId());

                timekeepings.addAll(timekeepings1);
            }

            List<ExportItem> exportItemList = new ArrayList<>();
            for (Timekeeping timekeeping : timekeepings) {
                ExportItem exportItem = new ExportItem();
                Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);
                exportItem.setEmployeeID(employeeId);
                exportItem.setFullname(employee.getFullName());

                Department department = AppDatabase.getInstance(this).departmentDao().getById(employee.getDepartmentId());
                exportItem.setDepartment(department != null ? department.getDepartmentName() : "Không có!");

                Position position = AppDatabase.getInstance(this).positionDao().getPositionById(employee.getPositionId());
                exportItem.setPosition(position != null ? position.getPositionName() : "Không có!");

                Session session = AppDatabase.getInstance(this).sessionDao().getSessionById(timekeeping.getSessionId());
                exportItem.setDate(session.getDay() + "/" + session.getMonth() + "/" + session.getYear());

                exportItem.setTimeIn(timekeeping.getTimeIn());
                exportItem.setTimeOut(timekeeping.getTimeOut());
                exportItem.setOverTime(String.valueOf(timekeeping.getOvertime()));

                Shift shift = AppDatabase.getInstance(this).shiftDao().getShiftById(session.getShiftId());
                exportItem.setShiftName(shift != null ? shift.getShiftType() : "Không có!");

                exportItemList.add(exportItem);
            }

            if (!exportItemList.isEmpty()) {
                exportToExcel(exportItemList);
            } else {
                Toast.makeText(this, "Không có dữ liệu chấm công để xuất.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi khi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getAllTimekeeping(int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo) {
        Toast.makeText(this, "Hiện tại chưa xử lí chức năng này", Toast.LENGTH_SHORT).show();
    }

    private void setupSpinnerType() {
        List<String> data = new ArrayList<>(Arrays.asList(new String[]{"Tất cả chấm công", "Theo mã nhân viên"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spiner, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    searchView.setEnabled(false);
                    searchView.setQuery("", false);
                } else {
                    searchView.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnExport = findViewById(R.id.btn_export);
        tvFromDate = findViewById(R.id.tv_date_from);
        tvToDate = findViewById(R.id.tv_date_to);
        spinnerType = findViewById(R.id.sp_filter);
        searchView = findViewById(R.id.sv_search);

        fromDate = Calendar.getInstance();
        toDate = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvFromDate.setText(dateFormat.format(fromDate.getTime()));
        tvToDate.setText(dateFormat.format(toDate.getTime()));
    }
}
