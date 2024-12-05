package com.example.myapplication.MainApp.Employee;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.MainApp.UserAccount.UserAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>{
    private List<Employee> listEmployee;
    private IClickItemEmployee iClickItemEmployee;

    public EmployeeAdapter(List<Employee> listEmployee, EmployeeAdapter.IClickItemEmployee iClickItemEmployee) {
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
        holder.tvBirth.setText(String.format("Ngày sinh: %s", employee.getBirth()));
        holder.tvAddress.setText(String.format("Địa chỉ: %s", employee.getAddress()));
        holder.tvEmail.setText(String.format("Email: %s", employee.getEmail()));
        holder.tvStatus.setText(String.format("Trạng thái: %s", employee.isApprove() ? "Đã được duyệt" : "Chưa được duyệt"));

        // Bắt sự kiện click vào employee để update
        holder.layoutForeground.setOnClickListener(v -> {
            iClickItemEmployee.clickUpdateEmployee(employee);
        });

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

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvBirth;
        TextView tvAddress;
        TextView tvEmail;
        TextView tvStatus;
        ImageView imgAvatar;
        View layoutForeground;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvBirth = itemView.findViewById(R.id.tv_birth);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imgAvatar = itemView.findViewById(R.id.image_avatar);
            layoutForeground = itemView.findViewById(R.id.layout_foreground);
        }
    }
}
