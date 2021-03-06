package com.example.project_my_app.api;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    APIInterface retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")//https://be-soc.herokuapp.com/http://10.0.2.2:8080/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface.class);
    @POST("app/query")
    public Call<ResponseAPI> anonymousQueryAPI(@Body Request request);
    @POST("app/client/query")
    public Call<ResponseAPI> clientQueryAPI(@Body Request request,
                                            @Header("Authorization") String authHeader);
    @Multipart
    @POST("app/client/query")
    public Call<ResponseAPI> clientQueryAPIWithFile(@Part("query")RequestBody query,
                                                    @Header("Authorization") String authHeader,
                                                    @Part MultipartBody.Part ...file);
}
