package com.example.myapplication.MainApp.RewardsDiscipline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddRewardsDiscipline extends AppCompatActivity {

    private Button btnBack;
    private Button btnAddNewRewardDiscipline;
    private EditText edtRewardDisciplineName;
    private Spinner edtType;
    private EditText edtContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rewards_discipline);

        initUI();

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void initUI() {
        edtRewardDisciplineName = findViewById(R.id.edt_reward_discipline_name);
        edtType = findViewById(R.id.edt_type);
        edtContent = findViewById(R.id.edt_content);
        btnAddNewRewardDiscipline = findViewById(R.id.btn_add_reward_discipline);
        btnBack = findViewById(R.id.btn_back);
    }
}
