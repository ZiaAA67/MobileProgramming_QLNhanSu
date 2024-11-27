package com.example.myapplication.MainApp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.database.entities.User;

public class Viewpager2Adapter extends FragmentStateAdapter {
    private User user;

    public Viewpager2Adapter(@NonNull FragmentActivity fragmentActivity, User user) {
        super(fragmentActivity);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return createFragmentWithUserId(new HomeFragment(), user);
            case 1:
                return createFragmentWithUserId(new NotifyFragment(), user);
            case 2:
                return createFragmentWithUserId(new ProfileFragment(), user);
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private Fragment createFragmentWithUserId(Fragment fragment, User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        fragment.setArguments(bundle);
        return fragment;
    }
}
