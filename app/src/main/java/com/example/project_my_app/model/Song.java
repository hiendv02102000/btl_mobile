package com.example.project_my_app.model;

import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song extends BaseModel implements Serializable,Comparable<Song>{
//    ID         int     `gorm:"column:id;primary_key;auto_increment;not null"`
//    Title      string  `gorm:"column:title;"`
//    Singer     string  `gorm:"column:singer;"`
//    ContentURL *string `gorm:"column:content_url"`
//    View int     `gorm:"column:view"`
//    UserID     int     `gorm:"column:user_id;"`
//    ImageURL   *string `gorm:"column:image_url"`
//    Decription string  `gorm:"column:decription"`
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("singer")
    private String singer;
    @SerializedName("content_url")
    private  String contentUrl;
    @SerializedName("view")
    private int views;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("decription")
    private String description;
    @SerializedName("user")
    private User user;
    public Song() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Song o) {
        if(this.views>o.views) return -1;
        if(this.views==o.views) return 0;
        return 1;
    }
}
