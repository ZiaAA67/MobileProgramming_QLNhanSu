package com.example.myapplication.MainApp.AccountManagement;

import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Configuration;
import com.example.myapplication.MainApp.CheckInput;
import com.example.myapplication.MainApp.EmployeeRequest.EmployeeRequestActivity;
import com.example.myapplication.MainApp.EmployeeRequest.SpinnerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.Role;
import com.example.myapplication.database.entities.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AccountManagement extends AppCompatActivity {
    List<User> listUser;
    RecyclerView rcvUser;
    UserAdapter userAdapter;
    Spinner spinnerType;
    EditText edtSearch;
    SearchView searchView;
    ExtendedFloatingActionButton fabAdd;

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
        ItemTouchHelper.SimpleCallback callback = setupItemTouchHelper();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
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
                        searchWithName(s);
                        switch (i) {
                            case 0: loadData();
                                break;
                            case 1: searchWithName(s);
                                break;
                            case 2: searchWithRole(s);
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

        loadData();
    }

    private void handleClickAddUser() {
        // Show dialog
        Dialog dialog = new Dialog(AccountManagement.this);
        int layout = R.layout.dialog_add_user_layout;
        Configuration.showDialog(dialog, layout);

        // Binding view
        EditText edtUsername = dialog.findViewById(R.id.edt_username);
        EditText edtPassword = dialog.findViewById(R.id.edt_password);
        Spinner spinnerRole = dialog.findViewById(R.id.spinner_role);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);

        // Setup spinner
        List<String> data = new ArrayList<>();
        setupSpinnerInDialog(data, spinnerRole);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnAdd.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String role = (String) spinnerRole.getSelectedItem();

            if(!CheckInput.checkUsername(AccountManagement.this, edtUsername, username)) {
                return;
            }

            if(!CheckInput.isValidPassword(password)) {
                edtPassword.setError("Mật khẩu cần ít nhất 8 ký tự, bao gồm chữ số, chữ thường, và chữ hoa!");
                edtPassword.requestFocus();
                return;
            }

            int roleId = AppDatabase.getInstance(AccountManagement.this).roleDao().getRoleByName(role).getRoleId();
            User user = new User(username, Configuration.md5(password), Configuration.STRING_TODAY, true, true, roleId);
            AppDatabase.getInstance(AccountManagement.this).userDao().insert(user);

            dialog.dismiss();

            loadData();
        });

    }



    private void handleClickUpdateUser(User user) {
        // Show dialog
        Dialog dialog = new Dialog(AccountManagement.this);
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
                if(!CheckInput.checkUsername(AccountManagement.this, edtUsername, usernameSelected)) {
                    return;
                }
                user.setUsername(usernameSelected);
            }

            int roleId = AppDatabase.getInstance(AccountManagement.this).roleDao().getRoleByName(roleNameSelected).getRoleId();
            user.setRoleId(roleId);
            AppDatabase.getInstance(AccountManagement.this).userDao().update(user);

            dialog.dismiss();

            loadData();
        });

    }

    private void setupSpinnerInDialog(List<String> data, Spinner spinner) {
        AppDatabase.getInstance(this).roleDao().getAllRoles().forEach(r -> data.add(r.getRoleName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private ItemTouchHelper.SimpleCallback setupItemTouchHelper() {
        // tạo callback khi vuốt qua trái
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Hàm xử lý sau khi vuốt
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Lấy vị trí item xoá
                int position = viewHolder.getAbsoluteAdapterPosition();

                // thực hiện xoá item
                User user = listUser.get(position);
                user.setActive(false);
                AppDatabase.getInstance(AccountManagement.this).userDao().update(user);
                listUser.remove(position);

                // Thông báo vị trí xoá cho adapter -> load lại dữ liệu
                userAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                // Binding các layout view
                View itemView = viewHolder.itemView;
                View layoutBackground = itemView.findViewById(R.id.layout_background);
                View layoutForeground = itemView.findViewById(R.id.layout_foreground);

                // Cho background xuất hiện
                layoutBackground.setVisibility(View.VISIBLE);

                // Set vị trí cho foreground khi vuốt
                layoutForeground.setTranslationX(dX);

                // Nếu kh làm gì hoặc huỷ bỏ vuốt
                if (!isCurrentlyActive && dX == 0) {
                    layoutBackground.setVisibility(View.GONE);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        return simpleCallback;
    }

    private void searchWithName(String kw) {
        List<User> list = listUser.stream().filter(u -> u.getUsername().toLowerCase().contains(kw.toLowerCase())).collect(Collectors.toList());
        userAdapter.setData(list);
    }

    private void searchWithRole(String kw) {
        List<Role> listRole = AppDatabase.getInstance(AccountManagement.this).roleDao().getAllRoles();
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
        List<String> data = new ArrayList<>(Arrays.asList(new String[]{"Lọc", "Theo tên User", "Theo vai trò"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountManagement.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void bindingView() {
        rcvUser = findViewById(R.id.rcv_user);
        spinnerType = findViewById(R.id.spinner_type);
        edtSearch = findViewById(R.id.edt_search);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
    }
}