package com.togglecorp.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private User mUser;
    private static FirebaseDatabase mFDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mFDb == null) {
            mFDb = FirebaseDatabase.getInstance();
            mFDb.setPersistenceEnabled(true);
        }

        // Get logged in user or start Login Activity
        mUser = new User(this);
        if (mUser.getUser() == null) {
            // Not signed in, launch the Log In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }
}
