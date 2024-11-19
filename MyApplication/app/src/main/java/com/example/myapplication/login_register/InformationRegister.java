package com.example.myapplication.login_register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.dao.EmployeeDAO;
import com.example.myapplication.database.entities.Employee;

import java.util.Calendar;

public class InformationRegister extends AppCompatActivity {
    private Spinner spinner;
    private EditText edtFullName;
    private EditText edtBirth;
    private EditText edtCCCD;
    private EditText edtAddress;
    private EditText edtPhoneNumber;
    private EditText edtEmail;
    private Button btnConfirm;
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_register);

        initUI();

        setupSpinner();

        edtBirth.setOnClickListener(v -> showDatePickerDialog());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm();
            }
        });

        btnBack.setOnClickListener(view -> {
            backToLogin();
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
                        edtBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void Confirm() {
        String strFullName = edtFullName.getText().toString().trim();
        int gender = spinner.getSelectedItemPosition(); // Male = 0, Female = 1, Other = 2
        String strBirth = edtBirth.getText().toString().trim();
        String strCCCD = edtCCCD.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        String strNumberPhone = edtPhoneNumber.getText().toString().trim();
        String strEmail = edtEmail.getText().toString().trim();

        // Check validate input fields
        if (!checkValidData(strFullName, strBirth, strCCCD, strNumberPhone, strEmail,
                edtFullName, edtBirth, edtCCCD, edtPhoneNumber, edtEmail)) {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add employee
        Employee employee = new Employee(strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail
                , 0, null, null, null, null, null, null, null);
        try {
            AppDatabase.getInstance(this).employeeDao().insert(employee);
            Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
            showRegistrationSuccessDialog();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    private void backToLogin(){
        Intent intent = new Intent(this, GiaoDienLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp hoạt động
        startActivity(intent);
        finish();
    }

    private void showRegistrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng ký thành viên thành công!")
                .setMessage("Cảm ơn bạn! Vui lòng chờ đợi phê duyệt.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backToLogin();
                    }
                })
                .setCancelable(false); // Không cho phép người dùng nhấn ngoài để đóng dialog

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initUI() {
        edtFullName = findViewById(R.id.edt_fullName);
        spinner = findViewById(R.id.spinner_gender);
        edtBirth = findViewById(R.id.edt_birth);
        edtCCCD = findViewById(R.id.edt_cccd);
        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_phoneNumber);
        edtEmail = findViewById(R.id.edt_email);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnBack = findViewById(R.id.btn_back);
    }
}