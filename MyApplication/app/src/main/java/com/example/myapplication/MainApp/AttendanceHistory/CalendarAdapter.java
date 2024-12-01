package com.example.myapplication.MainApp.AttendanceHistory;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> days;
    private int currentMonth;
    private int currentYear;

    private List<Integer> markedDays;

    public CalendarAdapter(Context context, ArrayList<String> days, int currentMonth, int currentYear, List<Integer> markedDays) {
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
        // Kiểm tra nếu convertView là null, nếu có thì tạo mới
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        }

        // Lấy TextView từ convertView
        TextView dayTextView = convertView.findViewById(R.id.tvDay);

        // Lấy giá trị day từ danh sách
        dayTextView.setText(days.get(position));

        int day = Integer.parseInt(days.get(position));

        // Kiểm tra nếu day có trong danh sách markedDays
        if (markedDays.contains(day)) {
            // Nếu có, sử dụng drawable với màu nền xanh
            convertView.setBackgroundResource(R.drawable.marked);
        } else {
            // Nếu không, sử dụng drawable với màu nền trắng
            convertView.setBackgroundResource(R.drawable.non_marked);
        }


        return convertView;

    }

}







