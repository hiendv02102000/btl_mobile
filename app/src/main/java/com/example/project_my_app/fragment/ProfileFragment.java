package com.example.project_my_app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_my_app.ModifyProfileActivity;
import com.example.project_my_app.R;
import com.example.project_my_app.api.APIInterface;
import com.example.project_my_app.api.Request;
import com.example.project_my_app.api.ResponseAPI;
import com.example.project_my_app.graphql.ClientQuery;
import com.example.project_my_app.model.User;
import com.example.project_my_app.utils.ConverObject;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;


public class ProfileFragment extends Fragment {
    public  static  final int MODIFY_PROFILE_CODE = 100;
    private TextView fullNameTxt;
    private TextView emailTxt;
    private ImageView avatar;
    private User user;
    private Button logoutBtn;
    private Button mofifyProfileBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }
    private void init (View view ){
        fullNameTxt=view.findViewById(R.id.user_full_name);
        emailTxt=view.findViewById(R.id.user_email);
        avatar =view.findViewById(R.id.avatar_img);
        logoutBtn = view.findViewById(R.id.logout_btn);
        mofifyProfileBtn = view.findViewById(R.id.modify_profile_btn);
        swipeRefreshLayout = view.findViewById(R.id.refresh_profile_layout);
        initData();
        setUpButton();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });
    }
    private void initData(){
        getProfileApi();
    }
    private synchronized void getProfileApi() {
        APIInterface.retrofit.clientQueryAPI(new Request(ClientQuery.getUserProfileQuery()),"Bearer "+this.user.getToken())
                .enqueue(new Callback<ResponseAPI>() {
                    @Override
                    public void onResponse(Call<ResponseAPI> call, retrofit2.Response<ResponseAPI> response) {
                        ResponseAPI res = response.body();
                        //Log.d("AOTHAT",ClientQuery.getUserProfileQuery());
                        if(res != null && res.getStatus()==200){
                            String token = user.getToken();
                            User userProfile = (User) ConverObject.converJsontoObject(res.getResult().getAsJsonObject("get_user_profile"),User.class);
                            user.setAvatarUrl(userProfile.getAvatarUrl());

                          try {
                              user = (User)userProfile.clone();
                          }catch (Exception ex){

                          }
                          user.setToken(token);
                          updateProfileWithData();
                        }else {
                            //Log.d("AOTHAT",res.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseAPI> call, Throwable t) {
                        call.cancel();
                    }

                });
    }
    private void updateProfileWithData(){
        fullNameTxt.setText(user.getFirstName() +" "+ user.getLastName());
        emailTxt.setText(user.getEmail());
        String avatarURL = user.getAvatarUrl();
        if(avatarURL!=null && !avatarURL.isEmpty())
            Picasso.get().load(avatarURL).into(avatar);
    }
    private void setUpButton(){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mofifyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ModifyProfileActivity.class);
                try {
                 //   Log.d("TAG", "onClick: "+user);
                    i.putExtra("user",(User)user.clone());
                }catch (CloneNotSupportedException ex){

                }
                getActivity().startActivityForResult(i,MODIFY_PROFILE_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ProfileFragment.MODIFY_PROFILE_CODE:{

                if (resultCode== Activity.RESULT_OK){
                    Log.d("TAG", "onActivityResult: "+"OK");
                    if( data.getStringExtra("new_token")!=null){
                        user.setToken(data.getStringExtra("new_token"));

                    }
                    if(data.getSerializableExtra("new_user")!=null){
                            User newU = (User) data.getSerializableExtra("new_user");
                            user.setLastName(newU.getLastName());
                            user.setFirstName(newU.getFirstName());
                            user.setAvatarUrl(newU.getAvatarUrl());
                            updateProfileWithData();
                    }
                }

                break;
            }
        }

    }
}