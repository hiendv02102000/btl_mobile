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
import com.example.project_my_app.dialog.LoadingDialog;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.User;
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

public class ModifyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button changeProfileBtn;
    private Button changePasswordBtn;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText oldPassEdit;
    private EditText newPassEdit;
    private EditText confirmNewPassEdit;
    private User user;
    private ImageView avatarImage;
    private TextView changeAvatarTxt;
    private Uri imgURI;
    private LoadingDialog loadingDialog = new LoadingDialog(this);
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
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
                            avatarImage.setImageBitmap(bitmap);

                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        init();
    }
    private void init (){
        changeProfileBtn = findViewById(R.id.btn_change_profile);
        changePasswordBtn = findViewById(R.id.btn_change_password);
        firstNameEdit = findViewById(R.id.text_first_name);
        lastNameEdit = findViewById(R.id.text_last_name);
        oldPassEdit = findViewById(R.id.text_old_password);
        newPassEdit = findViewById(R.id.text_new_password);
        confirmNewPassEdit = findViewById(R.id.text_confirm_new_password);
        avatarImage = findViewById(R.id.avatar_img);
        changeAvatarTxt = findViewById(R.id.modify_avatar_txt);
        initData();
        changeAvatarTxt.setOnClickListener(this::onClick);
        changeProfileBtn.setOnClickListener(this::onClick);
        changePasswordBtn.setOnClickListener(this::onClick);
    }
    private void updateProfileWithData(){
        firstNameEdit.setText(user.getFirstName() );
        lastNameEdit.setText(user.getLastName() );
        String avatarURL = user.getAvatarUrl();
        if(avatarURL!=null && !avatarURL.isEmpty())
            Picasso.get().load(avatarURL).into(avatarImage);
    }
    private void initData(){
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.d("TAG", "initData: "+user);
        updateProfileWithData();
    }
    private void changeProfileApi(){
                MultipartBody.Part reqMultipartBodyImg=null;
                if(imgURI != null ){
                    File file = new File(RealPathUtil.getRealPath(ModifyProfileActivity.this,imgURI));
                    RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                    reqMultipartBodyImg = MultipartBody.Part.createFormData("file",file.getName(),requestBodyImg);
                }
                user.setLastName(lastNameEdit.getText().toString());
                user.setFirstName(firstNameEdit.getText().toString());
                loadingDialog.startLoadingDialog();
                RequestBody queryRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), ClientQuery.changeProfileMutation(user));
                APIInterface.retrofit.clientQueryAPIWithFile(queryRequestBody,"Bearer "+user.getToken(),reqMultipartBodyImg)
                        .enqueue(new Callback<ResponseAPI>() {
                            @Override
                            public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                                ResponseAPI res = response.body();

                                if(res!=null && res.getStatus()==200){
                                    User userC = (User) ConverObject.converJsontoObject(res.getResult().getAsJsonObject("update_user_profile"),User.class);
                                    Intent intentReturn =new Intent();
                                    intentReturn.putExtra("new_user",userC);
                                    setResult(Activity.RESULT_OK,intentReturn);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();

                                }else {
                                    Log.d("TAG", "onResponse: "+ClientQuery.changeProfileMutation(user));
                                    Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
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
    private synchronized void changePassApi() {
        String oldPass = oldPassEdit.getText()+"";
        String newPass = newPassEdit.getText()+"";
        if(!newPass.isEmpty()&&!newPass.equals(confirmNewPassEdit.getText().toString())){
            Toast.makeText(getApplicationContext(), "Xác nhận mật khảu mới không giống nhau", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.startLoadingDialog();
        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.changePasswordMutation(oldPass,newPass)),"Bearer "+this.user.getToken())
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        Log.d("ChangePasswordActivity:",res.getResult().toString());
                        if(res.getStatus()==200){
                            User userC = (User) ConverObject.converJsontoObject(res.getResult().getAsJsonObject("change_password"),User.class);
                            Intent intentReturn =new Intent();
                            intentReturn.putExtra("new_token",userC.getToken());
                            setResult(Activity.RESULT_OK,intentReturn);
                            finish();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == UploadFileUtil.READ_IMG_REQUEST){
            if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                UploadFileUtil.openImageGallery(activityResultLauncher,"Select Avatar");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_avatar_txt:{
                UploadFileUtil.onClickRequestPermission(this,activityResultLauncher,"Select Avatar");
                break;
            }
            case R.id.btn_change_password:{
                changePassApi();
                break;
            }
            case R.id.btn_change_profile:{
                changeProfileApi();
                break;
            }
        }
    }
}