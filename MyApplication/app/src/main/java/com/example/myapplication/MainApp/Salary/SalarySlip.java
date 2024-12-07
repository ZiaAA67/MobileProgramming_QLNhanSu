package com.example.myapplication.MainApp.Salary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SalarySlip extends AppCompatActivity {

    private int userId;

    private ListView lvSalarySlips;
    private List<SalarySlipItem> salarySlipItems;
    private SalarySlipAdapter salarySlipAdapter;
    private int YEAR;
    private Button btnNextYear;
    private Button btnPreviousYear;
    private Button btnBack;
    private TextView tvCurrentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_slip);
        initUI();

        userId = getIntent().getIntExtra("UserID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        YEAR = Calendar.getInstance().get(Calendar.YEAR);
        updateYearDisplay();

        salarySlipItems = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            salarySlipItems.add(new SalarySlipItem(i, YEAR));
        }

        salarySlipAdapter = new SalarySlipAdapter(this, salarySlipItems, userId);
        lvSalarySlips.setAdapter(salarySlipAdapter);
        lvSalarySlips.setOnItemClickListener((parent, view, position, id) -> {
            SalarySlipItem item = salarySlipItems.get(position);
            Intent intent = new Intent(SalarySlip.this, SalarySlipInformation.class);
            intent.putExtra("UserID", userId);
            intent.putExtra("Month", item.getMonth());
            intent.putExtra("Year", item.getYear());
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());

        btnPreviousYear.setOnClickListener(view -> {
            YEAR--;
            updateYearDisplay();
            refreshSalarySlips();
        });

        btnNextYear.setOnClickListener(view -> {
            YEAR++;
            updateYearDisplay();
            refreshSalarySlips();
        });
    }

    private void initUI() {
        lvSalarySlips = findViewById(R.id.lv_salary_slips);
        btnNextYear = findViewById(R.id.btn_next_year);
        btnPreviousYear = findViewById(R.id.btn_previous_year);
        btnBack = findViewById(R.id.btn_back);
        tvCurrentYear = findViewById(R.id.tv_current_year);
    }

    private void updateYearDisplay() {
        tvCurrentYear.setText(String.valueOf(YEAR));
    }

    private void refreshSalarySlips() {
        salarySlipItems.clear();
        for (int i = 1; i <= 12; i++) {
            salarySlipItems.add(new SalarySlipItem(i, YEAR));
        }
        salarySlipAdapter.notifyDataSetChanged();
    }
}
