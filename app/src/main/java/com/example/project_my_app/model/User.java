package com.example.project_my_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

public class User extends BaseModel implements Serializable {
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
    @SerializedName("id")
    private int id;
    @SerializedName("token_expired_at")
    private String tokenExpiredAt;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
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
}
