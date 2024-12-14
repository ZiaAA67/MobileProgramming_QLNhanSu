package com.example.myapplication.MainApp.Employee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.MyItemTouchHelper;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeAddListItem extends AppCompatActivity {
    private List<Employee> listEmployee;
    private RecyclerView rcvEmployee;
    private EmployeeAddItemAdapter employeeAdapter;
    private Button btnBack;
    private Button btnUpload;
    private Button btnAddList;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_add_list_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        // Setup adapter
        listEmployee = new ArrayList<>();
        employeeAdapter = new EmployeeAddItemAdapter(listEmployee);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);
        rcvEmployee.setAdapter(employeeAdapter);

        // Tạo hàm callback khi vuốt
        ItemTouchHelper itemTouchHelper = setupItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(rcvEmployee);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        readExcelFile(fileUri);
                    }

                    loadData(listEmployee);
                });

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }


    @NonNull
    private ItemTouchHelper setupItemTouchHelper() {
        MyItemTouchHelper myItemTouchHelper = new MyItemTouchHelper(position -> {
            // Lấy vị trí item xoá
            Employee employee = listEmployee.get(position);

            // Thực hiện xoá item, Thông báo vị trí xoá cho adapter -> load lại dữ liệu
            employee.setActive(false);
            employeeAdapter.notifyItemRemoved(position);


            // Hiển thị Snackbar với nút Undo
            Snackbar.make(rcvEmployee, "Đã xóa " + employee.getFullName(), Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác", v -> {
                        // Khôi phục item nếu người dùng chọn Undo
                        listEmployee.add(position, employee);
                        employeeAdapter.notifyItemInserted(position);
                        rcvEmployee.scrollToPosition(position);
                    })
                    .addCallback(new Snackbar.Callback() {
                        // Nếu Snackbar bị ẩn mà không chọn Undo
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
//                                // Thực hiện xóa chính thức
//                                AppDatabase.getInstance(EmployeeAddListItem.this).employeeDao().update(employee);
//                                listEmployee.remove(position);
                            }
                        }
                    })
                    .show();
        });
        ItemTouchHelper.SimpleCallback simpleCallback = myItemTouchHelper.handleItemTouchHelper();
        return new ItemTouchHelper(simpleCallback);
    }

    private void readExcelFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                String name = row.getCell(0).getStringCellValue();
                int gender = (int) row.getCell(1).getNumericCellValue();
                String birth = row.getCell(2).getStringCellValue();
                String identity = row.getCell(3).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();
                String phone = row.getCell(5).getStringCellValue();
                String email = row.getCell(6).getStringCellValue();

                Employee employee = new Employee(name, gender, birth, identity, address, phone, email, true, true, null, null, null, null, null, null, null);
                listEmployee.add(employee);
            }
            Toast.makeText(this, "Đọc file thành công!", Toast.LENGTH_SHORT).show();
            workbook.close();

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đọc file!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                return true;
            }
        }
        return false;
    }

    private void loadData(List<Employee> list) {
//        listEmployee = AppDatabase.getInstance(this).employeeDao().getActiveEmployees();
        employeeAdapter.setData(list);
    }

    private void bindingView() {
        rcvEmployee = findViewById(R.id.rcv_employee);
        btnBack = findViewById(R.id.btn_back);
        btnUpload = findViewById(R.id.btn_upload);
        btnAddList = findViewById(R.id.btn_add_list);
    }
}