package com.example.myapplication.MainApp;

import android.content.Context;
import android.widget.EditText;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.User;

import java.util.regex.Pattern;

public class CheckInput {
    // Check passwrod ít nhất 8 ký tự, có chữ hoa, chữ thường và số
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(regex, password);
    }

    public static boolean checkUsername(Context context, EditText edtUsername, String username) {
        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên tài khoản!");
            edtUsername.requestFocus();
            return false;
        }

        if(username.length() > 20 || username.contains(" ")) {
            edtUsername.setError("Tài khoản không hợp lệ!");
            edtUsername.requestFocus();
            return false;
        }

        User user = AppDatabase.getInstance(context).userDao().getUserByUsername(username);
        if(user != null) {
            edtUsername.setError("Tài khoản đã tồn tại!");
            edtUsername.requestFocus();
            return false;
        }

        return true;
    }
}
