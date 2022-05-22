package com.example.project_my_app.model;

import com.google.gson.annotations.SerializedName;

public class Comment extends BaseModel{
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private User user;
    @SerializedName("content")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
