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
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.dialog.LoadingDialog;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.Song;
import com.example.project_my_app.model.User;
import com.example.project_my_app.utils.ConverObject;
import com.example.project_my_app.utils.RealPathUtil;
import com.example.project_my_app.utils.UploadFileUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddSongActivity extends AppCompatActivity {
    private EditText eTitle,eSinger,eDescription;
    private TextView btnUploadImg,statusMusicFile;
    private Button btnUploadMusicFile;
    private ImageView imageView;
    private Uri imgURI,musicURI;
    private Button btnAddSong;
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
                            String path = RealPathUtil.getRealPath(AddSongActivity.this,uri);
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
        setContentView(R.layout.activity_add_song);
        init();
    }
    private void init(){
        eTitle = findViewById(R.id.text_title_song_add);
        eSinger = findViewById(R.id.text_title_singer_add);
        eDescription = findViewById(R.id.text_title_description_add);
        btnUploadImg = findViewById(R.id.btn_upload_image_file);
        statusMusicFile = findViewById(R.id.music_file_status);
        btnUploadMusicFile = findViewById(R.id.btn_upload_music_file);
        imageView = findViewById(R.id.music_img);
        btnAddSong = findViewById(R.id.btn_add_song);
        setUpBtn();
    }
    private void setUpBtn(){
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileUtil.onClickRequestPermission(AddSongActivity.this,activityResultLauncherWithImg,"Select Avatar");
            }
        });
        btnUploadMusicFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileUtil.onClickRequestPermissionWithAudio(AddSongActivity.this,activityResultLauncherWithAudio,"Select Music Audio");
            }
        });
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSongApi();
            }
        });
    }
    private void createSongApi(){
        String token = getIntent().getStringExtra("token");
        MultipartBody.Part reqMultipartBodyImg=null;
        MultipartBody.Part reqMultipartBodyAudio=null;
        if(imgURI != null ){
            File file = new File(RealPathUtil.getRealPath(this,imgURI));
            RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            reqMultipartBodyImg = MultipartBody.Part.createFormData("image_file",file.getName(),requestBodyImg);
        }
        if (musicURI == null){
            Toast.makeText(getApplicationContext(), "Chưa tải nhạc lên", Toast.LENGTH_SHORT).show();
            return;
        }
        File audioFile = new File(RealPathUtil.getRealPath(this,musicURI));
        RequestBody requestBodyAudio = RequestBody.create(MediaType.parse("multipart/form-data"),audioFile);
        reqMultipartBodyAudio = MultipartBody.Part.createFormData("music_file",audioFile.getName(),requestBodyAudio);
        Song newSong = new Song();
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
        RequestBody queryRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), ClientQuery.createSongMutation(newSong));
        APIInterface.retrofit.clientQueryAPIWithFile(queryRequestBody,"Bearer "+token,reqMultipartBodyImg,reqMultipartBodyAudio)
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();

                        if(res!=null && res.getStatus()==200){
                            finish();
                            Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("TAG", "onResponse: "+res);
                        loadingDialog.dismisDialog();
                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(), "Thêm thất bại"+t, Toast.LENGTH_SHORT).show();
                        if (t.toString().contains("Timeout")){
                            finish();
                            Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismisDialog();
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