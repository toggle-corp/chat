package com.togglecorp.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MessengerFragment extends Fragment {
    Context mContext = null;
    ArrayList<Message> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messenger, container, false);

        final RecyclerView recyclerView;
        final RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_messages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClickable(true);

        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mMessages = new ArrayList<>();

        final MessageAdapter adapter = new MessageAdapter(getActivity(), mMessages);
        recyclerView.setAdapter(adapter);

        this.fetchMessages();

        return rootView;
    }

    // Fetch messages from server
    private void fetchMessages(){
        // TODO: fetch actual messages into mMessages from server
        Message m1 = new Message(), m2 = new Message();
        m1.text = "Hello world this is first message ever";
        m2.text = "And this is the second message";
        mMessages.add(m1);
        mMessages.add(m2);
    }
}