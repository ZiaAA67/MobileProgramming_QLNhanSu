package com.example.myapplication.MainApp.Salary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class SalarySlipAdapter extends BaseAdapter {

    private Context context;
    private List<SalarySlipItem> salarySlipItems;
    private LayoutInflater inflater;

    public SalarySlipAdapter(Context context, List<SalarySlipItem> salarySlipItems) {
        this.context = context;
        this.salarySlipItems = salarySlipItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return salarySlipItems.size();
    }

    @Override
    public Object getItem(int position) {
        return salarySlipItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_salary, parent, false);
        }

        TextView tvMonthYear = convertView.findViewById(R.id.tv_month_year);
        TextView tvDateRange = convertView.findViewById(R.id.tv_date_range);

        SalarySlipItem item = salarySlipItems.get(position);
        tvMonthYear.setText("Phiếu lương tháng " + item.getMonth() + "." + item.getYear());
        tvDateRange.setText("01/" + item.getMonth() + "/" + item.getYear() + " - 31/" + item.getMonth() + "/" + item.getYear());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SalarySlipInformation.class);
            intent.putExtra("month", item.getMonth());
            intent.putExtra("year", item.getYear());
            context.startActivity(intent);
        });

        return convertView;
    }
}
