package com.togglecorp.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by fhx on 10/20/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessages;

    MessageAdapter(){
        mMessages = new ArrayList<>();
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Frozen Helium", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));
        mMessages.add(new Message("asdf asdf asdf asdfefwdfw wdefwdf dwf", "Aditya Khatri", "12:23"));
        mMessages.add(new Message("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla", "Bibek Dahal", "12:24"));


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
        holder.sender.setText(current.getSender());
        holder.time.setText(current.getTime());
        holder.text.setText(current.getText());
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
