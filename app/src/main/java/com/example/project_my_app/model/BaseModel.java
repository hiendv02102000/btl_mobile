package com.example.project_my_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseModel implements Serializable {
    @SerializedName("created_at")
    protected String createdAt;
    @SerializedName("updated_at")
    protected String  updatedAt;
    @SerializedName("deleted_at")
    protected String  deletedAt;

    public String getCreatedAt() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter inputFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return myFormatObj.format(LocalDateTime.parse(createdAt.substring(0,19),inputFormate).plusHours(7));
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
