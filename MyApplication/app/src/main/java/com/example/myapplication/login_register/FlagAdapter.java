package com.example.myapplication.login_register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class FlagAdapter extends BaseAdapter {
    private Context context;
    private String[] countryNames;
    private int[] flags;

    public FlagAdapter(Context context, String[] countryNames, int[] flags) {
        this.context = context;
        this.countryNames = countryNames;
        this.flags = flags;
    }

    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int position) {
        return countryNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.language_spinner, parent, false);
        }

        ImageView flagIcon = convertView.findViewById(R.id.flag_icon);
        TextView flagName = convertView.findViewById(R.id.flag_name);

        // Hiển thị lá cờ trong chế độ chính
        flagIcon.setImageResource(flags[position]);
        flagName.setVisibility(View.GONE); // Ẩn tên trong chế độ chính

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.language_spinner, parent, false);
        }

        ImageView flagIcon = convertView.findViewById(R.id.flag_icon);
        TextView flagName = convertView.findViewById(R.id.flag_name);

        // Thiết lập hình ảnh lá cờ và tên
        flagIcon.setImageResource(flags[position]);
        flagName.setText(countryNames[position]);

        return convertView;
    }

}
