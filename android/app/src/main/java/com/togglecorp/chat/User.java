package com.togglecorp.chat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    public long id = -1;
    public String username;
    public String full_name;

    public static final String TABLE = "users";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT," +
            "full_name TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

    public static List<User> getAll(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE, null, null, null, null, null, null);

        List<User> users = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                users.add(get(cursor));
            }
            cursor.moveToNext();
        }

        cursor.close();
        return users;
    }

    public static User get(Cursor cursor) {
        User user = new User();
        user.id = cursor.getLong(cursor.getColumnIndex("id"));
        user.username = cursor.getString(cursor.getColumnIndex("username"));
        user.full_name = cursor.getString(cursor.getColumnIndex("full_name"));
        return user;
    }

    public static User get(SQLiteOpenHelper helper, long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(true, TABLE, null, "id=?", new String[]{id+""}, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = get(cursor);
        }

        cursor.close();
        return user;
    }

    public void save(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (id >= 0)
            values.put("id", id);
        values.put("username", username);
        values.put("full_name", full_name);

        id = (int) db.insertWithOnConflict(TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void deleteAll(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE, null, null);
    }

    public static User add(SQLiteOpenHelper helper, JSONObject user) throws JSONException {
        User u = new User();
        u.id = user.getLong("pk");
        u.username = user.getString("username");
        u.full_name = user.getString("full_name");
        u.save(helper);
        return u;
    }
}
