package com.example.project_my_app.dialog;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.project_my_app.R;


public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity myactivity)
    {
        activity= myactivity;
    }

    public void startLoadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void dismisDialog()
    {
        alertDialog.dismiss();
    }
}
