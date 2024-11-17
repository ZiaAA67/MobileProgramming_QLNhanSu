package com.example.myapplication.main_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class GiaoDienChinh extends AppCompatActivity {

    ViewPager2 viewPager2;
    BottomNavigationView navigationView;


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

        // Ánh xạ view
        bindingView();

        // Get USER
        int userId = getIntent().getIntExtra("UserID", -1);

        // Khởi tạo Adapter và gán cho Viewpager2
        Viewpager2Adapter adapter = new Viewpager2Adapter(this, userId);
        viewPager2.setAdapter(adapter);

        // Bắt sự kiện vuốt màn hình
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.notification).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                }
            }
        });

        // Bắt sự kiện bấm vào bottom nav
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.notification:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.profile:
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });



    }

    private void bindingView() {
        navigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.view_pager);
    }

}
