package com.example.myapplication.MainApp.LeaveRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.entities.LeaveRequest;

import java.util.List;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.LeaveRequestViewHolder> {

    private List<LeaveRequest> leaveRequests;

    public void setData(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_request_employee, parent, false);
        return new LeaveRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestViewHolder holder, int position) {
        LeaveRequest leaveRequest = leaveRequests.get(position);

        if (leaveRequest == null) {
            return;
        }

        int status = leaveRequest.getStatus();
        String strStatus;

        if (status == 0) {
            strStatus = "Chưa xử lý";
            holder.itemView.setBackgroundResource(R.drawable.pending_background); // Yellow background with green border
        } else if (status == 1) {
            strStatus = "Chấp nhận";
            holder.itemView.setBackgroundResource(R.drawable.accept_background); // Green background with yellow border
        } else if (status == 2) {
            strStatus = "Từ chối";
            holder.itemView.setBackgroundResource(R.drawable.rejected_background); // Red background with black border
        } else {
            strStatus = "Không xác định";
            holder.itemView.setBackgroundResource(R.drawable.rejected_background); // Default grey background
        }

        holder.tvStatus.setText(strStatus);
        holder.tvReason.setText("Lí do: " + leaveRequest.getReason());
        holder.tvSendDay.setText("Ngày gửi: " + leaveRequest.getSendDate());
        holder.tvFromDate.setText("Ngày bắt đầu: " + leaveRequest.getOffDateFrom());
        holder.tvToDate.setText("Ngày kết thúc: " + leaveRequest.getOffDateTo());
        holder.tvTotal.setText("Số ngày: " + leaveRequest.calculateLeaveDays());
    }

    @Override
    public int getItemCount() {
        if (leaveRequests != null)
            return leaveRequests.size();
        return 0;
    }

    public class LeaveRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus;
        private TextView tvReason;
        private TextView tvSendDay;
        private TextView tvFromDate;
        private TextView tvToDate;
        private TextView tvTotal;

        public LeaveRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvReason = itemView.findViewById(R.id.tv_leave_reason);
            tvSendDay = itemView.findViewById(R.id.tv_date_submitted);
            tvFromDate = itemView.findViewById(R.id.tv_leave_from_date);
            tvToDate = itemView.findViewById(R.id.tv_leave_to_date);
            tvTotal = itemView.findViewById(R.id.tv_total_leave_days);
        }
    }
}
