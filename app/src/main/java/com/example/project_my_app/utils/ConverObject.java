package com.example.project_my_app.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ConverObject {
    public static Object converJsontoObject(JsonObject jsonObject, Class classObject) {
        Gson gson = new Gson();
        return  gson.fromJson(jsonObject.toString(),classObject);
    }
    public static ArrayList converJsontoArray(JsonArray jsonArray,Class classObject){
        Gson gson = new Gson();
        return  (ArrayList) gson.fromJson(jsonArray.toString(),classObject);
    }
}
