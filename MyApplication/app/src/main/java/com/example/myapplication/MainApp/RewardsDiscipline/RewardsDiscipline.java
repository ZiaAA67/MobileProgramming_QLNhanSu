package com.example.myapplication.MainApp.RewardsDiscipline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class RewardsDiscipline extends AppCompatActivity {
    private int userId;

    private Button btnBack;
    private Button btnAddRewardDiscipline;
    private EditText edtSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_discipline);

        userId = getIntent().getIntExtra("UserID", -1);
        initUI();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnAddRewardDiscipline.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRewardsDiscipline.class);
            intent.putExtra("UserID", userId);
            startActivity(intent);
        });
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnAddRewardDiscipline = findViewById(R.id.btn_add_reward_discipline);
        edtSearch = findViewById(R.id.edt_search);
    }
}
