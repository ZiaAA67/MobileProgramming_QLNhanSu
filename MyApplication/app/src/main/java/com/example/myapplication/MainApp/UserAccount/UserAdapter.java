package com.example.myapplication.MainApp.AccountManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainApp.EmployeeRequest.RequestEmployeeAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context context;
    private List<User> listUser;
    private IClickItemUser iClickItemUser;

    public UserAdapter(Context context, List<User> listUser, IClickItemUser iClickItemUser) {
        this.context = context;
        this.listUser = listUser;
        this.iClickItemUser = iClickItemUser;
    }

    public void setData(List<User> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    public interface IClickItemUser {
        void clickUpdateUser(User user);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);

        if(user == null) {
            return;
        }

        Role role = AppDatabase.getInstance(context).roleDao().getRoleById(user.getRoleId());
        Employee employee = AppDatabase.getInstance(context).employeeDao().getEmployeeByUserId(user.getUserId());

        holder.tvUsername.setText(user.getUsername());
        holder.tvRole.setText(String.format("Vai trò: %s", role.getRoleName()));

        if(employee == null) {
            holder.tvStatus.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(String.format("Nhân viên sở hữu: %s", employee.getFullName()));
        }

        holder.itemLayout.setOnClickListener(v -> {
            iClickItemUser.clickUpdateUser(user);
        });
    }

    @Override
    public int getItemCount() {
        if(listUser != null) {
            return listUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvRole;
        TextView tvStatus;
        View itemLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username);
            tvRole = itemView.findViewById(R.id.tv_role);
            tvStatus = itemView.findViewById(R.id.tv_status);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
