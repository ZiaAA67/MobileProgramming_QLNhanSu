package com.example.myapplication.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.Fragment.CheckInput;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.User;

public class ChangePassword extends AppCompatActivity {

//    private int userID;
    private User user;
    private EditText edtCurrentPass;
    private EditText edtNewPass;
    private EditText edtConfirmPass;
    private Button btnApply;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

//        userID = getIntent().getIntExtra("UserID", -1);
        user = (User) getIntent().getSerializableExtra("user_key");
        initUI();

        btnApply.setOnClickListener(view -> {
            changePassword();
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void changePassword() {
//        if (userID == -1) {
//            showToast("User không tồn tại");
//            return;
//        }
        if(user == null) {
            showToast("User không tồn tại");
        }

        String currentPassword = edtCurrentPass.getText().toString().trim();
        AppDatabase db = AppDatabase.getInstance(this);
//        User user = db.userDao().getUserById(userID);

        if (!(user.getPassword()).equals(Configuration.md5(currentPassword))) {
            edtCurrentPass.setError("Mật khẩu hiện tại không đúng!");
            edtCurrentPass.requestFocus();
            return;
        }

        if (!validatePasswords(currentPassword)) {
            return;
        }

        try {
            user.setPassword(Configuration.md5(edtNewPass.getText().toString().trim()));
            db.userDao().update(user);
            showToast("Lưu thành công!");
            showPasswordChangeSuccessDialog();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }

    public boolean validatePasswords(String currentPassword) {
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword)) {
            edtNewPass.setError("Mật khẩu mới không thể trống!");
            edtNewPass.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPass.setError("Xác nhận mật khẩu không thể trống!");
            edtConfirmPass.requestFocus();
            return false;
        }

        if (currentPassword.equals(newPassword)) {
            edtNewPass.setError("Mật khẩu mới phải khác mật khẩu hiện tại");
            edtNewPass.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPass.setError("Mật khẩu xác nhận không khớp!");
            edtConfirmPass.requestFocus();
            return false;
        }

        if (!CheckInput.isValidPassword(newPassword)) {
            edtNewPass.setError("Mật khẩu cần ít nhất 8 ký tự, bao gồm chữ số, chữ thường, và chữ hoa!");
            edtNewPass.requestFocus();
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showPasswordChangeSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu thành công!")
                .setMessage("Vui lòng đăng nhập lại.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                        backToLogin();
                    }
                })
                .setCancelable(false); // Không cho phép người dùng nhấn ngoài để đóng dialog

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToLogin(){
        Intent intent = new Intent(this, GiaoDienLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp hoạt động
        startActivity(intent);
        finish();
    }

    private void initUI() {
        edtCurrentPass = findViewById(R.id.edt_current_pass);
        edtNewPass = findViewById(R.id.edt_new_pass);
        edtConfirmPass = findViewById(R.id.edt_confirm_pass);
        btnApply = findViewById(R.id.btn_apply);
        btnBack = findViewById(R.id.btn_back);
    }
}