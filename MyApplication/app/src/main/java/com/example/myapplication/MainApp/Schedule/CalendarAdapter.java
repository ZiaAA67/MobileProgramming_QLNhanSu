package com.example.myapplication.MainApp.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> days;
    private Calendar calendar;
    private ArrayList<LeaveDay> leaveDays;

    public CalendarAdapter(Context context, ArrayList<String> days, Calendar calendar, ArrayList<LeaveDay> leaveDays) {
        this.context = context;
        this.days = days;
        this.calendar = calendar;
        this.leaveDays = leaveDays;
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

        TextView tvDay = convertView.findViewById(R.id.tvDay);
        String day = days.get(position);

        tvDay.setText(day);

        if (!day.isEmpty()) {
            int dayInt = Integer.parseInt(day);
            LeaveDay leaveDay = findLeaveDay(dayInt, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

            if (leaveDay != null) {
                tvDay.setBackgroundResource(leaveDay.getLeaveType().equals("long") ? R.drawable.absence : R.drawable.long_absence);
                tvDay.setText(day + "\n" + leaveDay.getEmployeeName());
            } else {
                tvDay.setBackgroundResource(R.drawable.bg_item_day);
            }
        } else {
            tvDay.setBackgroundResource(R.drawable.bg_item_day);
        }

        return convertView;
    }

    private LeaveDay findLeaveDay(int day, int month, int year) {
        for (LeaveDay leaveDay : leaveDays) {
            if (leaveDay.getDay() == day && leaveDay.getMonth() == month && leaveDay.getYear() == year) {
                return leaveDay;
            }
        }
        return null;
    }
}
