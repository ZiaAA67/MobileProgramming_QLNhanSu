package com.example.myapplication.MainApp.Timekeeping;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Employee_Session;
import com.example.myapplication.database.entities.Session;
import com.example.myapplication.database.entities.Shift;
import com.example.myapplication.database.entities.Timekeeping;
import com.example.myapplication.database.entities.User;
import com.example.myapplication.database.entities.Workplace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewTimekeeping extends AppCompatActivity implements OnMapReadyCallback {

    private int userId;
    private int employeeId;

    private Button btnBack;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private String currentMode = "In";
    private TextView tvWorkplace;

    private Timekeeping timekeeping;
    private static Integer DEFAULT_SHIFT = 1;// Ca hành chính

    LocalDate today = LocalDate.now();
    int day = today.getDayOfMonth();
    int month = today.getMonthValue();
    int year = today.getYear();
    private Spinner spinnerShift;

    private static final float RADIUS = 100; // Khoảng cách cho phép ( 100m )
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap ggMap; // Đối tượng google map
    private Marker marker; // Con trỏ vị trí
    private LatLng targetLocation; // Vị trí cơ sở làm việc
    private FusedLocationProviderClient fusedLocationClient; // Vị trí ng dùng
    private Workplace wp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);

        initUI();
        setupSpinnerShift();

        // Khởi tạo bản đồ, nếu thành công sẽ gọi hàm onMapReady()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        userId = getIntent().getIntExtra("UserID", -1);
        employeeId = getEmployeeId(userId);

        showWorkplace();

        timekeeping = new Timekeeping();

        // Khôi phục trạng thái Timekeeping từ SharedPreferences
        restoreTimekeepingState();

        // Xử lý sự kiện chấm công
        btnCheckIn.setOnClickListener(v -> checkIn());
        btnCheckOut.setOnClickListener(v -> checkOut());
        btnBack.setOnClickListener(v -> finish());

        // Lấy vị trí hiện tại của người dùng
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void createSessionIfNeeded() {
        try {
            // Kiểm tra nếu đã có session cho ngày hôm nay
            Session existingSession = getSessionForToday(day, month, year);
            int sessionId;

            if (existingSession == null) {
                // Tạo session mới nếu không có
                Session session = new Session(day, month, year, false, DEFAULT_SHIFT); // shiftId = 1 (ca hành chính)
                long insertedId = AppDatabase.getInstance(this).sessionDao().insert(session);

                if (insertedId == -1) {
                    Toast.makeText(this, "Không thể tạo session mới!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Cập nhật sessionId từ kết quả chèn
                sessionId = (int) insertedId;
            } else {
                // Nếu đã có session thì sử dụng sessionId của session hiện tại
                sessionId = existingSession.getSessionId();
            }

            Log.d("DEBUG", "employeeId: " + employeeId + ", sessionId: " + sessionId);

            // Kiểm tra nếu Employee_Session đã tồn tại
            List<Integer> existingEmployeeSessions = AppDatabase.getInstance(this)
                    .employeeSessionDao()
                    .getSessionIdsByEmployeeId(employeeId);

            if (existingEmployeeSessions.contains(sessionId)) {
                return;
            }

            // Tạo Employee_Session liên kết n-n nhân viên và session
            Employee_Session employeeSession = new Employee_Session(employeeId, sessionId);
            AppDatabase.getInstance(this).employeeSessionDao().insert(employeeSession);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tạo mới: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private Session getSessionForToday(int day, int month, int year) {
        Session sessionsForDate = AppDatabase.getInstance(this)
                .sessionDao()
                .getSessionByDayMonthYear(day, month, year);
        return sessionsForDate != null ? sessionsForDate : null;
    }

    private void saveTimekeepingState() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentMode", currentMode); // Lưu trạng thái hiện tại (In hoặc Checkout)
        editor.putString("timeIn", timekeeping.getTimeIn()); // Lưu thời gian chấm công vào
        editor.putString("timeOut", timekeeping.getTimeOut()); // Lưu thời gian chấm công ra
        editor.apply();
    }

    private void restoreTimekeepingState() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
        currentMode = sharedPreferences.getString("currentMode", "In");
        String timeIn = sharedPreferences.getString("timeIn", null);
        String timeOut = sharedPreferences.getString("timeOut", null);

        timekeeping = new Timekeeping();
        timekeeping.setTimeIn(timeIn);
        timekeeping.setTimeOut(timeOut);

        updateUI();
    }

    private void updateUI() {
        if (currentMode.equals("In")) {
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.GONE);
        } else {
            btnCheckIn.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkExistingTimekeeping(int employeeId, int sessionid, int shiftid) {

        List<Timekeeping> timekeepings = AppDatabase.getInstance(this)
                .timekeepingDao()
                .getTimekeepingBySessionId(sessionid);


        for (Timekeeping timekeeping : timekeepings) {
            Session session = AppDatabase.getInstance(this)
                    .sessionDao()
                    .getSessionById(timekeeping.getSessionId());
            if (session.getShiftId() == shiftid && timekeeping.getEmployeeId() == employeeId)
                return false;
        }

        return true;
    }

    private void checkIn() {
        checkUserLocation(isWithinRadius -> {
            if (isWithinRadius) {
                createSessionIfNeeded();

                Session session = AppDatabase.getInstance(this).sessionDao().getSessionByDayMonthYear(day, month, year);

                boolean isNewTimekeeping = checkExistingTimekeeping(
                        employeeId, session.getSessionId(), DEFAULT_SHIFT);

                if (!isNewTimekeeping) {
                    Toast.makeText(this, "Ca này hôm nay đã chấm công rồi mà!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ;
                if (currentMode.equals("In")) {
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                    timekeeping.setTimeIn(now);
                    makeToast("Chấm công vào thành công lúc ");
                    currentMode = "Checkout";

                    saveTimekeepingState();
                    updateUI();
                }
            } else {
                Toast.makeText(this, "Bạn đang không ở vị trí cho phép!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void checkOut() {
        if (currentMode.equals("Checkout")) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            timekeeping.setTimeOut(now); // Set thời gian chấm công ra

            // Kiểm tra và thiết lập sessionId nếu chưa có
            if (timekeeping.getSessionId() == 0) {
                Session session = getSessionForToday(LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear());
                if (session != null) {
                    timekeeping.setSessionId(session.getSessionId());
                } else {
                    Toast.makeText(this, "Không tìm thấy session cho ngày hôm nay", Toast.LENGTH_SHORT).show();

                    // Xóa data nếu ra không được
                    SharedPreferences sharedPreferences = getSharedPreferences("TimekeepingPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    return;
                }
            }

            try {
                // Lưu thời gian chấm công vào cơ sở dữ liệu
                timekeeping.setIsAbsent(0);
                timekeeping.setOvertime(calculateOvertime());
                timekeeping.setEmployeeId(employeeId);
                AppDatabase.getInstance(this).timekeepingDao().insert(timekeeping);
                makeToast("Chấm công ra thành công lúc ");
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            // Reset lại chế độ
            currentMode = "In";

            saveTimekeepingState();
            updateUI();
        }
    }

    private int calculateOvertime() {
        try {
            String timeOut = timekeeping.getTimeOut();

            if (timeOut == null) {
                Toast.makeText(this, "Chưa có dữ liệu thời gian chấm công ra!", Toast.LENGTH_SHORT).show();
                return 0;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime timeOutParsed = LocalTime.parse(timeOut, formatter);

            Shift shift = AppDatabase.getInstance(this).shiftDao().getShiftById(DEFAULT_SHIFT);
            LocalTime endOfShift = LocalTime.parse(shift.getTimeEnd());

            // Ra sớm
            if (timeOutParsed.isBefore(endOfShift)) {
                return 0;
            }

            // Ra trễ
            long overtime = Duration.between(endOfShift, timeOutParsed).toMinutes();

            return (int) overtime;
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tính overtime: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return 0;
        }
    }

    private void makeToast(String toast) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Toast.makeText(this, toast + now, Toast.LENGTH_SHORT).show();
    }

    private void showWorkplace() {
        Employee employee = AppDatabase.getInstance(this).employeeDao().getById(employeeId);
        if (employee.getWorkplaceId() != null) {
            Workplace workplace = AppDatabase.getInstance(this).workplaceDao().getWorkplaceById(employee.getWorkplaceId());
            tvWorkplace.setText(String.format("Cơ sở %s - %s", workplace.getWorkplaceName(), workplace.getAddress()));
            wp = workplace;
        } else
            tvWorkplace.setText("Không có");
    }

    private int getEmployeeId(int userId) {
        Employee employee = null;
        try {
            employee = AppDatabase.getInstance(this).employeeDao().getEmployeeByUserId(userId);

            if (employee == null) {
                Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
                finish();
                return -1;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
            return -1;
        }

        if (!employee.isApprove()) {
            Toast.makeText(this, "Nhân viên chưa được châp nhận!", Toast.LENGTH_SHORT).show();
            finish();
        }

        return employee.getEmployeeId();
    }

    private void setupSpinnerShift() {
        List<String> data = new ArrayList<>();
        List<Shift> shifts = AppDatabase.getInstance(this).shiftDao().getAllShifts();

        if (shifts != null && !shifts.isEmpty()) {
            for (Shift shift : shifts) {
                data.add(shift.getShiftType());
            }
        } else {
            data.add("Không tồn tại dữ liệu");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spiner, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShift.setAdapter(adapter);

        spinnerShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateShiftFromSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateShiftFromSpinner() {
        if (spinnerShift != null && spinnerShift.getSelectedItem() != null) {
            String selectedShiftType = spinnerShift.getSelectedItem().toString();

            Shift selectedShift = AppDatabase.getInstance(this).shiftDao().getShiftByType(selectedShiftType);

            if (selectedShift != null) {
                DEFAULT_SHIFT = selectedShift.getShiftId();
                Toast.makeText(this, "Đã chọn ca: " + selectedShiftType, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ca làm việc không tồn tại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn ca làm việc!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back);
        btnCheckIn = findViewById(R.id.btn_checkin);
        btnCheckOut = findViewById(R.id.btn_checkout);
        tvWorkplace = findViewById(R.id.tv_workplace);
        spinnerShift = findViewById(R.id.spinner_shift);
    }

    // callback để trả về kết quả vị trí
    public interface LocationCheckCallback {
        void onResult(boolean isWithinRadius);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Lưu đối tượng gg map được trả về sau khi load map thành công
        ggMap = googleMap;

        if (wp != null) {
            targetLocation = new LatLng(wp.getLatitude(), wp.getLongitude());
            if (marker != null) {
                marker.remove();
            }
            marker = ggMap.addMarker(new MarkerOptions().position(targetLocation).title("Cơ sở" + wp.getWorkplaceName()));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            });
        }

        // Yêu cầu quyền truy cập vị trí của ng dùng nếu chưa có
        enableUserLocation();
    }

    // Hàm yêu cầu quyền truy cập vị trí
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Hiển thị vị trí ng dùng nếu đã có quyền truy cập
            ggMap.setMyLocationEnabled(true);
        }
    }

    // Hàm nhận kết quả khi yêu cầu quyền truy cập
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm ktra vị trí
    private void checkUserLocation(LocationCheckCallback callback) {
        if (targetLocation == null) {
            callback.onResult(false);
            return;
        }

        // Ktra lại quyền truy cập
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Lấy vị trí hiện tại ( vị trí cuối cùng được trả về )
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    float[] distance = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), targetLocation.latitude, targetLocation.longitude, distance);

                    boolean isWithinRadius = distance[0] <= RADIUS;
                    callback.onResult(isWithinRadius);
                } else {
                    callback.onResult(false);
                }
            });
        } else {
            callback.onResult(false);
        }
    }
}