package com.example.myapplication.MainApp.Department;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainApp.Employee.EmployeeAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;

import java.util.List;

public class EmployeeInDepartmentAdapter extends RecyclerView.Adapter<EmployeeInDepartmentAdapter.EmployeeInDepartmentViewHolder>{
    private List<Employee> listEmployee;

    public EmployeeInDepartmentAdapter(List<Employee> listEmployee) {
        this.listEmployee = listEmployee;
    }

    public void setData(List<Employee> list) {
        this.listEmployee = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeInDepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_in_department, parent, false);
        return new EmployeeInDepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeInDepartmentViewHolder holder, int position) {
        Employee employee = listEmployee.get(position);

        if(employee == null) {
            return;
        }

        holder.tvName.setText(employee.getFullName());

        String positionName = "";
        if(employee.getPositionId() != null) {
            Position employeePosition = AppDatabase.getInstance(holder.itemView.getContext()).positionDao().getPositionById(employee.getPositionId());
            positionName = employeePosition.getPositionName();
        }
        holder.tvPosition.setText(String.format("Vị trí: %s", positionName));

        // Hiển thị ảnh avatar
        String imagePath = employee.getImagePath();
        if (!imagePath.isEmpty()) {
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .apply(options)
                    .into(holder.imgAvatar);
        }
    }

    @Override
    public int getItemCount() {
        if(listEmployee != null) {
            return listEmployee.size();
        }
        return 0;
    }

    public class EmployeeInDepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPosition;
        ImageView imgAvatar;

        public EmployeeInDepartmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPosition = itemView.findViewById(R.id.tv_position);
            imgAvatar = itemView.findViewById(R.id.image_avatar);
        }
    }
}
