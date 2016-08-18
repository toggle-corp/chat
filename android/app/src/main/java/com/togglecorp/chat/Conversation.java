package com.togglecorp.chat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    public long id = -1;
    public String title;

    public static final String TABLE = "conversations";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

    public static List<Conversation> getAll(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE, null, null, null, null, null, null);

        List<Conversation> conversations = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                conversations.add(get(cursor));
            }
        }

        cursor.close();
        return conversations;
    }

    public static Conversation get(Cursor cursor) {
        Conversation conversation = new Conversation();
        conversation.id = cursor.getLong(cursor.getColumnIndex("id"));
        conversation.title = cursor.getString(cursor.getColumnIndex("title"));
        return conversation;
    }

    public static Conversation get(SQLiteOpenHelper helper, long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(true, TABLE, null, "id=?", new String[]{id+""}, null, null, null, null);

        Conversation conversation = null;
        if (cursor.moveToFirst()) {
            conversation = get(cursor);
        }

        cursor.close();
        return conversation;
    }

    public void save(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (id >= 0)
            values.put("id", id);
        values.put("title", title);

        id = (int) db.insertWithOnConflict(TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }


    public static final String REL_TABLE = "conversations_users";
    public static final String CREATE_REL_TABLE = "CREATE TABLE " + REL_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "conversation_id INTEGER, user_id INTEGER)";
    public static final String DROP_REL_TABLE = "DROP TABLE IF EXISTS " + REL_TABLE;

    public void clearUsers(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(REL_TABLE, "conversation_id=?", new String[]{id+""});
    }

    public void addUsers(SQLiteOpenHelper helper, List<Long> users) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("conversation_id", id);

        for (Long userId: users) {
            values.put("user_id", userId);
            db.insertWithOnConflict(REL_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public List<User> getUsers(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(REL_TABLE, new String[]{"user_id"}, "conversation_id=?",
                        new String[] {id+""}, null, null, null);

        List<User> users = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                users.add(User.get(helper, cursor.getLong(cursor.getColumnIndex("user_id"))));
            }
            cursor.moveToNext();
        }

        cursor.close();
        return users;
    }

    public static void deleteAll(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(REL_TABLE, null, null);
        db.delete(TABLE, null, null);
    }

    public static Conversation add(SQLiteOpenHelper helper, JSONObject conversation) throws JSONException {
        Conversation c = new Conversation();
        c.id = conversation.getLong("pk");
        c.title = conversation.getString("title");
        c.save(helper);

        List<Long> userIds = new ArrayList<>();
        JSONArray users = conversation.getJSONArray("users");
        for (int j=0; j<users.length(); ++j)
            userIds.add(users.getLong(j));
        c.addUsers(helper, userIds);

        return c;
    }
}
