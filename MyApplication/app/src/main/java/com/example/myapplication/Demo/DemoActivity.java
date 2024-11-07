package com.example.myapplication.Demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    EditText edtUsername;
    EditText edtAddress;
    Button btnAdd;
    TextView tvDeleteAll;
    EditText edtSearch;

    RecyclerView rcvUser;
    List<User> mListUser;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();

        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        mListUser = new ArrayList<>();
        userAdapter.setData(mListUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);

        rcvUser.setAdapter(userAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearchUser();
                }
                return false;
            }
        });

        loadData();
    }

    private void addUser() {
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();

        if(TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)) {
            return;
        }

        User user = new User(strUsername, strAddress);

        if(checkUserExist(user)) {
            Toast.makeText(this, "Error: Username Exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this, "Add success!", Toast.LENGTH_SHORT).show();

        loadData();

        edtUsername.setText("");
        edtAddress.setText("");
        edtUsername.requestFocus();

        hideSoftKeyboard();
    }

    private boolean checkUserExist(User user) {
        List<User> list = AppDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list != null && !list.isEmpty();
    }

    private void clickUpdateUser(User user) {
        Intent intent = new Intent(DemoActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    private void clickDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Comfirm Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase.getInstance(DemoActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(DemoActivity.this, "Delete success!", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clickDeleteAllUser() {
        new AlertDialog.Builder(this)
                .setTitle("Comfirm Delete All")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase.getInstance(DemoActivity.this).userDAO().deleteAllUser();
                        Toast.makeText(DemoActivity.this, "Delete all success!", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void handleSearchUser() {
        String strKeyword = edtSearch.getText().toString().trim();
        mListUser = new ArrayList<>();
        mListUser = AppDatabase.getInstance(this).userDAO().searchUser(strKeyword);
        userAdapter.setData(mListUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    private void loadData() {
        mListUser = AppDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(mListUser);
    }

    private void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow((getCurrentFocus().getWindowToken()), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initUI() {
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAdd = findViewById(R.id.btn_add);
        tvDeleteAll = findViewById(R.id.tv_delete_all);
        edtSearch = findViewById(R.id.edt_search);

        rcvUser = findViewById(R.id.rcv_user);
    }
}