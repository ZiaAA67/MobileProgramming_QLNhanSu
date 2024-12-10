package com.example.myapplication.MainApp.AttendanceHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryMonthAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> days;
    private int currentMonth;
    private int currentYear;

    private List<Integer> markedDays;

    public AttendanceHistoryMonthAdapter(Context context, ArrayList<String> days, int currentMonth, int currentYear, List<Integer> markedDays) {
        this.context = context;
        this.days = days;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.markedDays = markedDays;
    }


    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.tvDay);
        dayTextView.setText(days.get(position));

        // Make green
        int day = Integer.parseInt(days.get(position));
        // Kiểm tra nếu day có trong danh sách markedDays
        if (markedDays.contains(day)) {
            dayTextView.setBackgroundColor(context.getResources().getColor(R.color.green)); // Màu xanh
            // Nếu có, sử dụng drawable với màu nền xanh
            convertView.setBackgroundResource(R.drawable.marked);
        } else {
            dayTextView.setBackgroundColor(context.getResources().getColor(R.color.white)); // Màu trắng mặc định
            // Nếu không, sử dụng drawable với màu nền trắng
            convertView.setBackgroundResource(R.drawable.non_marked);
        }

        return convertView;
    }
}