package com.example.myapplication.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Configuration;
import com.example.myapplication.InformationRegister;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.main_app.GiaoDienChinh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


public class GiaoDienLogin extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;

    String username;
    String password;

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

        // setup spinner language
        setupSpinnerLanguage();


        // bắt sự kiện click nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                User user = AppDatabase.getInstance(GiaoDienLogin.this).userDao().getUserByUsername(username);

                if(user == null) {
                    edtUsername.setError("Tài khoản không tồn tại!");
                    edtUsername.setText("");
                    edtPassword.setText("");
                    edtUsername.requestFocus();
                    return;
                }

                if(!Objects.equals(user.getPassword(), Configuration.md5(password))) {
                    edtPassword.setError("Mật khẩu không chính xác!");
                    edtPassword.setText("");
                    edtPassword.requestFocus();
                    return;
                }

                Toast.makeText(GiaoDienLogin.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GiaoDienLogin.this, GiaoDienChinh.class);
                intent.putExtra("UserID", user.getUsersId());
                startActivity(intent);
            }
        });


        // bắt sự kiện click nút đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiaoDienLogin.this, InformationRegister.class);
                startActivity(intent);
            }
        });
    }


    private void bindingView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }


    private void setupSpinnerLanguage() {
        Spinner spinner = findViewById(R.id.language_spinner);
        String[] countryNames = {"Vietnam", "USA"};
        int[] flags = {R.drawable.flag_vietnam, R.drawable.flag_english};

        FlagAdapter adapter = new FlagAdapter(this, countryNames, flags);

        spinner.setAdapter(adapter);
    }



}