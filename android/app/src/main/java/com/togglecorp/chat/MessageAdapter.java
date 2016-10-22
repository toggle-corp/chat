package com.togglecorp.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessages = new ArrayList<>();

    MessageAdapter() {
    }

    public void setMessages(List<Message> messages){
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message current = mMessages.get(position);
        if (Database.get().selfId.equals(current.sender))
            holder.sender.setText("Me");
        else if (Database.get().users.containsKey(current.sender))
            holder.sender.setText(Database.get().users.get(current.sender).displayName);
        else
            holder.sender.setText("Unknown");
        holder.time.setText(new Date((long)current.time_sent).toString());
        holder.text.setText(current.text);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        protected TextView sender;
        protected TextView time;
        protected TextView text;

        MessageViewHolder(View v){
            super(v);

            sender = (TextView)v.findViewById(R.id.sender);
            time = (TextView)v.findViewById(R.id.time);
            text = (TextView)v.findViewById(R.id.text);
        }
    }
}
