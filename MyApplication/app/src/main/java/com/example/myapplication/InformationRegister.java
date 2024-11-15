package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.dao.EmployeeDAO;
import com.example.myapplication.database.entities.Employee;

import java.time.LocalDate;
import java.util.Calendar;

public class InformationRegister extends AppCompatActivity {
    private EditText editTextDate;
    private Spinner spinner;
    private EditText editFullName;
    private Spinner spinnerGender;
    private EditText editBirth;
    private EditText editCCCD;
    private EditText editAddress;
    private EditText editNumberPhone;
    private EditText editEmail;
    private Button btConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initUI();

        setupSpinner();

        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm();
            }
        });
    }

    private void setupSpinner() {
        String[] items = new String[]{"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showDatePickerDialog() {
        // Get today
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(InformationRegister.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Cập nhật ngày đã chọn vào EditText
                        editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void Confirm() {

        String strFullName = editFullName.getText().toString().trim();
        int gender = spinner.getSelectedItemPosition(); // Male = 0, Female = 1, Other = 2
        String strBirth = editBirth.getText().toString().trim();
        String strCCCD = editCCCD.getText().toString().trim();
        String strAddress = editAddress.getText().toString().trim();
        String strNumberPhone = editNumberPhone.getText().toString().trim();
        String strEmail = editEmail.getText().toString();

        String createDate = LocalDate.now().format(Configuration.FORMATTER);

        // Check validate input fields
        if (!checkValidData(strFullName, strBirth, strCCCD, strNumberPhone, strEmail,
                editFullName, editBirth, editCCCD, editNumberPhone, editEmail)) {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add employee
        Employee employee = new Employee(strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail
                , 0, null, null, null, null, null, null, null);
        AppDatabase.getInstance(this).employeeDao().insert(employee);

        // Show success message
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidData(String strFullName, String strBirth, String strCCCD
            , String strNumberPhone, String strEmail
            , EditText editFullName, EditText editBirth, EditText editCCCD
            , EditText editNumberPhone, EditText editEmail) {

        if (TextUtils.isEmpty(strFullName)) {
            editFullName.setError("Vui lòng nhập tên!");
            editFullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(strBirth)) {
            editBirth.setError("Vui lòng nhập ngày sinh");
            editBirth.requestFocus();
            return false;
        }

        if (strCCCD.length() != 12 || !strCCCD.matches("\\d+")) {
            editCCCD.setError("Vui lòng nhập chính xác CCCD 12 chữ số");
            editCCCD.requestFocus();
            return false;
        }

        if (strNumberPhone.length() != 10 || !strNumberPhone.matches("\\d+") || strNumberPhone.charAt(0) != '0') {
            editNumberPhone.setError("Vui lòng nhập chính xác 10 chữ số điện thoại");
            editNumberPhone.requestFocus();
            return false;
        }

        String emailPattern = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";// Kiểm tra ký tự của chuỗi email
        if (TextUtils.isEmpty(strEmail) || !strEmail.matches(emailPattern)) {
            editEmail.setError("Vui lòng nhập chính xác email!");
            editEmail.requestFocus();
            return false;
        }

        if (checkEmployeeIdentityNumberExists(strCCCD)) {
            editCCCD.setError("Số CCCD đã có người sử dụng!");
            editCCCD.requestFocus();
            return false;
        }

        if (checkEmployeePhoneNumberExists(strNumberPhone)) {
            editNumberPhone.setError("Số điện thoại đã có người sử dụng!");
            editNumberPhone.requestFocus();
            return false;
        }

        return true;// Valid input data
    }

    // Check duplicate information from database
    private boolean checkEmployeePhoneNumberExists(String phoneNumber) {
        EmployeeDAO employeeDAO = AppDatabase.getInstance(this).employeeDao();
        Employee employee = employeeDAO.getByPhoneNumber(phoneNumber);
        return employee != null;
    }

    private boolean checkEmployeeIdentityNumberExists(String identityNumber) {
        EmployeeDAO employeeDAO = AppDatabase.getInstance(this).employeeDao();
        Employee employee = employeeDAO.getByIdentityNumber(identityNumber);
        return employee != null;
    }

    private void initUI() {
        spinner = findViewById(R.id.spinner_gender);
        editTextDate = findViewById(R.id.edit_textDate);
        editFullName = findViewById(R.id.edit_hoten);
        spinner = findViewById(R.id.spinner_gender);
        editBirth = findViewById(R.id.edit_textDate);
        editCCCD = findViewById(R.id.edit_cccd);
        editAddress = findViewById(R.id.edit_address);
        editNumberPhone = findViewById(R.id.edit_numberPhone);
        editEmail = findViewById(R.id.edit_email);
        btConfirm = findViewById(R.id.bt_confirm);
    }
}
