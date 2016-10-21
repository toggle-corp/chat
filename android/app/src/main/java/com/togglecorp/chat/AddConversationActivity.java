package com.togglecorp.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class AddConversationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private AuthUser mAuthUser;
    private PeopleAdapter mPeopleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversation);

        // Firebase database
        mDatabase = MainActivity.getDatabase();
        // Get logged in user or start Login Activity
        mAuthUser = new AuthUser(this);
        if (mAuthUser.getFbUser() == null) {
            // Not signed in, launch the Log In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Conversation");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Add Conversation");
        }

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.peoples);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPeopleAdapter = new PeopleAdapter(this, Database.get().users.entrySet());
        recyclerView.setAdapter(mPeopleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        } else if (id == R.id.action_add_conversation) {
            addConversation();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addConversation() {
        if (mPeopleAdapter == null || mPeopleAdapter.getSelected().size() < 1)
            return;

        EditText title = (EditText) findViewById(R.id.title);
        String userId = mAuthUser.getFbUser().getUid();

        Conversation.Info info = new Conversation.Info();
        info.title = title.getText().toString();
        info.users.add(userId);
        for (String selected: mPeopleAdapter.getSelected()) {
            info.users.add(selected);
        }

        DatabaseReference conversation = mDatabase.child("conversations").push();
        conversation.child("info").setValue(info);

        mDatabase.child("users").child(userId).child("conversations")
                .child(conversation.getKey()).setValue(info.title);
        mDatabase.child("users").child(userId).child("active_conversation")
                .setValue(conversation.getKey());

        for (String selected: mPeopleAdapter.getSelected()) {
            mDatabase.child("users").child(selected).child("conversations")
                    .child(conversation.getKey()).setValue(info.title);
        }
    }


}
