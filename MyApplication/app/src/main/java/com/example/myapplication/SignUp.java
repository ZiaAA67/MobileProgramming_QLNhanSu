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
import com.example.myapplication.database.dao.UserDAO;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private EditText editTextDate;
    private Spinner spinner;
    private EditText editUser;
    private EditText editPassword;
    private EditText editPasswordAgain;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this,
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
        String strUserName = editUser.getText().toString().trim();
        String strPassword = editPassword.getText().toString().trim();
        String strPasswordAgain = editPasswordAgain.getText().toString().trim();
        String strFullName = editFullName.getText().toString().trim();
        int gender = spinner.getSelectedItemPosition(); // Male = 0, Female = 1, Other = 2
        String strBirth = editBirth.getText().toString().trim();
        String strCCCD = editCCCD.getText().toString().trim();
        String strAddress = editAddress.getText().toString().trim();
        String strNumberPhone = editNumberPhone.getText().toString().trim();
        String strEmail = editEmail.getText().toString();

        String createDate = LocalDate.now().format(Configuration.FORMATTER);

        // Check validate input fields
        if (!checkValidData(strUserName, strPassword, strPasswordAgain, strFullName, strBirth, strCCCD, strNumberPhone, strEmail)) {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        // If validation passes, proceed with inserting user and employee

        // Add user
        User user = new User(strUserName, md5(strPassword), createDate, null);
        AppDatabase.getInstance(this).userDao().insert(user);
        long userId = AppDatabase.getInstance(this).userDao().insert(user);

        // Add employee
        Employee employee = new Employee(
                strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail,
                1, null, null, null, null, null, (int) userId);
        AppDatabase.getInstance(this).employeeDao().insert(employee);

        // Show success message
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
    }


    private boolean checkValidData(String strUserName, String strPassword, String strPassWordAgain,
                          String strFullName, String strBirth, String strCCCD, String strNumberPhone, String strEmail) {

        if (TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strPassword)) {
            Toast.makeText(this, "Vui lòng nhập tên và mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!strPassword.equals(strPassWordAgain) || TextUtils.isEmpty(strPassWordAgain)) {
            Toast.makeText(this, "Vui lòng kiểm tra mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(strFullName)) {
            Toast.makeText(this, "Nhập đầy đủ tên", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra CCCD (đúng 12 ký tự và toàn bộ là số)
        if (strCCCD.length() != 12 || !strCCCD.matches("\\d+")) {
            Toast.makeText(this, "Nhập chính xác số CCCD đủ 12 chữ số", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra số điện thoại (đúng 10 ký tự và toàn bộ là số)
        if (strNumberPhone.length() != 10 || !strNumberPhone.matches("\\d+") || strNumberPhone.charAt(0) != '0') {
            Toast.makeText(this, "Nhập chính xác số điện thoại đủ 10 chữ số bắt đầu bằng số 0", Toast.LENGTH_SHORT).show();
            return false;
        }

        String emailPattern = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";// Kiểm tra ký tự của chuỗi email
        if (TextUtils.isEmpty(strEmail) || !strEmail.matches(emailPattern)) {
            Toast.makeText(this, "Nhập địa chỉ email hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkUserNameExists(strUserName)) {
            Toast.makeText(this, "Tên người dùng đã tồn tại. Vui lòng chọn tên khác.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkEmployeeIdentityNumberExists(strCCCD)) {
            Toast.makeText(this, "Số căn cước công dân này đã có người dùng", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkEmployeePhoneNumberExists(strNumberPhone)) {
            Toast.makeText(this, "Số điện thoại này đã có người sử dụng", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;// Valid input data
    }


    // Kiểm tra trùng lặp dữ liệu
    private boolean checkUserNameExists(String userName) {
        UserDAO userDAO = AppDatabase.getInstance(this).userDao();
        User user = userDAO.getUserByUsername(userName);
        return user != null;
    }

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


    // MD5 hashed
    public static String md5(String input) {
        try {
            // Lấy đối tượng MessageDigest với thuật toán MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Băm chuỗi đầu vào và trả về mảng byte
            byte[] bytes = digest.digest(input.getBytes());

            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                // Chuyển đổi từng byte thành chuỗi hex và thêm vào kết quả
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Trả về chuỗi hex
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Nếu thuật toán không tồn tại, trả về null
            e.printStackTrace();
            return null;
        }
    }

    private void initUI() {
        spinner = findViewById(R.id.spinner_gender);
        editTextDate = findViewById(R.id.edit_textDate);
        editUser = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        editPasswordAgain = findViewById(R.id.edit_passwordAgain);
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
