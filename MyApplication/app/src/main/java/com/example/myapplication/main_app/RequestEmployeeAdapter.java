package com.example.myapplication.main_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.login_register.GiaoDienLogin;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RequestEmployeeAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> list;

    public RequestEmployeeAdapter(@NonNull Context context, List<Employee> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_request_employee, parent, false);
        }

        Employee employee = list.get(position);

        ((TextView) convertView.findViewById(R.id.tv_employee_name)).setText(String.format("%s", employee.getFullName()));
        ((TextView) convertView.findViewById(R.id.tv_employee_address)).setText(String.format("Địa chỉ: %s", employee.getAddress()));
        ((TextView) convertView.findViewById(R.id.tv_employee_birth)).setText(String.format("Ngày sinh: %s", employee.getBirth()));
//        String gender = employee.getGender() == 0 ? "Nam" : "Nữ";
//        ((TextView) convertView.findViewById(R.id.tv_employee_gender)).setText(String.format(", Giới tính: %s", gender));
        ((TextView) convertView.findViewById(R.id.tv_employee_phone)).setText(String.format("SĐT: %s", employee.getPhoneNumber()));
        ((TextView) convertView.findViewById(R.id.tv_employee_email)).setText(String.format("Email: %s", employee.getEmail()));

        Button btnApprove = convertView.findViewById(R.id.btn_approve);
        Button btnDisapprove = convertView.findViewById(R.id.btn_disapprove);



        btnApprove.setOnClickListener(view -> {
            handleApproveClick(employee);
        });

        btnDisapprove.setOnClickListener(view -> {
            handleDisapproveClick(employee);
        });

        return convertView;
    }

    private void handleApproveClick(Employee employee) {
        employee.setActive(1);
        AppDatabase.getInstance(this.getContext()).employeeDao().update(employee);
        list.remove(employee);
        this.notifyDataSetChanged();

        String to = employee.getEmail();
        String sub = "Đăng ký thông tin thành công!!!";

        String username = Configuration.makeUsername(employee.getFullName(), employee.getPhoneNumber());
        String password = Configuration.randomString(10);

        String content =
                "Chúc mừng bạn đã đăng ký thông tin thành công, đây là tài khoản và mật khẩu của bạn. \n" +
                "Vui lòng đổi mật khẩu trong lần đăng nhập đầu tiên! \n\n" +
                "Tên tài khoản: " + username + "\n" +
                "Mật khẩu: " + password + "\n";


        Configuration.sendMail(context, to, sub, content);
    }

    private void handleDisapproveClick(Employee employee) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle("Cảnh báo!");
        dialog.setMessage("Bạn có chắc chắn muốn từ chối?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeEmployee(employee);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        dialog.create().show();
    }

    private void removeEmployee(Employee employee) {
        list.remove(employee);
        AppDatabase.getInstance(this.getContext()).employeeDao().delete(employee);
        this.notifyDataSetChanged();
    }


}
