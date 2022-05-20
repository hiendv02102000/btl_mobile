package com.example.project_my_app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_my_app.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Map<Integer, Song> data;
    private List<Song> songList;

    public MusicListAdapter(Map<Integer, Song> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(data !=null) {
            return data.size();
        }
        return 0;
    }

    public void notifyDataChanged(){
        songList = new ArrayList<>();
        for (Map.Entry<Integer, Song> entry : data.entrySet()) {
            songList.add(entry.getValue());
        }
        this.notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
