package com.example.project_my_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.project_my_app.R;
import com.example.project_my_app.adapter.MusicListAdapter;
import com.example.project_my_app.api.APIInterface;
import com.example.project_my_app.api.Request;
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.Song;
import com.example.project_my_app.model.User;
import com.example.project_my_app.utils.ConverObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class MusicListFragment extends Fragment {
    private User user;
    private int pageNum=0;
    private int pageSize = 4;
    private Map<Integer, Song> data;
    private RecyclerView songListRecycle;
    private SearchView songViewSearch;
    private MusicListAdapter musicListAdapter;
    private ProgressBar progressBar;
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
        init(view);
        return view;
    }
    private  void init(View view){
        data = new HashMap<>();
        musicListAdapter = new MusicListAdapter(data);
        musicListAdapter.notifyDataChanged();
        songListRecycle = view.findViewById(R.id.song_list_recycle);
        songListRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        songListRecycle.setAdapter(musicListAdapter);
        progressBar = view.findViewById(R.id.load_next);
        setRecyleViewListener();
        getSongListApi(null);
    }
    private void setRecyleViewListener(){
        songListRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                progressBar.setVisibility(View.VISIBLE);
                getSongListApi(null);
            }
        });
    }
    private synchronized void getSongListApi(Song song) {
        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.getMusicList(song,pageNum+1,pageSize)),"Bearer "+this.user.getToken())
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        Log.d("TAG", "onResponse: "+res);
                        if(res != null && res.getStatus()==200){
                            JsonArray jsonArray = res.getResult().getAsJsonObject("get_song_list").getAsJsonArray("songs");
                            for(JsonElement jsonElement : jsonArray){
                                Song songElement = (Song)ConverObject.converJsontoObject(jsonElement.getAsJsonObject(),Song.class);
                                data.put(songElement.getId(),songElement);
                            }
                            musicListAdapter.notifyDataChanged();
                            if(jsonArray.size()==pageSize) pageNum++;

                        }else {

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        call.cancel();
                    }

                });
    }
}