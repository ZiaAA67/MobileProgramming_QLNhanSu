package com.example.myapplication.MainApp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.MainApp.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.MainApp.Register.Register;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.MainApp.Fragment.GiaoDienChinh;

import java.util.Objects;

public class GiaoDienLogin extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;

    String username;
    String password;
    private boolean isSpinnerInitialized = false; // Biến kiểm tra trạng thái khởi tạo

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giao_dien_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        bindingView();


        // Bắt sự kiện click nút Đăng nhập
        btnLogin.setOnClickListener(view -> {
            username = edtUsername.getText().toString().trim();
            password = edtPassword.getText().toString().trim();

            if (username.isEmpty()) {
                edtUsername.setError("Vui lòng nhập tài khoản!");
                return;
            }

            if (password.isEmpty()) {
                edtPassword.setError("Vui lòng nhập mật khẩu!");
                return;
            }

            User user = AppDatabase.getInstance(GiaoDienLogin.this).userDao().getUserByUsername(username);

            if (user == null) {
                edtUsername.setError("Tài khoản không tồn tại!");
                edtUsername.setText("");
                edtPassword.setText("");
                edtUsername.requestFocus();
                return;
            }

            if (!Objects.equals(user.getPassword(), Configuration.md5(password))) {
                edtPassword.setError("Mật khẩu không chính xác!");
                edtPassword.setText("");
                edtPassword.requestFocus();
                return;
            }

            // Check đăng nhập lần đầu
            if (user.isFirstLogin()) {
                // Update lại trạng thái
                user.setFirstLogin(false);
                AppDatabase.getInstance(GiaoDienLogin.this).userDao().update(user);

                Intent intent = new Intent(GiaoDienLogin.this, ChangePassword.class);
                intent.putExtra("user_key", user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(GiaoDienLogin.this, GiaoDienChinh.class);
                intent.putExtra("user_key", user);
                startActivity(intent);
            }
        });

        // Bắt sự kiện click nút Đăng ký
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(GiaoDienLogin.this, Register.class);
            startActivity(intent);
        });
    }

    private void bindingView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }


}
