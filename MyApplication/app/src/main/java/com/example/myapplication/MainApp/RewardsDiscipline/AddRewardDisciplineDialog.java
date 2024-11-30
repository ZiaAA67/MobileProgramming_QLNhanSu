package com.example.myapplication.MainApp.RewardsDiscipline;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Configuration;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_RewardDiscipline;
import com.example.myapplication.database.entities.RewardDiscipline;

import java.util.ArrayList;
import java.util.List;

public class AddRewardDisciplineDialog extends Dialog {

    private TextView tvEmployeeName;
    private Spinner spinnerType, spinnerTarget;
    private EditText edtAmount;
    private Button btnSave, btnCancel;

    private Employee employee;
    private int rewardDisciplineId;

    public AddRewardDisciplineDialog(Context context, Employee employee) {
        super(context);
        this.employee = employee;
        setContentView(R.layout.dialog_employee_reward_discipline);

        tvEmployeeName = findViewById(R.id.tv_employee_name);
        spinnerType = findViewById(R.id.spinner_reward_discipline_type);
        spinnerTarget = findViewById(R.id.spinner_reward_discipline_target);
        edtAmount = findViewById(R.id.edt_amount);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        tvEmployeeName.setText(employee.getFullName());

        setupSpinnerType();

        btnSave.setOnClickListener(v -> {
            String amount = edtAmount.getText().toString();
            String selectedType = spinnerType.getSelectedItem().toString();
            String selectedTarget = spinnerTarget.getSelectedItem().toString();

            if (amount.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            saveRewardDiscipline(selectedType, selectedTarget, amount);

            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void saveRewardDiscipline(String type, String target, String amount) {
        try {
            int amountInt = Integer.parseInt(amount);

            RewardDiscipline rewardDiscipline = getRewardDisciplineByName(target);
            if (rewardDiscipline != null) {
                rewardDisciplineId = rewardDiscipline.getRewardDisciplineId();

                Employee_RewardDiscipline employeeRewardDiscipline = new Employee_RewardDiscipline(
                        employee.getEmployeeId(), rewardDisciplineId, Configuration.STRING_TODAY, (float) amountInt);

                AppDatabase.getInstance(getContext()).employeeRewardDisciplineDao().insert(employeeRewardDiscipline);

                Toast.makeText(getContext(), "Lưu khen thưởng/kỷ luật thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Không tìm thấy thông tin khen thưởng/kỷ luật", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Đã xảy ra lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private RewardDiscipline getRewardDisciplineByName(String target) {
        List<RewardDiscipline> rewardDisciplineList = AppDatabase.getInstance(getContext()).rewardDisciplineDao().getRewardDisciplineByName(target);
        if (rewardDisciplineList != null && !rewardDisciplineList.isEmpty()) {
            return rewardDisciplineList.get(0);  // Assuming there's only one match
        }
        return null;
    }

    private void setupSpinnerType() {
        String[] items = new String[]{"Kỷ luật", "Khen thưởng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spiner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setupSpinnerGetRewardDiscipline(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setupSpinnerGetRewardDiscipline(int type) {
        List<String> data = new ArrayList<>();
        List<RewardDiscipline> rewardDisciplineList;

        if (type == 1) {  // Reward
            rewardDisciplineList = AppDatabase.getInstance(getContext()).rewardDisciplineDao().getRewardDisciplineByType(1);
        } else {  // Discipline
            rewardDisciplineList = AppDatabase.getInstance(getContext()).rewardDisciplineDao().getRewardDisciplineByType(0);
        }

        if (rewardDisciplineList != null && !rewardDisciplineList.isEmpty()) {
            for (RewardDiscipline discipline : rewardDisciplineList) {
                data.add(discipline.getRewardDisciplineName());
            }
        } else {
            data.add("Không tồn tại dữ liệu");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spiner, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarget.setAdapter(adapter);
    }
}
