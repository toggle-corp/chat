package com.togglecorp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context mContext;
    private List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages){
        mContext = context;
        mMessages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.layout_message, parent, false);

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message msg = mMessages.get(position);
        DatabaseHelper helper = MainActivity.getDatabaseHelper(mContext);
        if (msg.getOP(helper) != null)
            holder.username.setText(msg.getOP(helper).full_name);
        holder.time.setText(msg.getTime());
        holder.messageText.setText(msg.message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
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
