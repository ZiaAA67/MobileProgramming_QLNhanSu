package com.example.myapplication.MainApp.Department;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;


import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder>{
    private List<Department> listDepartment;
    private DepartmentAdapter.IClickItemDepartment iClickItemDepartment;

    public DepartmentAdapter(List<Department> listDepartment, DepartmentAdapter.IClickItemDepartment iClickItemDepartment) {
        this.listDepartment = listDepartment;
        this.iClickItemDepartment = iClickItemDepartment;
    }

    public void setData(List<Department> list) {
        this.listDepartment = list;
        notifyDataSetChanged();
    }

    public interface IClickItemDepartment {
        void clickUpdateDepartment(Department department);
    }

    @NonNull
    @Override
    public DepartmentAdapter.DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull DepartmentViewHolder holder) {
        super.onViewRecycled(holder);

        holder.background.setVisibility(View.GONE);
        holder.background.setTranslationX(0);
        holder.foreground.setTranslationX(0);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentAdapter.DepartmentViewHolder holder, int position) {
        Department department = listDepartment.get(position);

        if(department == null) {
            return;
        }

        holder.tvDepartmentName.setText("Phòng " + department.getDepartmentName());
        holder.tvDepartmentDesc.setText(String.format("Mô tả: %s", department.getDescription()));

        long quantity = AppDatabase.getInstance(holder.itemLayout.getContext()).employeeDao().getEmployeeCountByDepartment(department.getDepartmentId());
        holder.tvQuantityEmployee.setText(String.format("Số lượng nhân viên: %d", quantity));

        holder.itemLayout.setOnClickListener(v -> {
            iClickItemDepartment.clickUpdateDepartment(department);
        });

    }

    @Override
    public int getItemCount() {
        if(listDepartment != null) {
            return listDepartment.size();
        }
        return 0;
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartmentName;
        TextView tvDepartmentDesc;
        TextView tvQuantityEmployee;
        View itemLayout;
        View background;
        View foreground;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDepartmentName = itemView.findViewById(R.id.tv_department_name);
            tvDepartmentDesc = itemView.findViewById(R.id.tv_department_desc);
            tvQuantityEmployee = itemView.findViewById(R.id.tv_quantity_employee);
            itemLayout = itemView.findViewById(R.id.item_layout);
            background = itemView.findViewById(R.id.layout_background);
            foreground = itemView.findViewById(R.id.layout_foreground);
        }
    }
}
