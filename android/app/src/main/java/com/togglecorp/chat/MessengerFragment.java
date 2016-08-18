package com.togglecorp.chat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MessengerFragment extends Fragment {
    List<Message> mMessages;
    MessageAdapter mAdapter;
    long mConversationId = 1;   // Make sure you add this in admin in web.
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_messenger, container, false);

        final RecyclerView.LayoutManager layoutManager;

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_messages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);

        layoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mMessages = new ArrayList<>();

        mAdapter = new MessageAdapter(getActivity(), mMessages);
        mRecyclerView.setAdapter(mAdapter);


        // Scroll to bottom when it is resized.
        // TODO: Change this to scroll to previously shown item instead of bottom.
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                    }
                }, 100);
            }
        });

        // Send message.
        Button sendButton = (Button) rootView.findViewById(R.id.send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editMessage = (EditText) rootView.findViewById(R.id.edit_message);
                String message = editMessage.getText().toString().trim();
                if (message.length() > 0) {
                    sendMessage(message);
                }
                editMessage.setText("");
            }
        });

        downloadMessages();
        fetchMessages();
        return rootView;
    }

    // Fetch messages from server.
    private void downloadMessages() {
        new Client.NetworkHandler(new Client.Listener() {
            @Override
            public void process() {
                // First download messages from server to local database.
                try {
                    Client client = new Client(getContext());
                    client.getMessages(mConversationId, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void post() {
                fetchMessages();
            }
        }).execute();
    }

    // Fetch messages from local database.
    private void fetchMessages() {
        // Next fetch messages from local database.
        List<Message> messages = Message.getAll(
                MainActivity.getDatabaseHelper(getContext())
        );
        mMessages.clear();
        for (Message m: messages)
            mMessages.add(m);
        mAdapter.notifyDataSetChanged();

        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    // Send message.
    private void sendMessage(final String message) {
        new Client.NetworkHandler(new Client.Listener() {
            @Override
            public void process() {
                // First download messages from server to local database.
                try {
                    Client client = new Client(getContext());
                    client.addMessage(mConversationId, message, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void post() {
                fetchMessages();
            }
        }).execute();
    }

    public static final String BROADCAST_INTENT = "TOGGLE:CHAT_MESSAGE_RECEIVED";

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mMessageReceiver, new IntentFilter(BROADCAST_INTENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);

            downloadMessages();
        }
    };
}