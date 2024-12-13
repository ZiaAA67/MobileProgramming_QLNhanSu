package com.example.myapplication.MainApp.TimekeepingManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimekeepingExportExcel extends AppCompatActivity {

    private Button btnBack;
    private Button btnExport;
    private TextView tvFromDate, tvToDate;
    private Calendar fromDate, toDate;

    List<ExportItem> exportItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel);

        initUI();

        tvFromDate.setOnClickListener(v -> showDatePickerDialog(tvFromDate, fromDate));
        tvToDate.setOnClickListener(v -> showDatePickerDialog(tvToDate, toDate));

        btnBack.setOnClickListener(v -> finish());

        btnExport.setOnClickListener(v -> exportToExcel());

        // Test the getAllTimekeepings function with sample date range
        int dayFrom = 1;  // Starting day
        int monthFrom = 1;  // Starting month
        int yearFrom = 2023;  // Starting year
        int dayTo = 13;  // Ending day
        int monthTo = 12;  // Ending month
        int yearTo = 2024;  // Ending year

        getAllTimekeepings(dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo);
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

    private void exportToExcel() {
        if (fromDate.getTimeInMillis() > toDate.getTimeInMillis()) {
            Toast.makeText(this, "Ngày bắt đầu không được sau ngày kết thúc", Toast.LENGTH_LONG).show();
            return;
        }

        String fileName = "timekeeping_data.xls";
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Lấy ngày hiện tại
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(calendar.getTime());

            // Tiêu đề file
            String title = "Kiểm tra lịch sử chấm công\n";
            String exportDate = "Ngày xuất file: " + currentDate + "\n";

            // Thông tin khoảng ngày kiểm tra
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateStr = format.format(fromDate.getTime());
            String toDateStr = format.format(toDate.getTime());
            String dateRange = "Đang kiểm tra lịch sử từ ngày: " + fromDateStr + " đến ngày: " + toDateStr + "\n\n";

            // Ghi tiêu đề vào file
            fos.write(title.getBytes());
            fos.write(exportDate.getBytes());
            fos.write(dateRange.getBytes());

            // Ghi dữ liệu vào file
            String rowData = fromDateStr + "\t" + toDateStr + "\n";
            fos.write(rowData.getBytes());

            // Hiển thị thông báo thành công
            Toast.makeText(this, "Xuất file thành công: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // Hiển thị thông báo lỗi
            Toast.makeText(this, "Lỗi khi xuất file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getAllTimekeepings(int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo) {
        try {
            List<Session> sessions = AppDatabase.getInstance(this).sessionDao()
                    .getSessionsBetweenDates(dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo);

            if (sessions.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy dữ liệu trong khoảng thời gian đã chọn.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Session session : sessions) {
                List<Timekeeping> timekeepings = AppDatabase.getInstance(this)
                        .timekeepingDao()
                        .getTimekeepingBySessionId(session.getSessionId());

                List<Integer> employeeSession = AppDatabase.getInstance(this).employeeSessionDao().getEmployeeIdsBySessionId(session.getSessionId());
                for (Timekeeping tk : timekeepings) {
                    // Tạo ExportItem cho mỗi Timekeeping
                    ExportItem exportItem = new ExportItem();

                    // Lấy Employee theo EmployeeID từ danh sách employeeSession
                    for (Integer employeeId : employeeSession) {
                        Employee employee = AppDatabase.getInstance(this)
                                .employeeDao()
                                .getById(employeeId);  // Pass the employeeId here

                        if (employee == null) continue;

                        exportItem.setEmployeeID(employee.getEmployeeId());
                        exportItem.setFullname(employee.getFullName());

                        Department department = AppDatabase.getInstance(this)
                                .departmentDao()
                                .getById(employee.getDepartmentId());

                        Position position = AppDatabase.getInstance(this)
                                .positionDao()
                                .getPositionById(employee.getPositionId());

                        exportItem.setDepartment(department != null ? department.getDepartmentName() : "N/A");
                        exportItem.setPosition(position != null ? position.getPositionName() : "N/A");

                        exportItem.setDate(session.getDay() + "/" + session.getMonth() + "/" + session.getYear());
                        exportItem.setTimeIn(tk.getTimeIn());
                        exportItem.setTimeOut(tk.getTimeOut());

                        // Lấy giá trị overtime từ Timekeeping
                        exportItem.setOverTime(String.valueOf(tk.getOvertime()));

                        Shift shift = AppDatabase.getInstance(this)
                                .shiftDao()
                                .getShiftById(session.getShiftId());

                        exportItem.setShiftName(shift != null ? shift.getShiftType() : "N/A");

                        // Thêm vào danh sách export
                        exportItemList.add(exportItem);
                    }
                }
            }

            if (exportItemList.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu chấm công để xuất.", Toast.LENGTH_SHORT).show();
            } else {
                // Tiếp tục xử lý exportItemList (xuất ra file Excel hoặc hiển thị)
                Log.d("ExportItems", "Số lượng dữ liệu: " + exportItemList.size());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi khi lấy danh sách!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnExport = findViewById(R.id.btn_export);
        tvFromDate = findViewById(R.id.tv_date_from);
        tvToDate = findViewById(R.id.tv_date_to);

        fromDate = Calendar.getInstance();
        toDate = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvFromDate.setText(dateFormat.format(fromDate.getTime()));
        tvToDate.setText(dateFormat.format(toDate.getTime()));
    }
}
