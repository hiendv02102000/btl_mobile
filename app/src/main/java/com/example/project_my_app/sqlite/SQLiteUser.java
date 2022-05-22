package com.example.project_my_app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.project_my_app.model.Song;
import com.example.project_my_app.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteUser {
    private SQLiteHelper db ;

    public SQLiteUser(Context context) {
        db = new SQLiteHelper(context);
    }
    public User getLast() {
        SQLiteDatabase st = db.getReadableDatabase();
        String order = "id DESC";

        Cursor rs = st.query("users", null, null,
                null, null, null, order);
        while (rs != null && rs.moveToNext()) {
            String username = rs.getString(1);
            String password = rs.getString(2);
            return new User(username,password);
        }

        return null;
    }
    public long addUser(User user){
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        SQLiteDatabase st = db.getWritableDatabase();
        return st.insert("users", null, values);
    }

}
