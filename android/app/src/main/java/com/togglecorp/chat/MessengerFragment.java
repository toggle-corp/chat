package com.togglecorp.chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerFragment extends Fragment {
    private static final String TAG = "MessengerFragment";

    private DatabaseReference mDatabase;

    private String mConversationId = null;
    private boolean mStarted = false;
    private List<Message> mMessages = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mMessageAdapter = new MessageAdapter();
        mMessageAdapter.setMessages(mMessages);
        recyclerView.setAdapter(mMessageAdapter);

        mDatabase = MainActivity.getDatabase();
        layoutManager.setStackFromEnd(true);
        return root;
    }

    private void refreshMessages() {
        if (mConversationId == null || mConversationId.length() == 0) {
            getActivity().findViewById(R.id.message_box).setVisibility(View.GONE);
            return;
        }
        getActivity().findViewById(R.id.message_box).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.send_message).setOnClickListener(mSendListener);

        Conversation conversation = Database.get().getConversation(mConversationId);
        getActivity().setTitle(conversation.info.getTitle());

        mMessages.clear();
        for (HashMap.Entry<String, Message> entry: conversation.messages.entrySet()) {
            mMessages.add(entry.getValue());
        }

        Collections.sort(mMessages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return Long.valueOf(m1.time_sent).compareTo(Long.valueOf(m2.time_sent));
            }
        });
        mMessageAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mConversationId == null || mConversationId.length() == 0)
                return;

            EditText editMessage = (EditText)getActivity().findViewById(R.id.edit_message);
            String text = editMessage.getText().toString();
            editMessage.setText("");
            if (text.length() > 0) {
                Message message = new Message();
                message.text = text;
                message.sender = Database.get().selfId;
                message.time_sent = System.currentTimeMillis();
                message.time_delivered = 0;
                message.status = 0;

                DatabaseReference newMessage = mDatabase.child("conversations")
                        .child(mConversationId).child("messages").push();
                newMessage.setValue(message);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        refreshMessages();

        startSync();
        mStarted = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSync();
    }

    public void setConversation(String id) {
        if (mConversationId != null && mConversationId.equals(id))
            return;

        mConversationId = id;
        if (mStarted) {
            restartSync();
            refreshMessages();
        }
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

                getActivity().setTitle(Database.get().getConversation(mConversationId)
                        .info.getTitle());
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
                    if (message.child("time_delivered").getValue(Long.class) == 0)
                        message.child("time_delivered").getRef()
                                .setValue(System.currentTimeMillis());

                    conversation.messages.put(message.getKey(),
                            message.getValue(Message.class));

                }
                refreshMessages();
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
