package com.example.myapplication.MainApp.UserAccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

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

import com.example.myapplication.MainApp.Configuration;
import com.example.myapplication.MainApp.CheckInput;
import com.example.myapplication.MainApp.MyItemTouchHelper;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserAccountManagement extends AppCompatActivity {
    private List<User> listUser;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private Spinner spinnerType;
    private SearchView searchView;
    private ExtendedFloatingActionButton fabAdd;
    private Button btnBack;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        setupSpinnerType();

        // Setup adapter
        listUser = new ArrayList<>();
        userAdapter = new UserAdapter(this, listUser, new UserAdapter.IClickItemUser() {
            @Override
            public void clickUpdateUser(User user) {
                handleClickUpdateUser(user);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

        // Tạo hàm callback khi vuốt
        // Tạo hàm callback khi vuốt
        ItemTouchHelper itemTouchHelper = setupItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(rcvUser);

        // clear focus cho search bar ( áp dụng với thiết bị api thấp )
        searchView.clearFocus();

        // Bắt sự kiện thay đổi spinner type, xử lý khi nhập input vào search bar
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Khi thay đổi spinner type, bắt sự kiện nhập text vào input search
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        switch (i) {
                            case 0: searchWithName(s);
                                break;
                            case 1: searchWithRole(s);
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

        // Thêm user mới
        fabAdd.setOnClickListener(v -> {
            handleClickAddUser();
        });

        btnBack.setOnClickListener(v -> finish());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data!=null && Objects.equals(data.getStringExtra("resultKey"), "success")) {
                    loadData();
                }
            }
        });

        loadData();
    }

    private void handleClickAddUser() {
        Intent intent = new Intent(this, UserAccountAddItem.class);
        activityResultLauncher.launch(intent);
    }

    private void handleClickUpdateUser(User user) {
        // Show dialog
        Dialog dialog = new Dialog(UserAccountManagement.this);
        int layout = R.layout.dialog_update_user_layout;
        Configuration.showDialog(dialog, layout);

        // Binding view
        EditText edtUsername = dialog.findViewById(R.id.edt_username);
        Spinner spinnerRole = dialog.findViewById(R.id.spinner_role);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);

        // Get data to edit text
        edtUsername.setText(user.getUsername());

        // Setup spinner
        List<String> data = new ArrayList<>();
        setupSpinnerInDialog(data, spinnerRole);

        // Selected spinner item
        spinnerRole.setSelection(user.getRoleId()-1);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnUpdate.setOnClickListener(v -> {
            String usernameSelected = edtUsername.getText().toString().trim();
            String roleNameSelected = (String) spinnerRole.getSelectedItem();

            if(!user.getUsername().equals(usernameSelected)) {
                if(!CheckInput.checkUsername(UserAccountManagement.this, edtUsername, usernameSelected)) {
                    return;
                }
                user.setUsername(usernameSelected);
            }

            int roleId = AppDatabase.getInstance(UserAccountManagement.this).roleDao().getRoleByName(roleNameSelected).getRoleId();
            user.setRoleId(roleId);
            AppDatabase.getInstance(UserAccountManagement.this).userDao().update(user);

            dialog.dismiss();

            loadData();
        });

    }

    private void setupSpinnerInDialog(List<String> data, Spinner spinner) {
        AppDatabase.getInstance(this).roleDao().getAllRoles().forEach(r -> data.add(r.getRoleName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserAccountManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @NonNull
    private ItemTouchHelper setupItemTouchHelper() {
        MyItemTouchHelper myItemTouchHelper = new MyItemTouchHelper(position -> {
            // Lấy vị trí item xoá
            User user = listUser.get(position);

            // Thực hiện xoá item, Thông báo vị trí xoá cho adapter -> load lại dữ liệu
            listUser.remove(position);
            userAdapter.notifyItemRemoved(position);


            // Hiển thị Snackbar với nút Undo
            Snackbar.make(rcvUser, "Đã xóa " + user.getUsername(), Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác", v -> {
                        // Khôi phục item nếu người dùng chọn Undo
                        listUser.add(position, user);
                        userAdapter.notifyItemInserted(position);
                        rcvUser.scrollToPosition(position);
                    })
                    .addCallback(new Snackbar.Callback() {
                        // Nếu Snackbar bị ẩn mà không chọn Undo
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // Thực hiện xóa chính thức
                                user.setActive(false);
                                AppDatabase.getInstance(UserAccountManagement.this).userDao().update(user);

                                // set account employee la null
                                Employee em = AppDatabase.getInstance(UserAccountManagement.this).employeeDao().getEmployeeByUserId(user.getUserId());
                                em.setUserId(null);
                                AppDatabase.getInstance(UserAccountManagement.this).employeeDao().update(em);
                            }
                        }
                    })
                    .show();
        });
        ItemTouchHelper.SimpleCallback simpleCallback = myItemTouchHelper.handleItemTouchHelper();
        return new ItemTouchHelper(simpleCallback);
    }

    private void searchWithName(String kw) {
        List<User> list = listUser.stream().filter(u -> u.getUsername().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        userAdapter.setData(list);
    }

    private void searchWithRole(String kw) {
        List<Role> listRole = AppDatabase.getInstance(UserAccountManagement.this).roleDao().getAllRoles();
        listRole = listRole.stream().filter(r -> r.getRoleName().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());

        List<User> result = new ArrayList<>();
        listRole.forEach(r -> result.addAll(listUser.stream().filter(u -> u.getRoleId() == r.getRoleId()).collect(Collectors.toList())));

        userAdapter.setData(result);
    }

    private void loadData() {
        listUser = AppDatabase.getInstance(this).userDao().getActiveUsers();
        userAdapter.setData(listUser);
    }

    private void setupSpinnerType() {
        List<String> data = new ArrayList<>(Arrays.asList(new String[]{"Theo tên User", "Theo vai trò"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserAccountManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void bindingView() {
        rcvUser = findViewById(R.id.rcv_user);
        spinnerType = findViewById(R.id.spinner_type);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
        btnBack = findViewById(R.id.btn_back);
    }
}