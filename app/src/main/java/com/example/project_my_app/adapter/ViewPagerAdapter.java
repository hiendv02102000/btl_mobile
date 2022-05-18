package com.example.project_my_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project_my_app.fragment.ProfileFragment;
import com.example.project_my_app.model.User;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private User user;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, User user) {
        super(fm, behavior);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new ProfileFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}