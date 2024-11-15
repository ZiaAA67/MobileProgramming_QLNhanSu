package com.example.myapplication.Demo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class DemoMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float RADIUS = 1000; // Bán kính quy định ( 1km )
    private GoogleMap ggMap; // Đối tượng google map
    private LatLng targetLocation; // Toạ độ được người dùng chấm ( target )
    private Marker marker; // Con trỏ vị trí
    private FusedLocationProviderClient fusedLocationClient; // Biến lưu vị trí của người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo bản đồ, nếu thành công sẽ gọi hàm onMapReady()
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Bắt sự kiện nhấn nút check location
        Button btnCheckLocation = findViewById(R.id.btnCheckLocation);
        btnCheckLocation.setOnClickListener(v -> checkUserLocation());

        // Lấy vị trí hiện tại của người dùng
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Lưu đối tượng gg map được trả về sau khi load map thành công
        ggMap = googleMap;

        // Bắt sự kiện ng dùng bấm vào bất kỳ vị trí trên bản đồ
        googleMap.setOnMapClickListener(latLng -> {
            // lấy toạ độ ng dùng bấm
            targetLocation = latLng;
            // Nếu con trỏ đã tồn tại thì xoá đi
            if (marker != null) {
                marker.remove();
            }
            // Tạo con trỏ ở toạ độ ng dùng đã bấm
            marker = ggMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
            // Zoom vào vị trí ng dùng vừa bấm ( để dễ quan sát )
            ggMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        });

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
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    

    // Hàm ktra vị trí
    private void checkUserLocation() {
        // Nếu ng dùng chưa chọn vị trí để check
        if (targetLocation == null) {
            Toast.makeText(this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ktra lại quyền truy cập
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Lấy vị trí hiện tại ( vị trí cuối cùng được trả về )
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    // Tạo biến lưu khoảng cách giữa ng dùng và điểm đã chọn
                    float[] distance = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), targetLocation.latitude, targetLocation.longitude, distance);

                    if (distance[0] <= RADIUS) {
                        Toast.makeText(DemoMapActivity.this, "Bạn đang trong bán kính 1km từ vị trí chỉ định.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DemoMapActivity.this, "Bạn không nằm trong bán kính 1km từ vị trí chỉ định.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



}