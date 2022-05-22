package com.example.project_my_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.project_my_app.adapter.CommentListAdapter;
import com.example.project_my_app.api.APIInterface;
import com.example.project_my_app.api.Request;
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.Comment;
import com.example.project_my_app.model.Song;
import com.example.project_my_app.model.User;
import com.example.project_my_app.utils.ConverObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class PlayMusicActivity extends AppCompatActivity {
    private TextView playPosition,playDuration,titleTxt;
    private SeekBar seekBar ;
    private ImageView btnRew,btnPlay,btnPause,btnFF;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private float rotate;
    private Song song;
//comment
    private RecyclerView mRecycleListComment;
    private CommentListAdapter listCommentAdapter;
    private Button upCommentBtn;
    private EditText eCmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
    }
    private void init(){
        getSupportActionBar().hide();
        playPosition = findViewById(R.id.play_position);
        playDuration = findViewById(R.id.play_duration);
        seekBar = findViewById(R.id.seek_bar);
        btnRew = findViewById(R.id.btn_rew);
        btnFF = findViewById(R.id.btn_ff);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        titleTxt =findViewById(R.id.play_music_title);
        eCmt = findViewById(R.id.textComment);
        upCommentBtn = findViewById(R.id.upCmtBtn);
        mRecycleListComment = findViewById(R.id.listComment);
        listCommentAdapter = new CommentListAdapter(null);
        mRecycleListComment.setAdapter(listCommentAdapter);
        mRecycleListComment.setLayoutManager(new LinearLayoutManager(this));
        handler = new Handler();
        getSongDetailApi();
        getCommentsApi();
        upCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCommentsApi();
            }
        });
    }
    private void   initSong(){
        titleTxt.setText(song.getTitle() + " - "+ song.getSinger());
        String audioUrl = song.getContentUrl();
        mediaPlayer = MediaPlayer.create(this, Uri.parse(audioUrl));
        ImageView img = findViewById(R.id.image);
        String imgUrl = song.getImageUrl();
        if(imgUrl!=null &&!imgUrl.isEmpty()){
            Picasso.get().load(imgUrl).into(img);
        }
        rotate=0;
        runnable = new Runnable() {
            @Override
            public void run() {
                rotate+=2.2;
                img.animate().rotation(rotate).start();
                playPosition.setText(convertFormatDuration(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,42);
            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormatDuration(duration);
        playDuration.setText(sDuration);
        setUpBtn();
        setUpSeekBar();
    }
    private void setUpSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
                playPosition.setText(convertFormatDuration(mediaPlayer.getCurrentPosition()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPause.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
                handler.removeCallbacks(runnable);
            }
        });
    }

    private void setUpBtn(){
        setBtnPlay();
        setBtnPause();
        setBtnFF();
        setBtnRew();
    }
    private  void setBtnPlay(){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable,0);
            }
        });
    }
    private  void setBtnPause(){
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPause.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.removeCallbacks(runnable);
            }
        });
    }
    private  void setBtnFF(){
        btnFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if(mediaPlayer.isPlaying()) {
                    currentPos += 5000;
                    if (duration <= currentPos){
                        currentPos = duration;
                    }
                    playPosition.setText(convertFormatDuration(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }

            }
        });
    }
    private void setBtnRew(){
        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if(mediaPlayer.isPlaying()) {
                    currentPos -= 5000;
                    if (currentPos<0){
                        currentPos = 0;
                    }
                    playPosition.setText(convertFormatDuration(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }

            }
        });
    }


    private String convertFormatDuration(int duration){
        long minute = TimeUnit.MILLISECONDS.toMinutes(duration);
        long second = TimeUnit.MILLISECONDS.toSeconds(duration)- 60*minute;
        return String.format("%02d:%02d",
                minute,
                second);
    }
    private synchronized void getSongDetailApi() {
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        int songId = intent.getIntExtra("song_id",0);

        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.getDetailSong(songId)),"Bearer "+token)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        Log.d("TAG", "onResponse: "+res);
                        if(res != null && res.getStatus()==200){
                            song = (Song)ConverObject.converJsontoObject(res.getResult().getAsJsonObject("get_song_detail"),Song.class);
                            initSong();
                        }else {

                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {

                        call.cancel();
                    }

                });
    }
    private synchronized void getCommentsApi() {
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        int songId = intent.getIntExtra("song_id",0);

        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.getCommentSongQuery(songId)),"Bearer "+token)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        Log.d("TAG", "onResponse: "+ClientQuery.getCommentSongQuery(songId));
                        if(res != null && res.getStatus()==200){
                            List<Comment> list = new ArrayList<>();
                            JsonArray jsonArray = res.getResult().getAsJsonObject("get_comment_song").getAsJsonArray("comments");
                            for(JsonElement jsonElement : jsonArray){
                                Comment commentElement = (Comment) ConverObject.converJsontoObject(jsonElement.getAsJsonObject(),Comment.class);
                                list.add(commentElement);
                            }
                            listCommentAdapter.setLocalDataSet(list);

                        }else {

                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {

                        call.cancel();
                    }

                });
    }
    private synchronized void createCommentsApi() {
        if(eCmt.getText().toString().isEmpty()){
            return;
        }
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        int songId = intent.getIntExtra("song_id",0);
        Comment comment = new Comment();
        comment.setContent(eCmt.getText().toString());
        User u = new User();
        u.setUsername("Bạn");
        comment.setUser(u);
        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.createCmtApiMutation(comment,songId)),"Bearer "+token)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        Log.d("TAG", "onResponse: "+ClientQuery.getCommentSongQuery(songId));
                        if(res != null && res.getStatus()==200){
                            listCommentAdapter.addData(comment);
                        }else {

                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {

                        call.cancel();
                    }

                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}