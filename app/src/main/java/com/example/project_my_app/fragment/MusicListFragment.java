package com.example.project_my_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_my_app.R;
import com.example.project_my_app.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicListFragment extends Fragment {
    private User user;
    private int pageNum=0;
    private int pageSize = 4;

    // TODO: Rename and change types of parameters

    public MusicListFragment(User user) {
        this.user = user;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        return view;
    }
}