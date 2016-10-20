package com.togglecorp.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private User mUser;
    private static FirebaseDatabase mFDb;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


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

        // Initialize the nav drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ArrayList<NavigationDrawerItem> navigationDrawerItems = new ArrayList<>();
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.LABEL, "Conversations", null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.CONVERSATION, "Test Conversation 1", null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.CONVERSATION, "Test Conversation 2", null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.CONVERSATION, "Test Conversation 3", null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.DIVIDER, null, null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.CONVERSATION, "Test Conversation A", null));
        navigationDrawerItems.add(new NavigationDrawerItem(NavigationDrawerItem.CONVERSATION, "Test Conversation B", null));
        final NavigationDrawerItemAdapter navigationDrawerItemAdapter = new NavigationDrawerItemAdapter(navigationDrawerItems);

        RecyclerView recyclerView = (RecyclerView) mNavigationView
                .findViewById(R.id.navigation_drawer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(navigationDrawerItemAdapter);

        // Set the nav drawer header
        View header = mNavigationView.findViewById(R.id.navigation_drawer_header);
        ((TextView)header.findViewById(R.id.username)).setText(mUser.getUser().getDisplayName());
        ((TextView)header.findViewById(R.id.extra)).setText(mUser.getUser().getEmail());
        Picasso.with(this)
                .load(mUser.getUser().getPhotoUrl())
                .into((CircleImageView)header.findViewById(R.id.avatar));

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // The hamburger icon
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mDrawerLayout.closeDrawers();
            return true;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
