package com.example.myapplication.MainApp.Workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

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

import com.example.myapplication.MainApp.MyItemTouchHelper;
import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.entities.Employee;
import com.example.myapplication.database.entities.Workplace;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkplaceManagement extends AppCompatActivity {
    private List<Workplace> listWorkplace;
    private RecyclerView rcvWorkplace;
    private WorkplaceAdapter workplaceAdapter;
    private SearchView searchView;
    private ExtendedFloatingActionButton fabAdd;
    private Button btnBack;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workplace_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();

        // Setup adapter
        listWorkplace = new ArrayList<>();
        workplaceAdapter = new WorkplaceAdapter(listWorkplace, new WorkplaceAdapter.IClickItemWorkplace() {
            @Override
            public void clickUpdateWorkplace(Workplace workplace) {
                handleClickUpdateWorkplace(workplace);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvWorkplace.setLayoutManager(linearLayoutManager);
        rcvWorkplace.setAdapter(workplaceAdapter);

        // Tạo hàm callback khi vuốt
        // Tạo hàm callback khi vuốt
        ItemTouchHelper itemTouchHelper = setupItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(rcvWorkplace);

        // clear focus cho search bar ( áp dụng với thiết bị api thấp ), khi thay đổi text trong search view
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                List<Workplace> list = listWorkplace.stream().filter(d -> d.getWorkplaceName().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
//                workplaceAdapter.setData(list);
                return true;
            }
        });

        // Thêm workplace mới
        fabAdd.setOnClickListener(v -> {
            handleClickAddWorkplace();
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

    private void handleClickUpdateWorkplace(Workplace workplace) {
        Intent intent = new Intent(this, WorkplaceAddItem.class);
        intent.putExtra("workplaceKey", workplace);
        activityResultLauncher.launch(intent);
    }


    private void handleClickAddWorkplace() {
        Intent intent = new Intent(this, WorkplaceAddItem.class);
        activityResultLauncher.launch(intent);
    }


    @NonNull
    private ItemTouchHelper setupItemTouchHelper() {
        MyItemTouchHelper myItemTouchHelper = new MyItemTouchHelper(position -> {
            // Lấy vị trí item xoá
            Workplace workplace = listWorkplace.get(position);

            // Thực hiện xoá item, Thông báo vị trí xoá cho adapter -> load lại dữ liệu
            listWorkplace.remove(position);
            workplaceAdapter.notifyItemRemoved(position);


            // Hiển thị Snackbar với nút Undo
            Snackbar.make(rcvWorkplace, "Đã xóa " + workplace.getWorkplaceName(), Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác", v -> {
                        // Khôi phục item nếu người dùng chọn Undo
                        listWorkplace.add(position, workplace);
                        workplaceAdapter.notifyItemInserted(position);
                        rcvWorkplace.scrollToPosition(position);
                    })
                    .addCallback(new Snackbar.Callback() {
                        // Nếu Snackbar bị ẩn mà không chọn Undo
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // Thực hiện xóa chính thức
                                workplace.setActive(false);
                                AppDatabase.getInstance(WorkplaceManagement.this).workplaceDao().update(workplace);

                                List<Employee> list = AppDatabase.getInstance(WorkplaceManagement.this).employeeDao().getByWorkplaceId(workplace.getWorkplaceId());
                                list.forEach(e -> {
                                    e.setWorkplaceId(null);
                                    AppDatabase.getInstance(WorkplaceManagement.this).employeeDao().update(e);
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
        listWorkplace = AppDatabase.getInstance(this).workplaceDao().getActiveWorkplace();
        workplaceAdapter.setData(listWorkplace);
    }

    private void bindingView() {
        rcvWorkplace = findViewById(R.id.rcv_workplace);
        searchView = findViewById(R.id.search_view);
        fabAdd = findViewById(R.id.fab_add);
        btnBack = findViewById(R.id.btn_back);
    }
}