package com.example.myapplication.main_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Viewpager2Adapter extends FragmentStateAdapter {
    private int userId;

    public Viewpager2Adapter(@NonNull FragmentActivity fragmentActivity, int userId) {
        super(fragmentActivity);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return createFragmentWithUserId(new HomeFragment(), userId);
            case 1:
                return createFragmentWithUserId(new NotifyFragment(), userId);
            case 2:
                return createFragmentWithUserId(new ProfileFragment(), userId);
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private Fragment createFragmentWithUserId(Fragment fragment, int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt("UserID", userId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
