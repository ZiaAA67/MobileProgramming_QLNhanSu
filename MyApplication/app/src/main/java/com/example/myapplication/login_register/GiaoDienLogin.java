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






        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                User user = AppDatabase.getInstance(GiaoDienLogin.this).userDao().getUserByUsername(username);
                if (user != null && Objects.equals(user.getPassword(), md5(password))) {
                    Toast.makeText(GiaoDienLogin.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GiaoDienLogin.this, GiaoDienChinh.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GiaoDienLogin.this, "Đăng nhập thất bại!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                User user = new User(username, md5(password), "15/11/2000", null);
                AppDatabase.getInstance(GiaoDienLogin.this).userDao().insert(user);
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

    // MD5 hashed
    public static String md5(String input) {
        try {
            // Lấy đối tượng MessageDigest với thuật toán MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Băm chuỗi đầu vào và trả về mảng byte
            byte[] bytes = digest.digest(input.getBytes());

            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                // Chuyển đổi từng byte thành chuỗi hex và thêm vào kết quả
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Trả về chuỗi hex
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Nếu thuật toán không tồn tại, trả về null
            e.printStackTrace();
            return null;
        }
    }
}