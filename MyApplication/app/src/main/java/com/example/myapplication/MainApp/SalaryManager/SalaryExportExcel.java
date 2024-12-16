package com.example.myapplication.MainApp.SalaryManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainApp.Salary.TaxBracket;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_RewardDiscipline;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Salary;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Timekeeping;
import com.example.myapplication.database.entities.User;

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

public class SalaryExportExcel extends AppCompatActivity {

    private Button btnBack;
    private Button btnExport;
    private Spinner spinnerMonthFrom, spinnerYearFrom, spinnerMonthTo, spinnerYearTo, spinnerType;
    private SearchView searchView;
    private int selectedMonth, selectedYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_manager);

        initUI();
        setupSpinnerType();
        setupMonthAndYearSpinners();

        btnExport.setOnClickListener(v -> exportData());
        btnBack.setOnClickListener(v -> finish());
    }

    private void exportData() {
        try {
            String employeeIdText = searchView.getQuery().toString();

            int startMonth = Integer.parseInt(spinnerMonthFrom.getSelectedItem().toString());
            int startYear = Integer.parseInt(spinnerYearFrom.getSelectedItem().toString());
            int endMonth = Integer.parseInt(spinnerMonthTo.getSelectedItem().toString());
            int endYear = Integer.parseInt(spinnerYearTo.getSelectedItem().toString());

            if (startYear > endYear || (startYear == endYear && startMonth > endMonth)) {
                Toast.makeText(this, "Khoảng thời gian không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spinnerType.getSelectedItemPosition() == 1) { // Filter by Employee ID
                if (!employeeIdText.isEmpty()) {
                    try {
                        int employeeId = Integer.parseInt(employeeIdText);
                        Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);
                        getSalaryDataForEmployee(employee, startMonth, startYear, endMonth, endYear);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Mã nhân viên không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Vui lòng nhập mã nhân viên!", Toast.LENGTH_SHORT).show();
                }
            } else {
                getAll(startMonth, startYear, endMonth, endYear);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi khi export Excel!", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToExcel(List<ExportItemSalary> list) {
        if (list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu để xuất.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String fileName = "Salary_" + dateFormat.format(System.currentTimeMillis()) + ".xls";
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Salary Data");

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Quản lý lương");

            Row dateRangeRow = sheet.createRow(1);
            dateRangeRow.createCell(0).setCellValue(String.format("Tháng %d năm %d", selectedMonth, selectedYear));

            Row headerRow = sheet.createRow(2);
            String[] headers = {"Employee ID", "Full Name", "Month/Year", "Reward/Discipline Money", "Basic Salary", "Allowance", "Coefficient", "Overtime Hours", "Overtime Money", "Tax"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowIndex = 3;
            for (ExportItemSalary exportItem : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(exportItem.getEmployeeId());
                row.createCell(1).setCellValue(exportItem.getFullname());
                row.createCell(2).setCellValue(exportItem.getMonthYear());
                row.createCell(3).setCellValue(exportItem.getGetRewardDisciplineMoney());
                row.createCell(4).setCellValue(exportItem.getBasicSalary());
                row.createCell(5).setCellValue(exportItem.getAllowance());
                row.createCell(6).setCellValue(exportItem.getCoefficient());
                row.createCell(7).setCellValue(exportItem.getOverTime());
                row.createCell(8).setCellValue(exportItem.getOverTimeMoney());
                row.createCell(9).setCellValue(exportItem.getTax());
            }

            workbook.write(fos);
            workbook.close();

            Toast.makeText(this, "Xuất file thành công: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi xuất file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getAll(int startMonth, int startYear, int endMonth, int endYear) {
        try {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Employee> employees = db.employeeDao().getAllEmployees(); // Lấy tất cả nhân viên
            List<ExportItemSalary> allExportItems = new ArrayList<>();

            for (Employee employee : employees) {
                List<ExportItemSalary> employeeExportItems = getSalaryDataForEmployee(employee, startMonth, startYear, endMonth, endYear);
                if (employeeExportItems != null) {
                    allExportItems.addAll(employeeExportItems); // Gộp dữ liệu
                }
            }

            if (!allExportItems.isEmpty()) {
                exportToExcel(allExportItems);
            } else {
                Toast.makeText(this, "Không có dữ liệu để xuất.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Đã xảy ra lỗi khi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<ExportItemSalary> getSalaryDataForEmployee(Employee employee, int startMonth, int startYear, int endMonth, int endYear) {
        List<ExportItemSalary> exportItemList = new ArrayList<>();

        try {
            User user = AppDatabase.getInstance(this).userDao().getUserById(employee.getUserId());
            String createDate = user.getCreateDate();

            int[] adjustedStart = adjustStartMonthAndYear(createDate, startMonth, startYear);
            startMonth = adjustedStart[0];
            startYear = adjustedStart[1];

            for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
                int monthStart = (currentYear == startYear) ? startMonth : 1;
                int monthEnd = (currentYear == endYear) ? endMonth : 12;

                for (int currentMonth = monthStart; currentMonth <= monthEnd; currentMonth++) {
                    ExportItemSalary item = readItem(employee.getEmployeeId(), currentMonth, currentYear);
                    if (item != null) {
                        exportItemList.add(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý nhân viên " + employee.getFullName(), Toast.LENGTH_SHORT).show();
        }

        return exportItemList;
    }

    private int[] adjustStartMonthAndYear(String createDate, int startMonth, int startYear) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar createCal = Calendar.getInstance();
            createCal.setTime(sdf.parse(createDate));

            int createYear = createCal.get(Calendar.YEAR);
            int createMonth = createCal.get(Calendar.MONTH) + 1;

            if (createYear > startYear || (createYear == startYear && createMonth > startMonth)) {
                return new int[]{createMonth, createYear};
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý ngày tạo tài khoản!", Toast.LENGTH_SHORT).show();
        }
        return new int[]{startMonth, startYear};
    }


    private void setupSpinnerType() {
        List<String> data = Arrays.asList("Tất cả nhân viên", "Theo mã nhân viên");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchView.setEnabled(position == 1);
                if (position == 0) {
                    searchView.setQuery("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private ExportItemSalary readItem(int employeeId, int month, int year) {
        ExportItemSalary exportItemSalary = new ExportItemSalary();

        try {
            Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);
            Salary salary = AppDatabase.getInstance(this).salaryDao().getSalaryById(employee.getSalaryId());
            Float overtime = getEmployeeOvertime(employee.getEmployeeId(), month, year);
            Float addMoney = overtime * TaxBracket.ADD_MONEY_PER_HOUR;
            Float baseSalary = (employee.getSalaryId() != null) ? getBaseSalary(employee.getSalaryId()) : 0;
            Float allowanceSalary = (employee.getSalaryId() != null) ? getAllowanceSalary(employee.getSalaryId()) : 0;
            Float rewardDisciplineMoney = (getRewardDisciplineMoney(employee.getEmployeeId(), month, year));
            Float totalSalary = baseSalary + allowanceSalary + rewardDisciplineMoney + addMoney;
            Float tax = (float) calculateTax(totalSalary);
            Float receiveMoney = totalSalary - tax;

            exportItemSalary.setEmployeeId(employee.getEmployeeId());
            exportItemSalary.setFullname(employee.getFullName());
            exportItemSalary.setMonthYear(String.format("%d/%d", month, year));
            exportItemSalary.setTotal(String.format("%.2f", receiveMoney));
            exportItemSalary.setGetRewardDisciplineMoney(String.format("%.2f", rewardDisciplineMoney));
            exportItemSalary.setBasicSalary(String.format("%.2f", salary.getBasicSalary()));
            exportItemSalary.setAllowance(String.format("%.2f", salary.getAllowance()));
            exportItemSalary.setCoefficient(String.valueOf(salary.getCoefficient()));
            exportItemSalary.setOverTime(String.valueOf(overtime));
            exportItemSalary.setOverTimeMoney(String.format("%.2f", addMoney));
            exportItemSalary.setTax(String.format("%.2f", tax));

            return exportItemSalary;
        } catch (Exception e) {
            Toast.makeText(this, "Thêm item thất bại!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

    public static double calculateTax(double tntt) {
        return TaxBracket.getTax(tntt);
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
        String strMonthAndYear = String.format("%02d/%d", month, year);
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

    private void setupMonthAndYearSpinners() {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i));
        }

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.item_spiner, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonthFrom.setAdapter(monthAdapter);
        spinnerMonthTo.setAdapter(monthAdapter);

        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 10; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.item_spiner, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYearFrom.setAdapter(yearAdapter);
        spinnerYearTo.setAdapter(yearAdapter);

        spinnerMonthFrom.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        spinnerYearFrom.setSelection(years.indexOf(String.valueOf(currentYear)));
        spinnerMonthTo.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        spinnerYearTo.setSelection(years.indexOf(String.valueOf(currentYear)));
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnExport = findViewById(R.id.btn_export);
        spinnerMonthFrom = findViewById(R.id.spinner_month_from);
        spinnerYearFrom = findViewById(R.id.spinner_year_from);
        spinnerMonthTo = findViewById(R.id.spinner_month_to);
        spinnerYearTo = findViewById(R.id.spinner_year_to);
        spinnerType = findViewById(R.id.sp_filter);
        searchView = findViewById(R.id.sv_search);
    }
}
