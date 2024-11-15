package com.example.myapplication.main_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.database.entities.Employee;

import java.util.List;

public class RequestEmployeeAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> list;

    public RequestEmployeeAdapter(@NonNull Context context, List<Employee> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_request_employee, parent, false);
        }

        Employee employee = list.get(position);

        ((TextView) convertView.findViewById(R.id.tv_employee_name)).setText(String.format("%s", employee.getFullName()));
        ((TextView) convertView.findViewById(R.id.tv_employee_address)).setText(String.format("Địa chỉ: %s", employee.getAddress()));
        ((TextView) convertView.findViewById(R.id.tv_employee_birth)).setText(String.format("Ngày sinh: %s", employee.getBirth()));
        String gender = employee.getGender() == 0 ? "Nam" : "Nữ";
        ((TextView) convertView.findViewById(R.id.tv_employee_gender)).setText(String.format(", Giới tính: %s", gender));
        ((TextView) convertView.findViewById(R.id.tv_employee_phone)).setText(String.format("SĐT: %s", employee.getPhoneNumber()));
        ((TextView) convertView.findViewById(R.id.tv_employee_email)).setText(String.format("Email: %s", employee.getEmail()));

        return convertView;
    }
}
