package com.example.myapplication.MainApp.UserAccount;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.CheckInput;
import com.example.myapplication.MainApp.Employee.EmployeeAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAccountAddItem extends AppCompatActivity {
    private List<Employee> listEmployee;
    private RecyclerView rcvEmployee;
    private EmployeeAdapter employeeAdapter;
    private SearchView searchView;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_account_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        // Setup adapter
        listEmployee = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(listEmployee, new EmployeeAdapter.IClickItemEmployee() {
            @Override
            public void clickUpdateEmployee(Employee employee) {
                handleAddUserToEmployee(employee);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);
        rcvEmployee.setAdapter(employeeAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchWithName(s);
                return true;
            }
        });

        btnBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void handleAddUserToEmployee(Employee employee) {
        // Show dialog
        Dialog dialog = new Dialog(UserAccountAddItem.this);
        int layout = R.layout.dialog_add_user_layout;
        Configuration.showDialog(dialog, layout);

        // Binding view
        EditText edtUsername = dialog.findViewById(R.id.edt_username);
        Spinner spinnerRole = dialog.findViewById(R.id.spinner_role);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);

        // Setup spinner
        List<String> data = new ArrayList<>();
        setupSpinnerInDialog(data, spinnerRole);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnAdd.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String role = (String) spinnerRole.getSelectedItem();
            String password = Configuration.randomString(20);

            if(!CheckInput.checkUsername(UserAccountAddItem.this, edtUsername, username)) {
                return;
            }

            // tạo user mới
            int roleId = AppDatabase.getInstance(UserAccountAddItem.this).roleDao().getRoleByName(role).getRoleId();
            User user = new User(username, password, Configuration.STRING_TODAY, true, true, roleId);
            int userId = (int) AppDatabase.getInstance(UserAccountAddItem.this).userDao().insertReturnId(user);

            // gán cho employee
            employee.setUserId(userId);
            AppDatabase.getInstance(UserAccountAddItem.this).employeeDao().update(employee);

            // Gửi mail thông tin về user cho employee
            String to = employee.getEmail();
            String sub = "Thông Tin Tài Khoản Ứng Dụng 2NT";
            String content = "<span><strong> Chúc mừng bạn đã trở thành thành viên công ty 2NT &#127881;&#127881;&#127881; </strong></span>\n" +
                            "<p> Đây là thông tin tài khoản của bạn:</p>\n" +
                            "<p style=\"color:red\"> &#128204; Lưu ý: Hãy đổi mật khẩu trong lần đầu tiên đăng nhập!!!</p>\n" +
                            "Tên tài khoản: " + username + "\n" +
                            "Mật khẩu: " + password + "\n" +
                            "<img width=400 src=\"https://res.cloudinary.com/dbmwgavqz/image/upload/v1733932636/illustration-calligraphic-inscription-congratulations-vector-600nw-1417624778_yv9fzx.jpg\"/>";

            // Gửi mail đăng ký thành công
            Configuration.sendMail(this, to, sub, content);

            dialog.dismiss();

            loadData();
        });

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultKey", "success");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void setupSpinnerInDialog(List<String> data, Spinner spinner) {
        AppDatabase.getInstance(this).roleDao().getAllRoles().forEach(r -> data.add(r.getRoleName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserAccountAddItem.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadData() {
        listEmployee = AppDatabase.getInstance(this).employeeDao().getEmployeeUnHaveUserAccount();
        employeeAdapter.setData(listEmployee);
    }

    private void searchWithName(String kw) {
        List<Employee> list = listEmployee.stream().filter(e -> e.getFullName().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void bindingView() {
        rcvEmployee = findViewById(R.id.rcv_employee);
        searchView = findViewById(R.id.search_view);
        btnBack = findViewById(R.id.btn_back);
    }
}