package com.example.myapplication.MainApp.Employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainApp.Department.DepartmentManagement;
import com.example.myapplication.MainApp.MyItemTouchHelper;
import com.example.myapplication.MainApp.UserAccount.UserAccountManagement;
import com.example.myapplication.MainApp.UserAccount.UserAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Register.InformationRegister;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeeManagement extends AppCompatActivity {

    private List<Employee> listEmployee;
    private RecyclerView rcvEmployee;
    private EmployeeAdapter employeeAdapter;
    private Spinner spinnerType;
    private SearchView searchView;
    private ExtendedFloatingActionButton fabAdd;
    private Button btnBack;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setupSpinnerType();

        // Setup adapter
        listEmployee = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(listEmployee, new EmployeeAdapter.IClickItemEmployee() {
            @Override
            public void clickUpdateEmployee(Employee employee) {
                handleClickUpdateEmployee(employee);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEmployee.setLayoutManager(linearLayoutManager);
        rcvEmployee.setAdapter(employeeAdapter);

        // Tạo hàm callback khi vuốt
        ItemTouchHelper itemTouchHelper = setupItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(rcvEmployee);

        // Bắt sự kiện thay đổi spinner type, xử lý khi nhập input vào search bar
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Khi thay đổi input text
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 4) {
                    searchWithApproveStatus();
                }
                if(i ==5) {
                    searchWithDisapproveStatus();
                }

                // bắt sự kiện nhập text vào input search
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        switch (i) {
                            case 0: loadData();
                                break;
                            case 1: searchWithName(s);
                                break;
                            case 2: searchWithAddress(s);
                                break;
                            case 3: searchWithEmail(s);
                                break;
                        }
                        return true;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Thêm nhân viên mới
        fabAdd.setOnClickListener(v -> {
            handleClickAddEmployee();
        });

        // Khi thêm nhân viên mới, nếu trạng thái là thành công thì load lại dữ liệu
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data!=null && Objects.equals(data.getStringExtra("resultKey"), "success")) {
                    loadData();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        loadData();
    }

    @NonNull
    private ItemTouchHelper setupItemTouchHelper() {
        MyItemTouchHelper myItemTouchHelper = new MyItemTouchHelper(position -> {
            // Lấy vị trí item xoá
            Employee employee = listEmployee.get(position);

            // Thực hiện xoá item, Thông báo vị trí xoá cho adapter -> load lại dữ liệu
            employee.setActive(false);
            employeeAdapter.notifyItemRemoved(position);


            // Hiển thị Snackbar với nút Undo
            Snackbar.make(rcvEmployee, "Đã xóa " + employee.getFullName(), Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác", v -> {
                        // Khôi phục item nếu người dùng chọn Undo
                        listEmployee.add(position, employee);
                        employeeAdapter.notifyItemInserted(position);
                        rcvEmployee.scrollToPosition(position);
                    })
                    .addCallback(new Snackbar.Callback() {
                        // Nếu Snackbar bị ẩn mà không chọn Undo
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // Thực hiện xóa chính thức
                                AppDatabase.getInstance(EmployeeManagement.this).employeeDao().update(employee);
                                listEmployee.remove(position);
                            }
                        }
                    })
                    .show();
        });
        ItemTouchHelper.SimpleCallback simpleCallback = myItemTouchHelper.handleItemTouchHelper();
        return new ItemTouchHelper(simpleCallback);
    }

    private void handleClickUpdateEmployee(Employee employee) {
        Intent intent = new Intent(this, InformationRegister.class);
        intent.putExtra("message", "AdminUpdate");
        intent.putExtra("employeeKey", employee);
        activityResultLauncher.launch(intent);
    }


    private void handleClickAddEmployee() {
        Intent intent = new Intent(this, InformationRegister.class);
        intent.putExtra("message", "AdminCreate");
        activityResultLauncher.launch(intent);
    }

    private void loadData() {
        listEmployee = AppDatabase.getInstance(this).employeeDao().getActiveEmployees();
        employeeAdapter.setData(listEmployee);
    }

    private void setupSpinnerType() {
        List<String> data = new ArrayList<>(Arrays.asList(new String[]{"Lọc", "Theo tên", "Theo địa chỉ", "Theo email", "Đã được duyệt", "Chưa được duyệt"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EmployeeManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void searchWithName(String kw) {
        List<Employee> list = listEmployee.stream().filter(e -> e.getFullName().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void searchWithAddress(String kw) {
        List<Employee> list = listEmployee.stream().filter(e -> e.getAddress().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void searchWithEmail(String kw) {
        List<Employee> list = listEmployee.stream().filter(e -> e.getEmail().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void searchWithApproveStatus() {
        List<Employee> list = listEmployee.stream().filter(e -> e.isApprove()).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void searchWithDisapproveStatus() {
        List<Employee> list = listEmployee.stream().filter(e -> !e.isApprove()).collect(Collectors.toList());
        employeeAdapter.setData(list);
    }

    private void bindingView() {
        rcvEmployee = findViewById(R.id.rcv_employee);
        spinnerType = findViewById(R.id.spinner_type);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
        btnBack = findViewById(R.id.btn_back);
    }
}