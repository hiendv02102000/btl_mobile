package com.example.project_my_app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.project_my_app.R;

public class ConfirmDialog {
    private Activity activity;
    private Dialog dialog;

    public ConfirmDialog(Activity myActivity)
    {
        activity= myActivity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.confirm_delete_dialog);
    }

    public void setYesListener(View.OnClickListener clickListener){
        Button yesBtn = dialog.findViewById(R.id.yes_btn);
        yesBtn.setOnClickListener(clickListener);
    }
    public void setNoListener(View.OnClickListener clickListener){
        Button noBtn = dialog.findViewById(R.id.no_btn);
        noBtn.setOnClickListener(clickListener);
    }
    public void startDialog()
    {
        dialog.show();
    }
    public void dismissDialog()
    {
        dialog.cancel();
    }
}
