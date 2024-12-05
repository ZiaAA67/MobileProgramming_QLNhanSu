package com.example.myapplication.MainApp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myapplication.MainApp.EmployeeRequest.SpinnerAdapter;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Position;
import com.example.myapplication.database.entities.Workplace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowSpinner {
    public static void setupSpinnerDepartment(Spinner spinner, Context context) {
        List<String> data = new ArrayList<>();
        List<Department> listDepartment = AppDatabase.getInstance(context).departmentDao().getActiveDepartment();
        if(listDepartment != null) {
            data.add("Chọn phòng ban");
            listDepartment.forEach(d -> data.add(d.getDepartmentName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void setupSpinnerPosition(Spinner spinner, Context context) {
        List<String> data = new ArrayList<>();
        List<Position> listPosition = AppDatabase.getInstance(context).positionDao().getAll();
        if(listPosition != null) {
            data.add("Chọn vị trí của nhân viên");
            listPosition.forEach(d -> data.add(d.getPositionName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void setupSpinnerWorkplace(Spinner spinner, Context context) {
        List<String> data = new ArrayList<>();
        List<Workplace> listWorkplace = AppDatabase.getInstance(context).workplaceDao().getActiveWorkplace();
        if(listWorkplace != null) {
            data.add("Chọn cơ sở làm việc");
            listWorkplace.forEach(d -> data.add(d.getWorkplaceName()));
        } else {
            data.add("Không tồn tại dữ liệu");
        }
        SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void setupSpinnerGender(Spinner spinner, Context context) {
        List<String> items = new ArrayList<>(Arrays.asList(new String[]{"Nam", "Nữ", "Khác"}));
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
