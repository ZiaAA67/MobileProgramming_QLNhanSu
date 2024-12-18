package com.example.myapplication.MainApp.Employee;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeAddItemAdapter extends RecyclerView.Adapter<EmployeeAddItemAdapter.EmployeeAddItemViewHolder>{
    private List<Employee> listEmployee;

    public EmployeeAddItemAdapter(List<Employee> listEmployee) {
        this.listEmployee = listEmployee;
    }

    public void setData(List<Employee> list) {
        this.listEmployee = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeAddItemAdapter.EmployeeAddItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_full_info, parent, false);
        return new EmployeeAddItemAdapter.EmployeeAddItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAddItemAdapter.EmployeeAddItemViewHolder holder, int position) {
        Employee employee = listEmployee.get(position);

        if(employee == null) {
            return;
        }

        holder.tvName.setText(employee.getFullName());
        holder.tvBirth.setText(String.format("Ngày sinh: %s", employee.getBirth()));
        holder.tvAddress.setText(String.format("Địa chỉ: %s", employee.getAddress()));
        holder.tvEmail.setText(String.format("Email: %s", employee.getEmail()));
        holder.tvPhone.setText(String.format("Số điện thoại: %s", employee.getPhoneNumber()));
        holder.tvIdentity.setText(String.format("CCCD: %s", employee.getIdentityNumber()));

        String gender = "/  ";
        if(employee.getGender() == 0) {
            gender += "Nam";
        } else if(employee.getGender() == 1) {
            gender += "Nữ";
        } else {
            gender = "";
        }
        holder.tvGender.setText(String.format("%s", gender));

        Context context = holder.itemView.getContext();

        if(!checkIdentity(context, employee) || !checkPhone(context, employee) || !checkEmail(context, employee) || !checkDate(employee)) {
            if(!checkIdentity(context, employee)) {
                holder.tvIdentity.setTextColor(ContextCompat.getColor(context, R.color.dis_approve));
            }

            if(!checkPhone(context, employee)) {
                holder.tvPhone.setTextColor(ContextCompat.getColor(context, R.color.dis_approve));
            }

            if(!checkEmail(context, employee)) {
                holder.tvEmail.setTextColor(ContextCompat.getColor(context, R.color.dis_approve));
            }

            if(!checkDate(employee)) {
                holder.tvBirth.setTextColor(ContextCompat.getColor(context, R.color.dis_approve));
            }

            holder.foreground.setBackgroundResource(R.drawable.red_solid_radius_box);
        } else {
            holder.foreground.setBackgroundResource(R.drawable.green_solid_radius_box);
            AppDatabase.getInstance(holder.itemView.getContext()).employeeDao().insert(employee);
        }
    }



    @Override
    public int getItemCount() {
        if(listEmployee != null) {
            return listEmployee.size();
        }
        return 0;
    }

    public class EmployeeAddItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvBirth;
        TextView tvAddress;
        TextView tvEmail;
        TextView tvGender;
        TextView tvIdentity;
        TextView tvPhone;
        View foreground;

        public EmployeeAddItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvBirth = itemView.findViewById(R.id.tv_birth);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvIdentity = itemView.findViewById(R.id.tv_identity);

            foreground = itemView.findViewById(R.id.layout_foreground);
        }
    }


    private boolean checkIdentity(Context context, Employee employee) {
        return AppDatabase.getInstance(context).employeeDao().getByIdentityNumber(employee.getIdentityNumber()) == null;
    }

    private boolean checkPhone(Context context, Employee employee) {
        return AppDatabase.getInstance(context).employeeDao().getByPhoneNumber(employee.getPhoneNumber()) == null;
    }

    private boolean checkEmail(Context context, Employee employee) {
        return AppDatabase.getInstance(context).employeeDao().getByEmail(employee.getEmail()) == null;
    }

    private boolean checkDate(Employee employee) {
        try {
            LocalDate.parse(employee.getBirth(), Configuration.FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
