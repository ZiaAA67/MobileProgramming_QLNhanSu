package com.example.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;

public class InitMediaManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Setup api key Cloudinary
        // Lưu ý, vì MediaManager chỉ nên init một lần duy nhất trong 1 vòng đời --> init ở activity đầu tiên, tránh gọi lại
        MediaManager.init(this, new HashMap<String, String>() {{
            put("cloud_name", "dbmwgavqz");
            put("api_key", "747824214758252");
            put("api_secret", "IjgCUhqhoxQhoiG1dcq-vWJk5wA");
        }});

        // Kiểm tra chế độ sáng/tối từ SharedPreferences và áp dụng cho toàn bộ ứng dụng
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
