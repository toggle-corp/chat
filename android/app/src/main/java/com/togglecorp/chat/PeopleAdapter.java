package com.togglecorp.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fhx on 10/20/16.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<User> mUsers;

    PeopleAdapter(List<User> users){
        mUsers = users;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_people, parent, false));
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        User current = mUsers.get(position);
        holder.name.setText(current.displayName);
        holder.extra.setText(current.email);
        // TODO: set avatar
        //holder.avatar.setImageURI(current.photoUrl);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView extra;
        protected ImageView avatar;

        PeopleViewHolder(View v){
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            extra = (TextView)v.findViewById(R.id.extra);
            avatar = (ImageView)v.findViewById(R.id.avatar);
        }
    }
}
