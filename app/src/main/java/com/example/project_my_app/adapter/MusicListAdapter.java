package com.example.project_my_app.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_my_app.R;
import com.example.project_my_app.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Map<Integer, Song> data;
    private SelectItemListener selectItemListener;
    private List<Song> songList;

    public MusicListAdapter(Map<Integer, Song> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_hor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.descriptionTxt.setText("Mô tả: "+song.getDescription());
        holder.viewTxt.setText("Lượt xem: "+song.getViews()+"");
        holder.titleTxt.setText(song.getTitle());
        holder.singerTxt.setText("Ca sĩ: "+song.getSinger());
        String imgUrl = song.getImageUrl();
        if(imgUrl!=null &&!imgUrl.isEmpty()){
            Picasso.get().load(imgUrl).into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemListener.onSelectItem(song.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(songList !=null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return songList.get(position).getId();
    }

    public void notifyDataChanged(){
        songList = new ArrayList<>();
        for (Map.Entry<Integer, Song> entry : data.entrySet()) {
            songList.add(entry.getValue());
        }
        Collections.sort(songList);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView singerTxt;
        private TextView titleTxt;
        private TextView viewTxt;
        private TextView descriptionTxt;
        private ImageView img;
        private View viewItem;
        public ViewHolder(View view) {
            super(view);
            singerTxt = view.findViewById(R.id.singer_song);
            titleTxt = view.findViewById(R.id.title_song);
            viewTxt = view.findViewById(R.id.views_song);
            descriptionTxt = view.findViewById(R.id.description_song);
            img =view.findViewById(R.id.image_song);
            viewItem =view;
        }
    }

    public void setSelectItemListener(SelectItemListener selectItemListener) {
        this.selectItemListener = selectItemListener;
    }
}
