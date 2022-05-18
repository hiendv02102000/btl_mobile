package com.example.project_my_app.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseAPI implements Serializable {
    @SerializedName("status")
    public int status;
    @SerializedName("result")
    public JsonObject result;
    @SerializedName("error")
    public JsonElement error;
    public ResponseAPI() {
    }

    public int getStatus() {
        return status;
    }

    public JsonObject getResult() {
        return result;
    }



    @Override
    public String toString() {
        return "ResponseAPI{" +
                "status=" + status +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
