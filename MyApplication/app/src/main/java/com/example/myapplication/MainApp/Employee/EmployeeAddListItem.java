package com.example.myapplication.MainApp.Employee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.entities.Employee;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAddListItem extends AppCompatActivity {
    private List<Employee> listEmployee;
    private RecyclerView rcvEmployee;
    private EmployeeAddItemAdapter employeeAdapter;
    private Button btnBack;
    private Button btnConfirm;
    private Button btnUpload;
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

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultKey", "success");
            setResult(RESULT_OK, resultIntent);

            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultKey", "success");
            setResult(RESULT_OK, resultIntent);

            finish();
        });
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


                String imgDefault = "https://res.cloudinary.com/dbmwgavqz/image/upload/v1732299242/Sample_User_Icon_n52rlr.png";
                Employee employee = new Employee(name, gender, birth, identity, address, phone, email, true, true, imgDefault, null, null, null, null, null, null);
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
        employeeAdapter.setData(list);
    }

    private void bindingView() {
        rcvEmployee = findViewById(R.id.rcv_employee);
        btnBack = findViewById(R.id.btn_back);
        btnUpload = findViewById(R.id.btn_upload);
        btnConfirm = findViewById(R.id.btn_confirm);
    }
}