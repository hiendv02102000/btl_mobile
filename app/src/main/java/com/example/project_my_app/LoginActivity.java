package com.example.project_my_app;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import com.example.project_my_app.api.APIInterface;
import com.example.project_my_app.api.Request;
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.dialog.LoadingDialog;
import com.example.project_my_app.graphql.Query;
import com.example.project_my_app.model.User;
import com.example.project_my_app.sqlite.SQLiteUser;
import com.example.project_my_app.utils.ConverObject;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnLogin , btnRegister;
    private LoadingDialog loadingDialog = new LoadingDialog(this);
    private SQLiteUser db = new SQLiteUser(LoginActivity.this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.register);
        User user = db.getLast();
        if(user!= null ){
            username.setText(user.getUsername());
            password.setText(user.getPassword());
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRetrofit2Api();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this ,RegisterActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });
    }

    private synchronized void loginRetrofit2Api() {
        loadingDialog.startLoadingDialog();
        btnLogin.setEnabled(false);
        User u = new User(username.getText()+"",password.getText()+"");
        APIInterface.retrofit.anonymousQueryAPI(new Request(Query.loginQuery(u)))
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                       // Log.d("Signin",res+"");
                        if (res != null&&res.getStatus() == 200 ) {
                            User userd = db.getLast();
                            if(userd!= null ){
                                if( !userd.getUsername().equals(u.getUsername() )|| !userd.getPassword().equals(u.getPassword())){
                                    db.addUser(u);
                                }
                            }else {
                                db.addUser(u);
                            }



                            User user = (User) ConverObject.converJsontoObject(res.getResult().getAsJsonObject("login"),User.class);
                            Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            i.putExtra("user",user);
                            startActivity(i);
                        }else {
                            Toast.makeText(getApplicationContext(), "Sai mat khau", Toast.LENGTH_SHORT).show();
                        }
                        btnLogin.setEnabled(true);
                        loadingDialog.dismisDialog();
                    }

                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        call.cancel();
                        btnLogin.setEnabled(true);
                        loadingDialog.dismisDialog();
                        Log.d("Signin","Lỗi kết nối"+t);
                        Toast.makeText(getApplicationContext(), "Lỗi kết nối"+t, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}