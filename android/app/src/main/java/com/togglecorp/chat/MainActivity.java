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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private static FirebaseDatabase mFbDb;
    public static DatabaseReference getDatabase() { return mFbDb.getReference(); }

    private AuthUser mAuthUser;
    private DatabaseReference mDatabase;

    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mNavAdapter;

    private ArrayList<NavigationDrawerItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mFbDb == null) {
            mFbDb = FirebaseDatabase.getInstance();
            mFbDb.setPersistenceEnabled(true);
        }
        mDatabase = getDatabase();

        // Get logged in user or start Login Activity
        mAuthUser = new AuthUser(this);
        if (mAuthUser.getFbUser() == null) {
            // Not signed in, launch the Log In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize the nav drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_content, new MessengerFragment())
                .commit();

        mNavAdapter = new NavigationDrawerAdapter(this, mNavItems, mNavListener);
        RecyclerView recyclerView = (RecyclerView) navView
                .findViewById(R.id.navigation_drawer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mNavAdapter);


        // Set the nav drawer header
        View header = navView.findViewById(R.id.navigation_drawer_header);
        ((TextView)header.findViewById(R.id.username)).setText(mAuthUser.getFbUser().getDisplayName());
        ((TextView)header.findViewById(R.id.extra)).setText(mAuthUser.getFbUser().getEmail());
        Picasso.with(this)
                .load(mAuthUser.getFbUser().getPhotoUrl())
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
        ActionBarDrawerToggle drawerListener = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        mDrawerLayout.addDrawerListener(drawerListener);
        drawerListener.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private final int ADD_NEW_CONVERSATION = -100;
    private void refreshNavItems() {
        mNavItems.clear();

        mNavItems.add(new NavigationDrawerItem(NavigationDrawerItem.LABEL,
                "Conversations", null, 0));
        mNavItems.add(new NavigationDrawerItem(
                getDrawable(R.drawable.ic_add), NavigationDrawerItem.ITEM,
                "Add new", ADD_NEW_CONVERSATION));

        Database db = Database.get();


//        mNavItems.add(new NavigationDrawerItem(NavigationDrawerItem.DIVIDER, null, null));
//        mNavItems.add(new NavigationDrawerItem(
//                NavigationDrawerItem.ITEM, "Test Conversation A", null));
//        mNavItems.add(new NavigationDrawerItem(
//                NavigationDrawerItem.ITEM, "Test Conversation B", null));
        mNavAdapter.notifyDataSetChanged();
    }

    private NavigationListener mNavListener = new NavigationListener() {
        @Override
        public void onClick(int id) {
            if (id == ADD_NEW_CONVERSATION) {
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, AddConversationActivity.class));
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        startSync();
        refreshNavItems();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSync();
    }

    private Map<Query, ValueEventListener> mListeners = new HashMap<>();
    private void addListener(Query ref, ValueEventListener listener) {
        ref.addValueEventListener(listener);
        mListeners.put(ref, listener);
    }

    private void startSync() {
        // Store self
        mDatabase.child("users").child(mAuthUser.getFbUser().getUid())
                .setValue(mAuthUser.getUser());

        // Fetch others
        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {

                        // All except self
                        if (!child.getKey().equals(mAuthUser.getFbUser().getUid()))
                            Database.get().users.put(child.getKey(), child.getValue(User.class));

                        // For self, read conversations (or at least the titles)
                        else {
                            for (DataSnapshot conversation:
                                    child.child("conversations").getChildren()) {
                                Database.get().getConversation(conversation.getKey()).info.title =
                                        conversation.getValue(String.class);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        addListener(mDatabase.child("users"), usersListener);
    }

    private void stopSync() {
        for (Map.Entry<Query, ValueEventListener> listener: mListeners.entrySet()) {
            listener.getKey().removeEventListener(listener.getValue());
        }
        mListeners.clear();
    }
}
