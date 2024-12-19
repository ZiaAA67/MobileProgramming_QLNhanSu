package com.example.myapplication.MainApp.SalaryManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Salary;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class EmployeeSalaryManagerAdapter extends RecyclerView.Adapter<EmployeeSalaryManagerAdapter.EmployeeViewHolderr> {

    private Context context;
    private List<Employee> listEmployee;
    private IClickItemEmployee iClickItemEmployee;

    public EmployeeSalaryManagerAdapter(Context context, IClickItemEmployee listener) {
        this.context = context;
        this.iClickItemEmployee = listener;
    }

    public void setData(Context context, List<Employee> list) {
        this.listEmployee = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee_salary, parent, false);
        return new EmployeeViewHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolderr holder, int position) {
        Employee employee = listEmployee.get(position);

        if (employee == null) {
            return;
        }

        holder.tvName.setText(employee.getFullName());
        holder.tvId.setText(String.format("Mã nhân viên: %s", employee.getEmployeeId()));
        holder.tvBasic.setText("Lương cơ bản: " + formatNumber(fetchSalary(employee.getSalaryId(), "basic")));
        holder.tvAllowance.setText("Trợ cấp: " + formatNumber(fetchSalary(employee.getSalaryId(), "allowance")));
        holder.tvCoefficient.setText("Hệ số: " + formatNumber(fetchSalary(employee.getSalaryId(), "coefficient")));

        String imagePath = employee.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .apply(options)
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(v -> iClickItemEmployee.onClickItem(employee));
    }

    private String fetchSalary(Integer salaryId, String type) {
        if (salaryId != null) {
            Salary salary = AppDatabase.getInstance(context).salaryDao().getSalaryById(salaryId);
            if (salary != null) {
                switch (type) {
                    case "basic":
                        return salary.getBasicSalary() != null ? salary.getBasicSalary().toString() : "0";
                    case "allowance":
                        return salary.getAllowance() != null ? salary.getAllowance().toString() : "0";
                    case "coefficient":
                        return salary.getCoefficient() != null ? salary.getCoefficient().toString() : "0";
                    default:
                        return "0";
                }
            }
        }
        return "0";
    }

    private String formatNumber(String number) {
        try {
            BigDecimal bd = new BigDecimal(number);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");  // You can adjust the format as needed
            return decimalFormat.format(bd);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    @Override
    public int getItemCount() {
        if (listEmployee != null) {
            return listEmployee.size();
        }
        return 0;
    }

    public class EmployeeViewHolderr extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName;
        TextView tvId;
        TextView tvBasic;
        TextView tvAllowance;
        TextView tvCoefficient;

        public EmployeeViewHolderr(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_employee);
            tvName = itemView.findViewById(R.id.tv_employee_name);
            tvId = itemView.findViewById(R.id.tv_employee_id);
            tvBasic = itemView.findViewById(R.id.tv_basic_salary);
            tvAllowance = itemView.findViewById(R.id.tv_allowance);
            tvCoefficient = itemView.findViewById(R.id.tv_coefficient);
        }
    }

    public interface IClickItemEmployee {
        void onClickItem(Employee employee);
    }
}