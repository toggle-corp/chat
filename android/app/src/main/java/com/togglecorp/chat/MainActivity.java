package com.togglecorp.chat;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static DatabaseHelper mDatabaseHelper;
    public static DatabaseHelper getDatabaseHelper(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DatabaseHelper(context);
        return mDatabaseHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Clear all messages, except last few.
        Message.deleteExcept(getDatabaseHelper(this), 20);

        // Send Fcm Token to server.
        FBInstanceIDService.sendTokenToServer(this);

        new Client.NetworkHandler(new Client.Listener() {
            @Override
            public void process() {
                // First load all conversations and users.
                try {
                    Client client = new Client(MainActivity.this);
                    client.getUsers();
                    client.getConversations();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void post() {
                // Then show the messenger fragment.
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_content, new MessengerFragment()).commitAllowingStateLoss();
            }
        }).execute();
    }
}
