package com.togglecorp.chat;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
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

    // Has the fragment started?
    private boolean mStarted = false;

    // Active conversation, list of its messages and their adapter
    private String mConversationId = null;
    private List<Message> mMessages = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    // Recycler view, its layout manager and the boolean variable for whether or
    // not to scroll the messages when new messages appear.
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    boolean mScrollToEnd = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        mRecyclerView = (RecyclerView)root.findViewById(R.id.messages);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMessageAdapter = new MessageAdapter();
        mMessageAdapter.setMessages(mMessages);
        mRecyclerView.setAdapter(mMessageAdapter);

        mDatabase = MainActivity.getDatabase();
        mLayoutManager.setStackFromEnd(true);

        // Send message button listener
        root.findViewById(R.id.send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        return root;
    }

    private void refreshMessages() {
        // If we have no active conversation, do not show the message box
        if (mConversationId == null || mConversationId.length() == 0) {
            getActivity().findViewById(R.id.message_box).setVisibility(View.GONE);
            return;
        }

        // Otherwise show the message box
        getActivity().findViewById(R.id.message_box).setVisibility(View.VISIBLE);

        // Get the active conversation and show the activity title accordingly
        Conversation conversation = Database.get().getConversation(mConversationId);
        getActivity().setTitle(conversation.info.getTitle().toUpperCase());

        // If the last item is currently visible, that means when new messages
        // are received, we should still scroll to end.
        if (mLayoutManager.findLastVisibleItemPosition() == mMessages.size()-1)
            mScrollToEnd = true;

        // Get all messages
        mMessages.clear();
        for (HashMap.Entry<String, Message> entry: conversation.messages.entrySet()) {
            mMessages.add(entry.getValue());
        }
        // And sort them
        Collections.sort(mMessages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return Long.valueOf((long)m1.time_sent).compareTo((long)m2.time_sent);
            }
        });
        // And notify the adapter to refresh
        mMessageAdapter.notifyDataSetChanged();

        // If needed, scroll to the last message
        if (mScrollToEnd)
            mRecyclerView.scrollToPosition(mMessages.size()-1);
        mScrollToEnd = false;
    }

    private void sendMessage() {
        if (mConversationId == null || mConversationId.length() == 0)
            return;

        // Get the message text, clear the text box and create and send new message
        EditText editMessage = (EditText)getActivity().findViewById(R.id.edit_message);
        String text = editMessage.getText().toString();
        editMessage.setText("");

        if (text.length() > 0) {
            Message message = new Message();
            message.text = text;
            message.sender = Database.get().selfId;
            message.time_sent = ServerValue.TIMESTAMP;  // System.currentTimeMillis();
            message.status = 0;

            DatabaseReference newMessage = mDatabase.child("conversations")
                    .child(mConversationId).child("messages").push();
            newMessage.setValue(message);
            mScrollToEnd = true;

            // Push notification to every recipient
            pushNotification(message);
        }
    }

    private void pushNotification(Message message) {
        Conversation conversation = Database.get().getConversation(mConversationId);
        List<String> tokens = new ArrayList<>();

        // Get tokens of all recipients
        for (String userId: conversation.info.users) {
            if (Database.get().users.containsKey(userId)) {
                User user = Database.get().users.get(userId);
                tokens.addAll(user.getTokens());
            }
        }

        // Send the notification to all the tokens
        new FCMServer.PushNotificationTask(Database.get().self.displayName,
                    message.text, mConversationId, tokens).execute();
    }

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
        mScrollToEnd = true;
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



    // FCM broadcast receiver.
    public static final String BROADCAST_INTENT = "TOGGLE:CHAT_MESSAGE_RECEIVED";

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter(BROADCAST_INTENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("conversation").equals(mConversationId)) {
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
            }
        }
    };

}
