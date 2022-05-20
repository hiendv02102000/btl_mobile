package com.example.project_my_app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;

public class UploadFileUtil {
    public  static final  int READ_IMG_REQUEST =1000;
    public static void openImageGallery(ActivityResultLauncher<Intent> activityResultLauncher, String action) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,action));
    }
    public static void onClickRequestPermission(Activity activity,ActivityResultLauncher<Intent> activityResultLauncher, String action){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openImageGallery(activityResultLauncher,action);
            return;
        }
        if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openImageGallery(activityResultLauncher,action);
        }else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            activity.requestPermissions(permission,READ_IMG_REQUEST);
        }
    }
}
