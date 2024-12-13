package com.example.myapplication.Stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Workplace;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Stats extends AppCompatActivity {
    private BarChart chart;
    private Spinner spnOption;
    private ArrayList<BarEntry> entries;
    private ArrayList<Integer> colors;
    private BarDataSet dataSet;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setupSpinnerOption(new ArrayList<>(Arrays.asList(new String[] {"Giới tính", "Phòng ban", "Cơ sở làm việc" ,"Vị trí làm việc"})));

        entries = new ArrayList<>();
        colors = new ArrayList<>();
        setupChart(chart);

        spnOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    setDataGender();
                    chart.invalidate();
                }

                if(i == 1) {
                    setDataDepartment();
                    chart.invalidate();
                }

                if(i == 2) {
                    setDataWorkplace();
                    chart.invalidate();
                }

                if(i == 3) {
                    setDataPosition();
                    chart.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void bindingView() {
        chart = findViewById(R.id.barChart);
        spnOption = findViewById(R.id.spinner_option);
        btnBack = findViewById(R.id.btn_back);
    }

    private void randomColors() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);

        colors.clear();
        for (int i = 0; i < entries.size(); i++) {
            colors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

    private void setupChart(BarChart chart) {
        // Label bottom
        chart.getXAxis().setGranularity(1f); // Hiển thị từng cột
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Vị trí trục X

        // Không hiển thị lưới
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        // Ẩn label mặc định
        chart.getDescription().setEnabled(false);

        // Luôn bắt đầu giá trị Y từ 0
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);

        // Chú thích
        Legend legend = chart.getLegend();
        legend.setTextColor(getResources().getColor(R.color.black));
        legend.setTextSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        chart.setExtraOffsets(0, 0, 0, 20f);
    }

    private BarData setupDataset(String title) {
        dataSet = new BarDataSet(entries, title);
        randomColors();
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.black)); // Màu chữ trên cột
        dataSet.setValueTextSize(12f); // Kích thước chữ

        return new BarData(dataSet);
    }

    private void setupSpinnerOption(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOption.setAdapter(adapter);
    }

    private void setDataGender() {
        entries.clear();
        long maleGender = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByGender(0);
        long femaleGender = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByGender(1);
        long otherGender = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByGender(2);

        entries.add(new BarEntry(0, maleGender));
        entries.add(new BarEntry(1, femaleGender));
        entries.add(new BarEntry(2, otherGender));

        // Thiết lập dữ liệu cho BarChart
        chart.setData(setupDataset("Giới tính"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Nam", "Nữ", "Khác"}));

        // Animation
        chart.animateY(1000);
    }

    private void setDataDepartment() {
        entries.clear();
        List<Department> list = AppDatabase.getInstance(this).departmentDao().getActiveDepartment();
        ArrayList<String> labels = new ArrayList<>();

        for (int i=0; i<list.size(); i++) {
            Department d = list.get(i);
            long count = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByDepartment(d.getDepartmentId());
            entries.add(new BarEntry(i, count));
            labels.add(d.getDepartmentName());
        }

        chart.setData(setupDataset("Phòng ban"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // Animation
        chart.animateY(1000);
    }

    private void setDataWorkplace() {
        entries.clear();
        List<Workplace> list = AppDatabase.getInstance(this).workplaceDao().getActiveWorkplace();
        ArrayList<String> labels = new ArrayList<>();

        for (int i=0; i<list.size(); i++) {
            Workplace w = list.get(i);
            long count = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByWorkplace(w.getWorkplaceId());
            entries.add(new BarEntry(i, count));
            labels.add(w.getWorkplaceName());
        }

        chart.setData(setupDataset("Cở sở làm việc"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // Animation
        chart.animateY(1000);
    }

    private void setDataPosition() {
        entries.clear();
        List<Position> list = AppDatabase.getInstance(this).positionDao().getAll();
        ArrayList<String> labels = new ArrayList<>();

        for (int i=0; i<list.size(); i++) {
            Position p = list.get(i);
            long count = AppDatabase.getInstance(this).employeeDao().getEmployeeCountByDepartment(p.getPositionId());
            entries.add(new BarEntry(i, count));
            labels.add(p.getPositionName());
        }

        chart.setData(setupDataset("Vị trí làm việc trong phòng ban"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // Animation
        chart.animateY(1000);
    }
}