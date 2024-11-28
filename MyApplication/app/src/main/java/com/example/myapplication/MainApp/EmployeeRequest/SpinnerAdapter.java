package com.example.myapplication.MainApp.EmployeeRequest;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> list;


    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        if (position == 0) {
            view.setBackgroundColor(Color.LTGRAY);
            TextView textView = (TextView) view;
            textView.setTextColor(Color.DKGRAY);
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        // Vô hiệu hóa item đầu tiên
        return position != 0;
    }
}
