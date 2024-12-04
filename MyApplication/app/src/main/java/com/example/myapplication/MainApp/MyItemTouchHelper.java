package com.example.myapplication.MainApp;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MyItemTouchHelper {
    private SwipedCallback swipedCallback;
    public interface SwipedCallback {
        void onSwiped(int position);
    }

    public MyItemTouchHelper(SwipedCallback swipedCallback) {
        this.swipedCallback = swipedCallback;
    }

    public androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback handleItemTouchHelper() {
        // tạo callback khi vuốt qua trái
        androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback simpleCallback = new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Hàm xử lý sau khi vuốt
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Lấy vị trí item xoá
                int position = viewHolder.getAbsoluteAdapterPosition();

                if(swipedCallback != null) {
                    swipedCallback.onSwiped(position);
                }
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
}
