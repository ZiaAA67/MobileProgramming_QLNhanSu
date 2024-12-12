package com.example.myapplication.MainApp.Department;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.MyItemTouchHelper;
import com.example.myapplication.R;
import com.example.myapplication.Register.InformationRegister;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Employee;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DepartmentManagement extends AppCompatActivity {
    private List<Department> listDepartment;
    private RecyclerView rcvDepartment;
    private DepartmentAdapter departmentAdapter;
    private SearchView searchView;
    private ExtendedFloatingActionButton fabAdd;
    private Button btnBack;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_department_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        // Setup adapter
        listDepartment = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(listDepartment, new DepartmentAdapter.IClickItemDepartment() {
            @Override
            public void clickUpdateDepartment(Department department) {
                handleClickUpdateDepartment(department);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDepartment.setLayoutManager(linearLayoutManager);
        rcvDepartment.setAdapter(departmentAdapter);

        // Tạo hàm callback khi vuốt
        // Tạo hàm callback khi vuốt
        ItemTouchHelper itemTouchHelper = setupItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(rcvDepartment);

        // clear focus cho search bar ( áp dụng với thiết bị api thấp ), khi thay đổi text trong search view
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Department> list = listDepartment.stream().filter(d -> d.getDepartmentName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                departmentAdapter.setData(list);
                return true;
            }
        });

        // Thêm department mới
        fabAdd.setOnClickListener(v -> {
            handleClickAddDepartment();
        });

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

    private void handleClickUpdateDepartment(Department department) {
        Intent intent = new Intent(this, DepartmentUpdateItem.class);
        intent.putExtra("departmentKey", department);
        activityResultLauncher.launch(intent);
    }


    private void handleClickAddDepartment() {
        // Show dialog
        Dialog dialog = new Dialog(DepartmentManagement.this);
        int layout = R.layout.dialog_add_department_layout;
        Configuration.showDialog(dialog, layout);

        // Binding view
        EditText edtDepartmentName = dialog.findViewById(R.id.edt_department_name);
        EditText edtDepartmentDesc = dialog.findViewById(R.id.edt_department_desc);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnAdd.setOnClickListener(v -> {
            String name = edtDepartmentName.getText().toString().trim();
            String desc = edtDepartmentDesc.getText().toString().trim();

            if(name.isEmpty()) {
                edtDepartmentName.setError("Vui lòng nhập thông tin!");
                return;
            }

            if(!checkDepartmentExist(name)) {
                AppDatabase.getInstance(DepartmentManagement.this).departmentDao().insert(new Department(name, true, desc));

                dialog.dismiss();
                loadData();
            } else {
                edtDepartmentName.setError("Phòng ban đã tồn tại!");
                edtDepartmentName.requestFocus();
                return;
            }
        });
    }

    private boolean checkDepartmentExist(String name) {
        List<Department> list = AppDatabase.getInstance(this).departmentDao().getListDepartmentByName(name);

        if(list.isEmpty()) return false;

        long count = list.stream().filter(Department::isActive).count();
        if(count == 0) return false;

        return true;
    }

    @NonNull
    private ItemTouchHelper setupItemTouchHelper() {
        MyItemTouchHelper myItemTouchHelper = new MyItemTouchHelper(position -> {
            // Lấy vị trí item xoá
            Department department = listDepartment.get(position);

            // Thực hiện xoá item, Thông báo vị trí xoá cho adapter -> load lại dữ liệu
            listDepartment.remove(position);
            departmentAdapter.notifyItemRemoved(position);


            // Hiển thị Snackbar với nút Undo
            Snackbar.make(rcvDepartment, "Đã xóa " + department.getDepartmentName(), Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác", v -> {
                        // Khôi phục item nếu người dùng chọn Undo
                        listDepartment.add(position, department);
                        departmentAdapter.notifyItemInserted(position);
                        rcvDepartment.scrollToPosition(position);
                    })
                    .addCallback(new Snackbar.Callback() {
                        // Nếu Snackbar bị ẩn mà không chọn Undo
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // Thực hiện xóa chính thức
                                department.setActive(false);
                                AppDatabase.getInstance(DepartmentManagement.this).departmentDao().update(department);

                                List<Employee> list = AppDatabase.getInstance(DepartmentManagement.this).employeeDao().getByDepartmentId(department.getDepartmentId());
                                list.forEach(e -> {
                                    e.setDepartmentId(null);
                                    AppDatabase.getInstance(DepartmentManagement.this).employeeDao().update(e);
                                });
                            }
                        }
                    })
                    .show();
        });
        ItemTouchHelper.SimpleCallback simpleCallback = myItemTouchHelper.handleItemTouchHelper();
        return new ItemTouchHelper(simpleCallback);
    }

    private void loadData() {
        listDepartment = AppDatabase.getInstance(this).departmentDao().getActiveDepartment();
        departmentAdapter.setData(listDepartment);
    }

    private void bindingView() {
        rcvDepartment = findViewById(R.id.rcv_department);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
        btnBack = findViewById(R.id.btn_back);
    }
}