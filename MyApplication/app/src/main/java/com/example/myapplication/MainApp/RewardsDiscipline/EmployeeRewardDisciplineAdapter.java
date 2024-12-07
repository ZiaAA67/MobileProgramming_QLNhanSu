package com.example.myapplication.MainApp.RewardsDiscipline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Position;

import java.util.List;

public class EmployeeRewardDisciplineAdapter extends RecyclerView.Adapter<EmployeeRewardDisciplineAdapter.EmployeeViewHolder> {

    private Context context;
    private List<Employee> mListEmployee;

    public void setData(List<Employee> list, Context context) {
        this.mListEmployee = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_add_reward, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = mListEmployee.get(position);
        if (employee == null) {
            return;
        }

        holder.tvEmployeename.setText(employee.getFullName());
        holder.tvDepartment.setText(fetchDepartmentName(employee.getDepartmentId()));
        holder.tvPosition.setText(fetchPositionName(employee.getPositionId()));
        holder.tvemployeeid.setText(String.valueOf(employee.getEmployeeId()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRewardDisciplineDialog(employee);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListEmployee != null) {
            return mListEmployee.size();
        }
        return 0;
    }

    private String fetchPositionName(Integer positionId) {
        if (positionId != null) {
            Position position = AppDatabase.getInstance(context).positionDao().getPositionById(positionId);
            return position != null ? position.getPositionName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private String fetchDepartmentName(Integer departmentId) {
        if (departmentId != null) {
            Department department = AppDatabase.getInstance(context).departmentDao().getById(departmentId);
            return department != null ? department.getDepartmentName() : "Không rõ!";
        }
        return "Không rõ!";
    }

    private void showAddRewardDisciplineDialog(Employee employee) {
        AddRewardDisciplineDialog dialog = new AddRewardDisciplineDialog(context, employee);
        dialog.show();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvEmployeename;
        private TextView tvPosition;
        private TextView tvDepartment;
        private TextView tvemployeeid;
        private Button btnAdd;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmployeename = itemView.findViewById(R.id.tv_emloyeename);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvDepartment = itemView.findViewById(R.id.tv_department);
            tvemployeeid = itemView.findViewById(R.id.tv_employeeid);
            btnAdd = itemView.findViewById(R.id.btn_approve);
        }
    }
}
