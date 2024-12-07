package com.example.myapplication.MainApp.RewardsDiscipline;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.RewardDiscipline;

public class AddRewardDiscipline extends AppCompatActivity {

    private Button btnBack;
    private Button btnAddNewRewardDiscipline;
    private EditText edtRewardDisciplineName;
    private EditText edtContent;
    private Spinner spinnerType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rewards_discipline);

        initUI();
        setupSpinnerType();

        btnBack.setOnClickListener(view -> finish());

        btnAddNewRewardDiscipline.setOnClickListener(view -> {
            if (isInputValid()) {
                addRewardDiscipline();
            }
        });
    }

    private void addRewardDiscipline() {
        try {
            String name = edtRewardDisciplineName.getText().toString().trim();
            int type = spinnerType.getSelectedItemPosition();
            String content = edtContent.getText().toString().trim();

            RewardDiscipline rewardDiscipline = new RewardDiscipline(name, type, content);
            AppDatabase.getInstance(this).rewardDisciplineDao().insert(rewardDiscipline);

            Toast.makeText(this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();

            clearInputFields();
        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isInputValid() {
        String name = edtRewardDisciplineName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            edtRewardDisciplineName.setError("Vui lòng nhập tên!");
            edtRewardDisciplineName.requestFocus();
            return false;
        }

        String content = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            edtContent.setError("Vui lòng nhập nội dung!");
            edtContent.requestFocus();
            return false;
        }

        if (spinnerType.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Vui lòng chọn loại!", Toast.LENGTH_SHORT).show();
            spinnerType.requestFocus();
            return false;
        }

        return true;
    }

    private void setupSpinnerType() {
        String[] items = new String[]{"Kỷ luật", "Khen thưởng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spiner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void clearInputFields() {
        edtRewardDisciplineName.setText("");
        edtContent.setText("");
        spinnerType.setSelection(0);
        edtRewardDisciplineName.requestFocus();
    }

    private void initUI() {
        edtRewardDisciplineName = findViewById(R.id.edt_reward_discipline_name);
        spinnerType = findViewById(R.id.spinner_type);
        edtContent = findViewById(R.id.edt_content);
        btnAddNewRewardDiscipline = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);
    }
}
