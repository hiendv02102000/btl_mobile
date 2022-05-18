package com.example.project_my_app;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity {
    private TextView playPosition,playDuration;
    private SeekBar seekBar ;
    private ImageView btnRew,btnPlay,btnPause,btnFF;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private float rotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
    }
    private void init(){
        playPosition = findViewById(R.id.play_position);
        playDuration = findViewById(R.id.play_duration);
        seekBar = findViewById(R.id.seek_bar);
        btnRew = findViewById(R.id.btn_rew);
        btnFF = findViewById(R.id.btn_ff);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        handler = new Handler();
        String audioUrl = "https://res.cloudinary.com/dtctadira/video/upload/v1652797138/backend-food/go/StillLife-BIGBANG-7182115.mp3.mp3";
        mediaPlayer = MediaPlayer.create(this, Uri.parse(audioUrl));
        ImageView img = findViewById(R.id.image);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}