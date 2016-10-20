package com.togglecorp.chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MessengerFragment extends Fragment {
    private static final String TAG = "Messenger Fragment";
    private String mConversationId;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        final MessageAdapter messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);

        mDatabase = MainActivity.getDatabase();
        if (getArguments() != null)
            mConversationId = getArguments().getString("conversation_id", null);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        startSync();
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

    private void restartSync() {
        stopSync();
        startSync();
    }

    private void startSync() {
        if (mConversationId == null || mConversationId.length() == 0)
            return;

        // The info
        ValueEventListener infoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || !dataSnapshot.exists())
                    return;
                Database.get().getConversation(mConversationId).info
                        = dataSnapshot.getValue(Conversation.Info.class);

                // TODO: Refresh main activity's nav drawer with new info
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        addListener(mDatabase.child("conversations").child(mConversationId).child("info"),
                infoListener);

        // The messages
        ValueEventListener messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || !dataSnapshot.exists())
                    return;
                Conversation conversation = Database.get().getConversation(mConversationId);
                for (DataSnapshot message: dataSnapshot.getChildren()) {
                    conversation.messages.put(message.getKey(),
                            message.getValue(Message.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        addListener(mDatabase.child("conversations").child(mConversationId)
                        .child("messages").orderByChild("time_sent").limitToLast(20),
                messagesListener);
    }
    private void stopSync() {
        for (Map.Entry<Query, ValueEventListener> listener: mListeners.entrySet()) {
            listener.getKey().removeEventListener(listener.getValue());
        }
        mListeners.clear();
    }
}
