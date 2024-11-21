package com.example.myapplication.MainApp.LeaveRequest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.LeaveRequest;

import java.util.List;

public class  LeaveRequestManagerAdapter extends RecyclerView.Adapter<LeaveRequestManagerAdapter.LeaveRequestManagerViewHolder>{
    private List<LeaveRequest> leaveRequests;
    private IClickItem iClickItem;
    private Context context;


    public interface IClickItem{
        void approvedLeaveRequest(LeaveRequest leaveRequest);

        void dissapprovedLeaveRequest(LeaveRequest leaveRequest);
    }

    public LeaveRequestManagerAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
        this.context = context;
    }

    public void setData(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaveRequestManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_request_manager, parent, false);
        return new LeaveRequestManagerAdapter.LeaveRequestManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestManagerViewHolder holder, int position) {
        final LeaveRequest leaveRequest = leaveRequests.get(position);

        if (leaveRequest == null){
            return;
        }

        int status = leaveRequest.getStatus();
        String strStatus;

        if (status == 0) {
            strStatus = "Chưa xử lý";
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.yellow));
        } else if (status == 1) {
            strStatus = "Chấp nhận";
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.green)); // Màu nền cho trạng thái chưa xử lý
        } else if (status == 2) {
            strStatus = "Từ chối";
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red)); // Màu nền cho trạng thái chưa xử lý
        } else {
            strStatus = "Không xác định";
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.grey)); // Màu nền cho trạng thái chưa xử lý
        }

        int employeeid = leaveRequest.getEmployeeId(); // Lấy ID nhân viên từ LeaveRequest

        Employee employee = AppDatabase.getInstance(context).employeeDao().getById(employeeid);

        String employeeName = (employee != null) ? employee.getFullName() : "Không tìm thấy nhân viên";

        holder.tvStatus.setText(strStatus);
        holder.tvEmployeename.setText("Tên nhân viên: " + employeeName);
        holder.tvReason.setText("Lí do: "+ leaveRequest.getReason());
        holder.tvSendDay.setText("Ngày gửi: " + leaveRequest.getSendDate());
        holder.tvFromDate.setText("Ngày bắt đầu: " + leaveRequest.getOffDateFrom());
        holder.tvToDate.setText("Ngày kết thúc: " + leaveRequest.getOffDateTo());
        holder.tvTotal.setText("Số ngày: " + leaveRequest.calculateLeaveDays());

        holder.btnApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItem.approvedLeaveRequest(leaveRequest);
            }
        });

        holder.btnDisapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItem.dissapprovedLeaveRequest(leaveRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (leaveRequests != null)
            return leaveRequests.size();
        return 0;
    }

    public class LeaveRequestManagerViewHolder extends RecyclerView.ViewHolder{
        private TextView tvStatus;
        private TextView tvEmployeename;
        private TextView tvReason;
        private TextView tvSendDay;
        private TextView tvFromDate;
        private TextView tvToDate;
        private TextView tvTotal;
        private Button btnApproved;
        private Button btnDisapproved;

        public LeaveRequestManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvEmployeename = itemView.findViewById(R.id.tv_employee_name);
            tvReason = itemView.findViewById(R.id.tv_leave_reason);
            tvSendDay = itemView.findViewById(R.id.tv_date_submitted);
            tvFromDate = itemView.findViewById(R.id.tv_leave_from_date);
            tvToDate = itemView.findViewById(R.id.tv_leave_to_date);
            tvTotal = itemView.findViewById(R.id.tv_total_leave_days);
            btnApproved = itemView.findViewById(R.id.btn_approve);
            btnDisapproved = itemView.findViewById(R.id.btn_disapprove);
        }
    }
}
