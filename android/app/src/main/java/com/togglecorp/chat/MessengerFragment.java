package com.togglecorp.chat;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fhx on 10/20/16.
 */
public class MessengerFragment extends Fragment {
    private static final String TAG = "Messenger Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        final MessageAdapter messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);

        return root;
    }
}
