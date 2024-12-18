package com.example.myapplication.MainApp.SalaryManager;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Salary;

import java.util.ArrayList;
import java.util.List;

public class DialogAddSalary extends Dialog {

    private Spinner spinnerType;
    private EditText edtBasicSalary, edtAllowance, edtCoefficient;
    private Button btnCancel, btnSave;
    private Employee employee;

    public DialogAddSalary(@NonNull Context context, Employee employee) {
        super(context);
        this.employee = employee;
        setContentView(R.layout.dialog_salary);
        initUI();
        setupSpinnerType();
        setupListeners();
    }

    private void initUI() {
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        edtBasicSalary = findViewById(R.id.edt_basic_salary);
        edtAllowance = findViewById(R.id.edt_allowance);
        edtCoefficient = findViewById(R.id.edt_coefficient);
        spinnerType = findViewById(R.id.spinner_salary);

        setInputFieldsEnabled(false, null);
    }

    private void setupSpinnerType() {
        ArrayList<String> items = new ArrayList<>();
        List<Salary> salaries = AppDatabase.getInstance(getContext()).salaryDao().getAllSalaries();
        final List<Salary> salaryList = new ArrayList<>(salaries);

        for (Salary salary : salaries) {
            items.add(String.format("%.2f", salary.getBasicSalary()));
        }

        items.add("Thêm mới!");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedItem = items.get(position);
                if ("Thêm mới!".equals(selectedItem)) {
                    setInputFieldsEnabled(true, null);
                } else {
                    Salary selectedSalary = salaryList.get(position);
                    setInputFieldsEnabled(false, selectedSalary);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void setupListeners() {
        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            try {
                String selectedType = (String) spinnerType.getSelectedItem();

                if (!"Thêm mới!".equals(selectedType)) {
                    int selectedPosition = spinnerType.getSelectedItemPosition();
                    Salary selectedSalary = AppDatabase.getInstance(getContext()).salaryDao().getAllSalaries().get(selectedPosition);

                    employee.setSalaryId(selectedSalary.getSalaryId());
                    AppDatabase.getInstance(getContext()).employeeDao().update(employee);

                    Toast.makeText(getContext(), "Cập nhật lương thành công!", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                float basicSalary = Float.parseFloat(edtBasicSalary.getText().toString());
                float allowance = Float.parseFloat(edtAllowance.getText().toString());
                float coefficient = Float.parseFloat(edtCoefficient.getText().toString());

                List<Salary> existingSalaries = AppDatabase.getInstance(getContext()).salaryDao().getAllSalaries();
                for (Salary salary : existingSalaries) {
                    if (salary.getBasicSalary() == basicSalary
                            && salary.getAllowance() == allowance
                            && salary.getCoefficient() == coefficient) {
                        Toast.makeText(getContext(), "Lương đã tồn tại, không thể thêm mới!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Salary newSalary = new Salary(basicSalary, allowance, coefficient);
                long salaryId = AppDatabase.getInstance(getContext()).salaryDao().insert(newSalary);

                employee.setSalaryId((int) salaryId);
                AppDatabase.getInstance(getContext()).employeeDao().update(employee);

                Toast.makeText(getContext(), "Thêm lương mới và cập nhật thành công!", Toast.LENGTH_SHORT).show();
                dismiss();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setInputFieldsEnabled(boolean enabled, @Nullable Salary selectedSalary) {
        edtBasicSalary.setEnabled(enabled);
        edtAllowance.setEnabled(enabled);
        edtCoefficient.setEnabled(enabled);

        if (enabled) {
            edtBasicSalary.setText("");
            edtAllowance.setText("");
            edtCoefficient.setText("");
            edtBasicSalary.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            edtAllowance.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            edtCoefficient.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (selectedSalary != null) {
            edtBasicSalary.setText(String.format("%.2f", selectedSalary.getBasicSalary()));
            edtAllowance.setText(String.format("%.2f", selectedSalary.getAllowance()));
            edtCoefficient.setText(String.format("%.2f", selectedSalary.getCoefficient()));
            edtBasicSalary.setInputType(InputType.TYPE_NULL);
            edtAllowance.setInputType(InputType.TYPE_NULL);
            edtCoefficient.setInputType(InputType.TYPE_NULL);
        } else {
            edtBasicSalary.setText("");
            edtAllowance.setText("");
            edtCoefficient.setText("");
            edtBasicSalary.setInputType(InputType.TYPE_NULL);
            edtAllowance.setInputType(InputType.TYPE_NULL);
            edtCoefficient.setInputType(InputType.TYPE_NULL);
        }
    }
}
