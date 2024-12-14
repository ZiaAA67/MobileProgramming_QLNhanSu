package com.example.myapplication.Demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.entities.Position;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DemoReadExcel extends AppCompatActivity {
    private Button btnUpload;
    private Button btnLoad;
    private ListView listView;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private List<Position> listPosition = new ArrayList<>();

    private boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_read_excel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button mainButton = findViewById(R.id.mainButton);
        Button arcButton1 = findViewById(R.id.arcButton1);
        Button arcButton2 = findViewById(R.id.arcButton2);

        mainButton.setOnClickListener(v -> {
            if (isMenuOpen) {
                closeMenu(arcButton1, arcButton2);
            } else {
                openMenu(arcButton1, arcButton2);
            }
        });

        arcButton1.setOnClickListener(v -> {
            closeMenu(arcButton1, arcButton2);
        });

        arcButton2.setOnClickListener(v -> {
            closeMenu(arcButton1, arcButton2);
        });

        btnUpload = findViewById(R.id.upload_btn);
        btnLoad = findViewById(R.id.load_data);
        listView = findViewById(R.id.listview);

        // Register file picker result
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        processExcelFile(fileUri);
                    }
                });

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        btnLoad.setOnClickListener(v -> loadData());
    }

    private void loadData() {
        List<String> data = new ArrayList<>();
        listPosition.forEach(p -> data.add(p.getPositionName()));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    private void processExcelFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String name = getCellValueAsString(row.getCell(0));
                    String desc = getCellValueAsString(row.getCell(1));
                    listPosition.add(new Position(name, desc));
                }
            }
            Toast.makeText(this, "Thanh cong!", Toast.LENGTH_SHORT).show();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Convert số thành chuỗi
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private void openMenu(Button arcButton1, Button arcButton2) {
        arcButton1.setVisibility(View.VISIBLE);
        arcButton2.setVisibility(View.VISIBLE);

        arcButton1.animate().translationX(-220).translationY(-260).rotation(60).setDuration(300).start();
        arcButton2.animate().translationX(-390).translationY(-50).rotation(20).setDuration(300).start();

        isMenuOpen = true;
    }

    private void closeMenu(Button arcButton1, Button arcButton2) {
        arcButton1.animate().translationX(0).translationY(0).setDuration(300).rotation(0).withEndAction(() -> {
            arcButton1.setVisibility(View.INVISIBLE);
        }).start();

        arcButton2.animate().translationX(0).translationY(0).setDuration(300).rotation(0).withEndAction(() -> {
            arcButton2.setVisibility(View.INVISIBLE);
        }).start();

        isMenuOpen = false;
    }
}