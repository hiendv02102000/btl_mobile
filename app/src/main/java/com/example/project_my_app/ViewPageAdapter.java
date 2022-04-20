package com.example.project_my_app;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;



public class ViewPageAdapter extends FragmentStatePagerAdapter{


    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return  new FavoriteFragment();
            default:
                return new NotificationFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String titile="";
        switch (position){
            case 0:
                titile= "Home";
                break;
            case 1:
                titile= "Favorite";
                break;
            default:
                titile= "Notification";
                break;
        }
        return  titile;
    }
}
