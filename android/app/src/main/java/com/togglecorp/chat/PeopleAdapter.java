package com.togglecorp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<Map.Entry<String, User>> mUsers = new ArrayList<>();
    private List<String> mSelected = new ArrayList<>();

    private Context mContext;

    PeopleAdapter(Context context,  Set<Map.Entry<String, User>> users){
        mContext = context;
        mUsers.clear();
        mUsers.addAll(users);
    }

    public List<String> getSelected() {
        return mSelected;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_people, parent, false));
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        User current = mUsers.get(position).getValue();
        holder.name.setText(current.displayName);
        holder.extra.setText(current.email);
        holder.id = mUsers.get(position).getKey();
        Picasso.with(mContext).load(current.photoUrl).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView extra;
        protected CircleImageView avatar;
        protected String id;

        PeopleViewHolder(View v){
            super(v);

            name = (TextView)v.findViewById(R.id.name);
            extra = (TextView)v.findViewById(R.id.extra);
            avatar = (CircleImageView)v.findViewById(R.id.avatar);

            v.setClickable(true);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected())
                        mSelected.remove(id);
                    else
                        mSelected.add(id);

                    view.setSelected(!view.isSelected());
                }
            });
        }

    }
}
