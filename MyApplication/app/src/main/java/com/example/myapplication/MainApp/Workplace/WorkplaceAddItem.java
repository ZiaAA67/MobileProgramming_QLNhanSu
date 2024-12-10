package com.example.myapplication.MainApp.Workplace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
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

import java.io.IOException;
import java.util.List;

public class WorkplaceAddItem extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap ggMap; // Đối tượng google map
    private Marker marker; // Con trỏ vị trí
    private LatLng targetLocation; // Toạ độ được người dùng chấm ( target )
    private String targetAddress;
    private FusedLocationProviderClient fusedLocationClient; // Biến lưu vị trí của người dùng
    private SearchView searchView;
    private EditText edtWorkplaceName;
    private EditText edtEdtWorkplaceAddress;
    private Button btnAdd;
    private Button btnBack;
    private Workplace workplaceUpdate;
    private String oldName, oldAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workplace_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        workplaceUpdate = (Workplace) getIntent().getSerializableExtra("workplaceKey");

        // Khởi tạo bản đồ, nếu thành công sẽ gọi hàm onMapReady()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Khởi tạo SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Gọi hàm xử lý địa chỉ khi người dùng nhấn tìm kiếm
                searchLocation(query);

                //clear forcus để tránh bị gọi 2 lần hàm search
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Lấy vị trí hiện tại của người dùng
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            String name = edtWorkplaceName.getText().toString().trim();
            if(name.isEmpty()) {
                edtWorkplaceName.setError("Vui lòng nhập thông tin!");
                edtWorkplaceName.requestFocus();
                return;
            }

            if(targetAddress==null || targetAddress.isEmpty()) {
                edtEdtWorkplaceAddress.setError("Vui lòng chọn vị trí!");
                Toast.makeText(this, "Vui lòng chọn vị trí!",Toast.LENGTH_SHORT).show();
                return;
            }

            //Update hoặc add cơ sở mới
            if(workplaceUpdate!=null) {
                handleUpdate(name);
            } else {
                handleAdd(name);
            }
        });
    }

    private void handleAdd(String name) {
        if(AppDatabase.getInstance(this).workplaceDao().checkDuplicate(name, targetAddress)>0) {
            edtWorkplaceName.setError("Tên hoặc địa chỉ cơ sở đã tồn tại!");
            edtWorkplaceName.requestFocus();
            return;
        }
        Workplace workplace = new Workplace(name, targetAddress, true, targetLocation.latitude, targetLocation.longitude);
        AppDatabase.getInstance(this).workplaceDao().insert(workplace);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultKey", "success");
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private void handleUpdate(String name) {
        // kh doi du lieu
        if(name.equals(oldName) && targetAddress.equals(oldAddress)) {
            finish();
            return;
        }

        if(!name.equals(oldName) && AppDatabase.getInstance(this).workplaceDao().checkNameExists(name)>0) {
            edtWorkplaceName.setError("Tên cơ sở đã tồn tại!");
            edtWorkplaceName.requestFocus();
            return;
        }

        if(!targetAddress.equals(oldAddress) && AppDatabase.getInstance(this).workplaceDao().checkAddressExists(targetAddress)>0) {
            edtWorkplaceName.setError("Địa chỉ đã tồn tại!");
            edtWorkplaceName.requestFocus();
            return;
        }

        workplaceUpdate.setWorkplaceName(name);
        workplaceUpdate.setAddress(targetAddress);
        workplaceUpdate.setLatitude(targetLocation.latitude);
        workplaceUpdate.setLongitude(targetLocation.longitude);
        AppDatabase.getInstance(this).workplaceDao().update(workplaceUpdate);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("resultKey", "success");
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private void loadInfoData() {
        edtWorkplaceName.setText(workplaceUpdate.getWorkplaceName());
        LatLng location = new LatLng(workplaceUpdate.getLatitude(), workplaceUpdate.getLongitude());
        addMarker(location, "Location");

        btnAdd.setText("Cập nhật");
        oldName = workplaceUpdate.getWorkplaceName();
        oldAddress = workplaceUpdate.getAddress();
    }

    private void searchLocation(String address) {
        // Sử dụng Geocoder để chuyển đổi địa chỉ thành tọa độ
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy địa chỉ!", Toast.LENGTH_SHORT).show();
                return;
            }
            Address location = addresses.get(0);

            LatLng searchLocation = new LatLng(location.getLatitude(), location.getLongitude());
            addMarker(searchLocation, "Location");

        } catch (IOException e) {
            Toast.makeText(this, "Có lỗi xảy ra khi tìm kiếm.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Lưu đối tượng gg map được trả về sau khi load map thành công
        ggMap = googleMap;

        // Bắt sự kiện ng dùng bấm vào bất kỳ vị trí trên bản đồ
        googleMap.setOnMapClickListener(latLng -> {
            addMarker(latLng, "location");
        });

        if(workplaceUpdate!=null) {
            loadInfoData();
        }

        // Yêu cầu quyền truy cập vị trí của ng dùng nếu chưa có
        enableUserLocation();
    }

    private void addMarker(LatLng position, String title) {
        // Nếu con trỏ đã tồn tại thì xoá đi
        if (marker != null) {
            marker.remove();
        }
        marker = ggMap.addMarker(new MarkerOptions().position(position).title(title));
        targetLocation = position;

        // Zoom vào vị trí ng dùng vừa bấm ( để dễ quan sát )
        ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));

        getAddress(position);
    }


    private void getAddress(LatLng location) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0);
                targetAddress = fullAddress;
                edtEdtWorkplaceAddress.setText(fullAddress);
            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Có lỗi xảy ra khi lấy địa chỉ.", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(this, "Quyền truy cập bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bindingView() {
        searchView = findViewById(R.id.search_view);
        edtWorkplaceName = findViewById(R.id.edt_workplace_name);
        edtEdtWorkplaceAddress = findViewById(R.id.edt_workplace_address);
        btnAdd = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.btn_back);
    }
}