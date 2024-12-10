package com.example.myapplication.MainApp.Workplace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Workplace;

import java.util.List;

public class WorkplaceAdapter extends RecyclerView.Adapter<WorkplaceAdapter.WorkplaceViewHolder>{
    private List<Workplace> listWorkplace;
    private WorkplaceAdapter.IClickItemWorkplace iClickItemWorkplace;

    public WorkplaceAdapter(List<Workplace> listWorkplace, WorkplaceAdapter.IClickItemWorkplace iClickItemWorkplace) {
        this.listWorkplace = listWorkplace;
        this.iClickItemWorkplace = iClickItemWorkplace;
    }

    public void setData(List<Workplace> list) {
        this.listWorkplace = list;
        notifyDataSetChanged();
    }

    public interface IClickItemWorkplace {
        void clickUpdateWorkplace(Workplace workplace);
    }

    @NonNull
    @Override
    public WorkplaceAdapter.WorkplaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workplace, parent, false);
        return new WorkplaceAdapter.WorkplaceViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull WorkplaceAdapter.WorkplaceViewHolder holder) {
        super.onViewRecycled(holder);

        holder.background.setVisibility(View.GONE);
        holder.background.setTranslationX(0);
        holder.foreground.setTranslationX(0);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkplaceAdapter.WorkplaceViewHolder holder, int position) {
        Workplace workplace = listWorkplace.get(position);

        if(workplace == null) {
            return;
        }

        holder.tvWorkplaceName.setText("Cơ sở " + workplace.getWorkplaceName());
        holder.tvAddress.setText(String.format("Địa chỉ: %s", workplace.getAddress()));

        long quantity = AppDatabase.getInstance(holder.itemLayout.getContext()).employeeDao().getEmployeeCountByWorkplace(workplace.getWorkplaceId());
        holder.tvQuantityEmployee.setText(String.format("Số lượng nhân viên: %d", quantity));

        holder.itemLayout.setOnClickListener(v -> {
            iClickItemWorkplace.clickUpdateWorkplace(workplace);
        });

    }

    @Override
    public int getItemCount() {
        if(listWorkplace != null) {
            return listWorkplace.size();
        }
        return 0;
    }

    public class WorkplaceViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkplaceName;
        TextView tvAddress;
        TextView tvQuantityEmployee;
        View itemLayout;
        View background;
        View foreground;

        public WorkplaceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWorkplaceName = itemView.findViewById(R.id.tv_workplace_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvQuantityEmployee = itemView.findViewById(R.id.tv_quantity_employee);
            itemLayout = itemView.findViewById(R.id.item_layout);
            background = itemView.findViewById(R.id.layout_background);
            foreground = itemView.findViewById(R.id.layout_foreground);
        }
    }
}
