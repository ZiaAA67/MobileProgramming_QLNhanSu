package com.example.myapplication.MainApp;

import com.example.myapplication.Configuration;
import com.example.myapplication.database.entities.User;

import java.util.regex.Pattern;

public class CheckInput {
    // Check passwrod ít nhất 8 ký tự, có chữ hoa, chữ thường và số
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(regex, password);
    }
}
