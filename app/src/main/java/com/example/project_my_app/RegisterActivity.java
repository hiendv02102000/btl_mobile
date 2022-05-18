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

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName,lastName,username,password,confirmPassword;
    private Button signUp;
    private LoadingDialog loadingDialog = new LoadingDialog(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signUp = (Button) findViewById(R.id.sign_up_btn);
        confirmPassword = findViewById(R.id.confirmPassword);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRetrofit2Api();
            }
        });
    }


    private synchronized void loginRetrofit2Api() {
        loadingDialog.startLoadingDialog();
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
            return;
        }
            User u = new User(username.getText().toString(),password.getText().toString());
            u.setFirstName(firstName.getText().toString());
            u.setLastName(lastName.getText().toString());
            APIInterface.retrofit.anonymousQueryAPI(new Request(Query.signUpQuery(u)))
                    .enqueue(new Callback<ResponseAPI>() {
                        @Override
                        public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                            ResponseAPI res = response.body();
                            Log.d("Status123",res.toString());
                            if (res.status == 200 ) {
                                Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismisDialog();
                        }

                        @Override
                        public void onFailure(Call<ResponseAPI> call, Throwable t) {
                            call.cancel();loadingDialog.dismisDialog();
                        }

                    });
    }

}
