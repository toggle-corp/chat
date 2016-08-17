package com.togglecorp.chat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Message {
    public long id;
    public long posted_at;
    public long posted_by;
    public String message;
    public long conversation_id;

    public static final String TABLE = "messages";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "posted_at INTEGER," +
            "posted_by INTEGER," +
            "message TEXT," +
            "conversation_id INTEGER)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

    public static List<Message> getAll(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE, null, null, null, null, null, null);

        List<Message> messages = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                messages.add(get(cursor));
            }
        }

        cursor.close();
        return messages;
    }

    public static Message get(Cursor cursor) {
        Message message = new Message();
        message.id = cursor.getLong(cursor.getColumnIndex("id"));
        message.posted_at = cursor.getLong(cursor.getColumnIndex("posted_at"));
        message.posted_by = cursor.getLong(cursor.getColumnIndex("posted_by"));
        message.message = cursor.getString(cursor.getColumnIndex("message"));
        message.conversation_id = cursor.getLong(cursor.getColumnIndex("conversation_id"));
        return message;
    }

    public static Message get(SQLiteOpenHelper helper, long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(true, TABLE, null, "id=?", new String[]{id+""}, null, null, null, null);

        Message message = null;
        if (cursor.moveToFirst()) {
            message = get(cursor);
        }

        cursor.close();
        return message;
    }

    public void save(SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (id >= 0)
            values.put("id", id);
        values.put("posted_at", posted_at);
        values.put("posted_by", posted_by);
        values.put("message", message);
        values.put("conversation_id", conversation_id);

        id = (int) db.insertWithOnConflict(TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }
}
