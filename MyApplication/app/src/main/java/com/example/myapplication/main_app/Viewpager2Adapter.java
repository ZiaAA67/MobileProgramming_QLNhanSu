package com.example.myapplication.main_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Viewpager2Adapter extends FragmentStateAdapter {
    public Viewpager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 1:
                return new NotifyFragment();
            case 2:
                return new ProfileFragment();

            //case 0 and default
            default:
                return new HomeFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
