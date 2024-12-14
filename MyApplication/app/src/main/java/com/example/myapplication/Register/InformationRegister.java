package com.example.myapplication.Register;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.Configuration;
import com.example.myapplication.Login.GiaoDienLogin;
import com.example.myapplication.MainApp.EmployeeRequest.EmployeeRequestActivity;
import com.example.myapplication.MainApp.ShowSpinner;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.dao.EmployeeDAO;
import com.example.myapplication.database.entities.EducationLevel;
import com.example.myapplication.database.entities.Employee;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InformationRegister extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_READ_STORAGE = 101;
    private static final int REQUEST_PERMISSION_READ_MEDIA_IMAGES = 102;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner spinnerGender;
    private EditText edtFullName;
    private EditText edtBirth;
    private EditText edtCCCD;
    private EditText edtAddress;
    private EditText edtPhoneNumber;
    private EditText edtEmail;
    private Button btnConfirm;
    private Button btnBack;
    private ImageView imageUpload;
    private Button btnUploadImage;
    private Uri selectedImageUri;
    private ProgressBar progressBar;
    private EditText edtEducationLevel;
    private String message;
    private EducationLevel education = new EducationLevel(null, null, null);
    private Employee employeeUpdate;
    private RelativeLayout relativeLayout;
    private View subLayout;
    private Spinner spinnerDepartment;
    private Spinner spinnerPosition;
    private Spinner spinnerWorkplace;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_register);

        message = getIntent().getStringExtra("message");
        if(message==null) message="";

        initUI();

        ShowSpinner.setupSpinnerGender(spinnerGender, this);

        // Nếu admin thêm nhân viên mới hoặc chỉnh sửa thông tin, sẽ bao gồm phân công công việc
        if(message.equals("AdminUpdate") || message.equals("AdminCreate")) {
            inflateLayout();

            // binding view in sub layout
            spinnerDepartment = subLayout.findViewById(R.id.spinner_department);
            spinnerPosition = subLayout.findViewById(R.id.spinner_position);
            spinnerWorkplace = subLayout.findViewById(R.id.spinner_workplace);

            // load dữ liệu lên spinner
            ShowSpinner.setupSpinnerDepartment(spinnerDepartment, InformationRegister.this);
            ShowSpinner.setupSpinnerPosition(spinnerPosition, InformationRegister.this);
            ShowSpinner.setupSpinnerWorkplace(spinnerWorkplace, InformationRegister.this);

            if(message.equals("AdminUpdate")) {
                employeeUpdate = (Employee) getIntent().getSerializableExtra("employeeKey");
                btnConfirm.setText("Chỉnh Sửa");
                loadInfoData(spinnerDepartment, spinnerPosition, spinnerWorkplace);
            }
        }

        edtBirth.setOnClickListener(v -> showDatePickerDialog());

        edtEducationLevel.setOnClickListener(view -> {
            showEducationLevelDialog(education);
        });

        btnConfirm.setOnClickListener(view -> Confirm());

        btnBack.setOnClickListener(view -> finish());

        // Btn check permission
        btnUploadImage.setOnClickListener(view -> checkAndRequestPermission());
    }


    // Đối với API 33 trở lên ( Android 13 ), sử dụng quyền READ_MEDIA_IMAGES thay vì quyền READ_EXTERNAL_STORAGE
    private void checkAndRequestPermission() {
        // Check nếu API 33+ ( TIRAMISU )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Nếu chưa được cấp quyền READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_PERMISSION_READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        } else {
            // Nếu nếu API thấp hơn 33 và chưa được cấp quyền
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        }
    }


    // Hàm nhận kết quả sau khi yêu cầu permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_STORAGE) {
            // Nếu được chấp nhận -> mở thư viện ảnh
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Không thể truy cập vào thư viện", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mở thư viện ảnh nếu được cấp quyền
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Hàm nhận kết quả sau khi chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check requset code, result code và data trả về
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Lấy dữ liệu ảnh
            selectedImageUri = data.getData();

            // Hiển thị ảnh trước khi upload lên cloud
            imageUpload.setImageURI(selectedImageUri);
        }
    }

    private void uploadToCloudinary(Uri imageUri, CloudinaryCallback callback) {
        // Sau khi chọn ảnh, kết quả trả về là uri, tuy nhiên kh thể trực tiếp đưa lên cloud
        // Phải chuyển uri thành đường dẫn thực --> upload lên cloud để nhận về đường dẫn online

        // Hiển thị progress bar khi đang xử lý upload ảnh
        progressBar.setVisibility(View.VISIBLE);
        try {
            // Tạo stream
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageData = IOUtils.toByteArray(inputStream);

            // upload ảnh
            MediaManager.get().upload(imageData)
                    .unsigned("unsigned_preset") // Chỉ định preset unsigned ( kh cần authen )
                    .option("folder", "AndroidApp") // Chỉ định folder upload
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) { }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) { }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            progressBar.setVisibility(View.GONE);
                            // lấy về đường dẫn online
                            String imageUrl = resultData.get("secure_url").toString();
                            callback.onSuccess(imageUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            progressBar.setVisibility(View.GONE);
                            callback.onError(error.getDescription());
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }).dispatch();
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
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
        int gender = spinnerGender.getSelectedItemPosition(); // Male = 0, Female = 1, Other = 2
        String strBirth = edtBirth.getText().toString().trim();
        String strCCCD = edtCCCD.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        String strNumberPhone = edtPhoneNumber.getText().toString().trim();
        String strEmail = edtEmail.getText().toString().trim();

        if (!checkValidData(strFullName, strBirth, strCCCD, strNumberPhone, strEmail, edtFullName, edtBirth, edtCCCD, edtPhoneNumber, edtEmail)) {
            return;
        }

        if(education.getEducationLevelName() == null) {
            edtEducationLevel.setError("");
            Toast.makeText(InformationRegister.this, "Vui lòng nhập thông tin trình độ học vấn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tạo nhân viên mới, check unique và ảnh đại diện kh đc trống
        if(!message.equals("AdminUpdate")) {
            if (!checkUnique(strCCCD, strNumberPhone, edtCCCD, edtPhoneNumber)) {
                return;
            }

            if(selectedImageUri == null) {
                Toast.makeText(InformationRegister.this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if(selectedImageUri == null) {
                updateEmployeeInfo(strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail);

                AppDatabase.getInstance(this).employeeDao().update(employeeUpdate);

                // Trả về trạng thái kết quả thành công -> để load lại data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("resultKey", "success");
                setResult(RESULT_OK, resultIntent);

                finish();
                return;
            }
        }

        // Upload to Cloudinary
        uploadToCloudinary(selectedImageUri, new CloudinaryCallback() {
            @Override
            public void onSuccess(String imgPath) {
                try {
                    if(message.equals("AdminUpdate")) {
                        updateEmployeeInfo(strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail);
                        employeeUpdate.setImagePath(imgPath);

                        AppDatabase.getInstance(InformationRegister.this).employeeDao().update(employeeUpdate);

                        // Trả về trạng thái kết quả thành công -> để load lại data
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("resultKey", "success");
                        setResult(RESULT_OK, resultIntent);

                        finish();
                        return;
                    }

                    // Add education level
                    int eduID = (int)AppDatabase.getInstance(InformationRegister.this).educationLevelDao().insertReturnId(education);
                    education.setEducationId(eduID);

                    // Add employee
                    Employee employee = new Employee(strFullName, gender, strBirth, strCCCD, strAddress, strNumberPhone, strEmail,
                            true, false, imgPath, null, null, null, education.getEducationId(), null, null);

                    // Nếu admin tạo nhân viên trực tiếp hoặc chỉnh sửa nhân viên
                    if(message.equals("AdminCreate")) {
                        int departmentId = spinnerDepartment.getSelectedItemPosition();
                        int positionId = spinnerPosition.getSelectedItemPosition();
                        int workplaceId = spinnerWorkplace.getSelectedItemPosition();

                        employee.setApprove(true);
                        employee.setDepartmentId( departmentId == 0 ? null : departmentId );
                        employee.setPositionId( positionId == 0 ? null : positionId );
                        employee.setWorkplaceId( workplaceId == 0 ? null : workplaceId );

                        AppDatabase.getInstance(InformationRegister.this).employeeDao().insert(employee);

                        // Trả về trạng thái kết quả thành công -> để load lại data
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("resultKey", "success");
                        setResult(RESULT_OK, resultIntent);

                        finish();
                    } else {
                        // Lưu vào db
                        int employeeId = (int)AppDatabase.getInstance(InformationRegister.this).employeeDao().insertReturnId(employee);
                        employee.setEmployeeId(employeeId);

                        // Trả về form Register đối tượng employee
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("employee_key", employee);
                        setResult(RESULT_OK, resultIntent);

                        showRegistrationSuccessDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(InformationRegister.this, "Lỗi khi lưu dữ liệu:", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String err) {
                Toast.makeText(InformationRegister.this, "Lỗi khi tải ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmployeeInfo(String strFullName, int gender, String strBirth, String strCCCD, String strAddress, String strNumberPhone, String strEmail) {
        int departmentId = spinnerDepartment.getSelectedItemPosition();
        int positionId = spinnerPosition.getSelectedItemPosition();
        int workplaceId = spinnerWorkplace.getSelectedItemPosition();

        employeeUpdate.setDepartmentId( departmentId == 0 ? null : departmentId );
        employeeUpdate.setPositionId( positionId == 0 ? null : positionId );
        employeeUpdate.setWorkplaceId( workplaceId == 0 ? null : workplaceId );

        employeeUpdate.setFullName(strFullName);
        employeeUpdate.setGender(gender);
        employeeUpdate.setBirth(strBirth);
        employeeUpdate.setIdentityNumber(strCCCD);
        employeeUpdate.setAddress(strAddress);
        employeeUpdate.setPhoneNumber(strNumberPhone);
        employeeUpdate.setEmail(strEmail);

        employeeUpdate.setEducationId(education.getEducationId());
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



        return true;
    }

    private boolean checkUnique(String strCCCD, String strNumberPhone, EditText editCCCD, EditText editNumberPhone) {
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

        return true;
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

    private void inflateLayout() {
        relativeLayout = findViewById(R.id.relative_layout);

        // Inflate layout phụ chứa spinner phân công
        LayoutInflater inflater = LayoutInflater.from(this);
        subLayout = inflater.inflate(R.layout.sub_layout_spinner, relativeLayout, false);

        // Gán ID cho subLayout nếu chưa có
        if (subLayout.getId() == View.NO_ID) {
            subLayout.setId(View.generateViewId());
        }

        // Tạo LayoutParams cho subLayout, chỉ định vị trí bên dưới input email
        RelativeLayout.LayoutParams subLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        subLayoutParams.addRule(RelativeLayout.BELOW, R.id.input_email);
        subLayout.setLayoutParams(subLayoutParams);

        // Thêm subLayout vào RelativeLayout
        relativeLayout.addView(subLayout);

        // Chỉnh lại vị trí button confirm, bên dưới spinner vừa thêm ( dưới cùng )
        RelativeLayout.LayoutParams buttonParams = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
        buttonParams.addRule(RelativeLayout.BELOW, subLayout.getId());
        btnConfirm.setLayoutParams(buttonParams);
    }

    private void loadInfoData(Spinner spinnerDepartment, Spinner spinnerPosition, Spinner spinnerWorkplace) {
        if(employeeUpdate != null) {
            edtFullName.setText(employeeUpdate.getFullName());
            edtBirth.setText(employeeUpdate.getBirth());
            edtCCCD.setText(employeeUpdate.getIdentityNumber());
            edtAddress.setText(employeeUpdate.getAddress());
            edtPhoneNumber.setText(employeeUpdate.getPhoneNumber());
            edtEmail.setText(employeeUpdate.getEmail());

            spinnerGender.setSelection(employeeUpdate.getGender());

            if(employeeUpdate.getDepartmentId()!=null) {
                spinnerDepartment.setSelection(employeeUpdate.getDepartmentId());
            }

            if(employeeUpdate.getPositionId()!=null) {
                spinnerPosition.setSelection(employeeUpdate.getPositionId());
            }

            if(employeeUpdate.getWorkplaceId()!=null) {
                spinnerWorkplace.setSelection(employeeUpdate.getWorkplaceId());
            }

            // Hiển thị ảnh avatar
            Glide.with(this).load(employeeUpdate.getImagePath()).circleCrop().into(imageUpload);

            // Trình độ học vấn
            if(employeeUpdate.getEducationId()!=null) {
                education = AppDatabase.getInstance(this).educationLevelDao().getById(employeeUpdate.getEducationId());
                edtEducationLevel.setText(education.getEducationLevelName());
            }
        }
    }

    private void showRegistrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng ký thành viên thành công!")
                .setMessage("Cảm ơn bạn! Vui lòng chờ đợi phê duyệt.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
//                        backToLogin();
                    }
                })
                .setCancelable(false); // Không cho phép người dùng nhấn ngoài để đóng dialog

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEducationLevelDialog(EducationLevel education) {
        Dialog dialog = new Dialog(this);
        Configuration.showDialog(dialog, R.layout.dialog_education_level_layout);

        // Ánh xạ view trong dialog
        Button btnCancel = dialog.findViewById(R.id.btn_education_cancel);
        Button btnConfirm = dialog.findViewById(R.id.btn_education_confirm);
        EditText edtMajor = dialog.findViewById(R.id.edt_education_major);
        EditText edtInstitute = dialog.findViewById(R.id.edt_education_institute);
        Spinner spinnerEducationLevel = dialog.findViewById(R.id.spinner_education_name);

        // Setup spinner hiển thị tên cấp bậc trình độ học vấn
        List<String> listEduName = new ArrayList<>(Arrays.asList(new String[]{"Chưa tốt nghiệp", "Cao đẳng", "Đại học", "Cao học", "khác"}));
        ArrayAdapter eduAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listEduName);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducationLevel.setAdapter(eduAdapter);

        if(education.getEducationLevelName()!=null) {
            int pos = listEduName.indexOf(education.getEducationLevelName());
            spinnerEducationLevel.setSelection(pos);
            if(education.getMajor()!=null) {
                edtMajor.setText(education.getMajor());
            }
            if(education.getInstitute()!=null) {
                edtInstitute.setText(education.getInstitute());
            }
        }

        // btn huỷ trong dialog
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        // btn xác nhận trong dialog
        btnConfirm.setOnClickListener(view -> {
            String major  = edtMajor.getText().toString().trim();
            String institute = edtInstitute.getText().toString().trim();
            String levelName = spinnerEducationLevel.getSelectedItem().toString();

            education.setMajor(major);
            education.setInstitute(institute);
            education.setEducationLevelName(levelName);

            // hiển thị ra edittext
            edtEducationLevel.setText(levelName);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void backToLogin() {
        Intent intent = new Intent(this, GiaoDienLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp hoạt động
        startActivity(intent);
        finish();
    }

    private void initUI() {
        edtFullName = findViewById(R.id.edt_fullName);
        spinnerGender = findViewById(R.id.spinner_gender);
        edtBirth = findViewById(R.id.edt_birth);
        edtCCCD = findViewById(R.id.edt_cccd);
        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_phoneNumber);
        edtEmail = findViewById(R.id.edt_email);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnBack = findViewById(R.id.btn_back);
        btnUploadImage = findViewById(R.id.btn_choose_image);
        imageUpload = findViewById(R.id.img_imageUser);
        progressBar = findViewById(R.id.progress_bar);
        edtEducationLevel =  findViewById(R.id.edt_education_level);
    }
}