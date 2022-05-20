package com.example.project_my_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

public class User extends BaseModel implements Serializable, Cloneable{
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private  String username;
    @SerializedName("password")
    private  String password;
    @SerializedName("email")
    private String email;
    @SerializedName("token")
    private String token;
    @SerializedName("role")
    private String role;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("token_expired_at")
    private String tokenExpiredAt;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenExpiredAt() {
        return tokenExpiredAt;
    }

    public void setTokenExpiredAt(String tokenExpiredAt) {
        this.tokenExpiredAt = tokenExpiredAt;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
