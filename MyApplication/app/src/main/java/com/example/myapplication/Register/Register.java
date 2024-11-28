package com.example.myapplication.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.Fragment.CheckInput;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;

public class Register extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;
    private Button btnContinue;
    private Button btnBack;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private String username;
    private String password;
    private String passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Employee employee = (Employee) result.getData().getSerializableExtra("employee_key");
                        if (employee != null) {

                            Role publicRole = AppDatabase.getInstance(this).roleDao().getRoleByName("Public");
                            // check có role là Public hay chưa, nếu chưa có thì tạo role
                            if(publicRole == null) {
                                publicRole = new Role("Public", "View only");
                                int roleId = (int)AppDatabase.getInstance(this).roleDao().insertReturnId(publicRole);
                                publicRole.setRoleId(roleId);
                            }

                            // Lưu user vào db
                            User user = new User(username, Configuration.md5(password), Configuration.STRING_TODAY, true, false, publicRole.getRoleId());
                            AppDatabase.getInstance(this).userDao().insert(user);
                            user = AppDatabase.getInstance(this).userDao().getUserByUsername(username);

                            // Liên kết employee được trả về với user, update employee
                            employee.setUserId(user.getUserId());
                            AppDatabase.getInstance(this).employeeDao().update(employee);

                            finish();
                        }
                    }
                }
        );


        btnContinue.setOnClickListener(view -> {
            username = edtUsername.getText().toString().trim();
            password = edtPassword.getText().toString().trim();
            passwordConfirm = edtPasswordConfirm.getText().toString().trim();

            if(!checkInput()) return;

            Intent intent = new Intent(Register.this, InformationRegister.class);
            activityResultLauncher.launch(intent);

        });

        btnBack.setOnClickListener(view -> {
            finish();
        });

    }

    private boolean checkInput() {
        if(!CheckInput.checkUsername(this, edtUsername, username)) {
            return false;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu!");
            edtPassword.requestFocus();
            return false;
        }

        if (!CheckInput.isValidPassword(password)) {
            edtPassword.setError("Mật khẩu cần ít nhất 8 ký tự, bao gồm chữ số, chữ thường, và chữ hoa!");
            edtPassword.requestFocus();
            return false;
        }

        if (passwordConfirm.isEmpty()) {
            edtPasswordConfirm.setError("Vui lòng nhập lại mật khẩu!");
            edtPasswordConfirm.requestFocus();
            return false;
        }

        if(!password.equals(passwordConfirm)) {
            edtPasswordConfirm.setError("Mật khẩu xác nhận không khớp!");
            edtPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    private void bindingView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtPasswordConfirm = findViewById(R.id.edt_password_confirm);
        btnContinue = findViewById(R.id.btn_continue);
        btnBack = findViewById(R.id.btn_back);
    }
}