package com.example.myapplication.login_register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class GiaoDienChinh extends AppCompatActivity {


//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giao_dien_chinh);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.home) {
                    Toast.makeText(GiaoDienChinh.this, "Home page", Toast.LENGTH_SHORT).show();
                } else if(itemId == R.id.notification) {
                    Toast.makeText(GiaoDienChinh.this, "Notification page", Toast.LENGTH_SHORT).show();
                } else if(itemId == R.id.profile) {
                    Toast.makeText(GiaoDienChinh.this, "Profile page", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }



}
