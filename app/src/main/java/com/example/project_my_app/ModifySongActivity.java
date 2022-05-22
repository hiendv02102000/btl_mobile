package com.example.project_my_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_my_app.api.APIInterface;
import com.example.project_my_app.api.Request;
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.dialog.ConfirmDialog;
import com.example.project_my_app.dialog.LoadingDialog;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.Song;
import com.example.project_my_app.utils.ConverObject;
import com.example.project_my_app.utils.RealPathUtil;
import com.example.project_my_app.utils.UploadFileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ModifySongActivity extends AppCompatActivity {
    private Song song;
    private EditText eTitle,eSinger,eDescription;
    private TextView btnUploadImg,statusMusicFile;
    private Button btnUploadMusicFile;
    private ImageView imageView;
    private Uri imgURI,musicURI;
    private Button btnDeleteSong,btnUpdateSong;
    private LoadingDialog loadingDialog = new LoadingDialog(this);
    private ActivityResultLauncher<Intent> activityResultLauncherWithImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null) return ;
                        Uri uri = data.getData();
                        imgURI = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);

                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> activityResultLauncherWithAudio = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null) return ;
                        Uri uri = data.getData();
                        musicURI = uri;
                        if(uri != null){
                            String path = RealPathUtil.getRealPath(ModifySongActivity.this,uri);
                            File file= new File(path);
                            statusMusicFile.setText("Đã tải file: "+file.getName());
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_song);
        init();
    }
    private void init(){
        eTitle = findViewById(R.id.text_title_song_update);
        eSinger = findViewById(R.id.text_title_singer_update);
        eDescription = findViewById(R.id.text_title_description_update);
        btnUploadImg = findViewById(R.id.btn_upload_image_file);
        statusMusicFile = findViewById(R.id.music_file_status);
        btnUploadMusicFile = findViewById(R.id.btn_upload_music_file);
        imageView = findViewById(R.id.music_img);
        btnUpdateSong = findViewById(R.id.btn_modify_song);
        btnDeleteSong = findViewById(R.id.btn_delete_song);
        getSongDetailApi();
        setUpBtn();
    }
    private void setUpBtn(){
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileUtil.onClickRequestPermission(ModifySongActivity.this,activityResultLauncherWithImg,"Select Avatar");
            }
        });
        btnUploadMusicFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileUtil.onClickRequestPermissionWithAudio(ModifySongActivity.this,activityResultLauncherWithAudio,"Select Music Audio");
            }
        });
        btnUpdateSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSongApi();
            }
        });
        btnDeleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(ModifySongActivity.this);
                dialog.setYesListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteSongApi();
                        dialog.dismissDialog();
                    }
                });
                dialog.setNoListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();
                    }
                });
                dialog.startDialog();
            }
        });
    }
    private void updateSongApi(){
        String token = getIntent().getStringExtra("token");
        MultipartBody.Part reqMultipartBodyImg=null;
        MultipartBody.Part reqMultipartBodyAudio=null;
        if(imgURI != null ){
            File file = new File(RealPathUtil.getRealPath(this,imgURI));
            RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            reqMultipartBodyImg = MultipartBody.Part.createFormData("image_file",file.getName(),requestBodyImg);
        }
        if (musicURI!= null){
            File audioFile = new File(RealPathUtil.getRealPath(this,musicURI));
            RequestBody requestBodyAudio = RequestBody.create(MediaType.parse("multipart/form-data"),audioFile);
            reqMultipartBodyAudio = MultipartBody.Part.createFormData("music_file",audioFile.getName(),requestBodyAudio);
        }

        Song newSong = new Song();
        newSong.setId(song.getId());
        newSong.setSinger(eSinger.getText().toString());
        newSong.setDescription(eDescription.getText().toString());
        newSong.setTitle(eTitle.getText().toString());
        if(newSong.getSinger().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tên ca sĩ không để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newSong.getTitle().isEmpty()){
            Toast.makeText(getApplicationContext(), "Bài hát chưa có tên", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.startLoadingDialog();
        RequestBody queryRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), ClientQuery.updateSongMutation(newSong));
        APIInterface.retrofit.clientQueryAPIWithFile(queryRequestBody,"Bearer "+token,reqMultipartBodyImg,reqMultipartBodyAudio)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();

                        if(res!=null && res.getStatus()==200){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        //Log.d("TAG", "onResponse: "+res);
                        loadingDialog.dismisDialog();
                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        if (t.toString().contains("Timeout")){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismisDialog();
                        call.cancel();
                    }
                });

    }
    private void deleteSongApi(){
        String token = getIntent().getStringExtra("token");

        loadingDialog.startLoadingDialog();
        RequestBody queryRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), ClientQuery.deleteSongMutation(song.getId()));
        APIInterface.retrofit.clientQueryAPIWithFile(queryRequestBody,"Bearer "+token)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();

                        if(res!=null && res.getStatus()==200){
                            finish();
                            Toast.makeText(getApplicationContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(getApplicationContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismisDialog();
                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        loadingDialog.dismisDialog();
                        call.cancel();
                    }
                });

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
                        if(res != null && res.getStatus()==200){
                            song = (Song) ConverObject.converJsontoObject(res.getResult().getAsJsonObject("get_song_detail"),Song.class);
                            eDescription.setText(song.getDescription());
                            eTitle.setText(song.getTitle());
                            eSinger.setText(song.getSinger());
                            String imgUrl = song.getImageUrl();
                            if(imgUrl!=null &&!imgUrl.isEmpty()){
                                Picasso.get().load(imgUrl).into(imageView);
                            }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == UploadFileUtil.READ_IMG_REQUEST){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                UploadFileUtil.openImageGallery(activityResultLauncherWithImg,"Select Music Image");
            }
        }
        if(requestCode == UploadFileUtil.READ_AUDIO_REQUEST){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                UploadFileUtil.openAudioGallery(activityResultLauncherWithAudio,"Select Music Audio");
            }
        }
    }
}