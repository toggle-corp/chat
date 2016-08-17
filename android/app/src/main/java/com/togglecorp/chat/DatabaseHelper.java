package com.togglecorp.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CHAT_DB";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Conversation.CREATE_TABLE);
        db.execSQL(Conversation.CREATE_REL_TABLE);
        db.execSQL(Message.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Message.DROP_TABLE);
        db.execSQL(Conversation.DROP_REL_TABLE);
        db.execSQL(Conversation.DROP_TABLE);
        db.execSQL(User.DROP_TABLE);

        onCreate(db);
    }
}
