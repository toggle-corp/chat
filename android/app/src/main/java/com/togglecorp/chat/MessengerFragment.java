package com.togglecorp.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessengerFragment extends Fragment {
    Context mContext = null;

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
        return rootView;
    }
}