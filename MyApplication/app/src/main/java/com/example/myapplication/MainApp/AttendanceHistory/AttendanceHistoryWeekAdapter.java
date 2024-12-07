package com.example.myapplication.MainApp.AttendanceHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;

import java.util.List;

public class AttendanceHistoryWeekAdapter extends RecyclerView.Adapter<AttendanceHistoryWeekAdapter.TimekeepingViewHolder> {

    private List<Timekeeping> timekeepings;
    private Context context;

    public void setData(List<Timekeeping> timekeepings, Context context) {
        this.timekeepings = timekeepings;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimekeepingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendence_history, parent, false);
        return new TimekeepingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimekeepingViewHolder holder, int position) {
        Timekeeping timekeeping = timekeepings.get(position);

        try {
            Session session = AppDatabase.getInstance(context).sessionDao().getSessionById(timekeeping.getSessionId());
            Shift shift = AppDatabase.getInstance(context).shiftDao().getShiftById(session.getShiftId());

            holder.date.setText(String.format("%02d/%02d/%d", session.getDay(), session.getMonth(), session.getYear()));
            holder.shiftName.setText(shift.getShiftType());
            holder.timeInShift.setText(shift.getTimeStart());
            holder.timeOutShift.setText(shift.getTimeEnd());
            holder.timeInTimeKeeping.setText(timekeeping.getTimeIn());
            holder.timeOutTimeKeeping.setText(timekeeping.getTimeOut());

        } catch (Exception ex) {
            Toast.makeText(context, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (timekeepings != null)
            return timekeepings.size();
        return 0;
    }

    public class TimekeepingViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView shiftName;
        private TextView timeInShift;
        private TextView timeOutShift;
        private TextView timeInTimeKeeping;
        private TextView timeOutTimeKeeping;

        public TimekeepingViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_date);
            shiftName = itemView.findViewById(R.id.tv_shift_name);
            timeInShift = itemView.findViewById(R.id.tv_checkin_time);
            timeOutShift = itemView.findViewById(R.id.tv_checkout_time);
            timeInTimeKeeping = itemView.findViewById(R.id.tv_checkin_time_actual);
            timeOutTimeKeeping = itemView.findViewById(R.id.tv_checkout_time_actual);
        }
    }
}
