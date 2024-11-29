package com.example.myapplication.MainApp.Employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainApp.UserAccount.UserAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>{
    private Context context;
    private List<Employee> listEmployee;
    private EmployeeAdapter.IClickItemEmployee iClickItemEmployee;

    public EmployeeAdapter(Context context, List<Employee> listEmployee, EmployeeAdapter.IClickItemEmployee iClickItemEmployee) {
        this.context = context;
        this.listEmployee = listEmployee;
        this.iClickItemEmployee = iClickItemEmployee;
    }

    public void setData(List<Employee> list) {
        this.listEmployee = list;
        notifyDataSetChanged();
    }

    public interface IClickItemEmployee {
        void clickUpdateEmployee(Employee employee);
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = listEmployee.get(position);

        if(employee == null) {
            return;
        }

        holder.tvName.setText(employee.getFullName());
        holder.tvBirth.setText(employee.getBirth());
        holder.tvEmail.setText(String.format("Email: %s", employee.getEmail()));
        holder.tvStatus.setText(String.format("Trạng thái: %s", employee.isApprove() ? "Đã được duyệt" : "Chưa được duyệt"));
    }


    @Override
    public int getItemCount() {
        if(listEmployee != null) {
            return listEmployee.size();
        }
        return 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvBirth;
        TextView tvEmail;
        TextView tvStatus;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvBirth = itemView.findViewById(R.id.tv_birth);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
