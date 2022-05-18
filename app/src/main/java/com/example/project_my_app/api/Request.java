package com.example.project_my_app.api;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("query")
    public String query;

    public Request(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
