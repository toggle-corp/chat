package com.togglecorp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context mContext;
    private ArrayList<Message> mMessages;

    public MessageAdapter(Context context){
        mContext = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        protected TextView username;
        protected TextView time;
        protected TextView messageText;

        MessageViewHolder(View v){
            super(v);
            username = (TextView)v.findViewById(R.id.textview_username);
            time = (TextView)v.findViewById(R.id.textview_time);
            messageText = (TextView)v.findViewById(R.id.textview_messageText);
        }
    }
}
