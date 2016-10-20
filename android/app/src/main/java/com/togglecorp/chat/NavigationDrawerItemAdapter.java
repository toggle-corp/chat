package com.togglecorp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NavigationDrawerItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NavigationDrawerItem> mItems;
    private Context mContext;

    NavigationDrawerItemAdapter(Context context, List<NavigationDrawerItem> items){
        mContext = context;
        mItems = items;
    }

    public void setItems(List<NavigationDrawerItem> items){
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case NavigationDrawerItem.CONVERSATION:
                holder = new NavigationDrawerItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_navigation_drawer_item, parent, false));
                break;
            case NavigationDrawerItem.LABEL:
                holder = new NavigationDrawerLabelViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_navigation_drawer_label, parent, false));
                break;
            case NavigationDrawerItem.DIVIDER:
                holder = new NavigationDrawerDividerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_navigation_drawer_divider, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return (mItems.get(position)).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NavigationDrawerItem item = mItems.get(position);
        switch (item.getType()){
            case NavigationDrawerItem.CONVERSATION:
                NavigationDrawerItemViewHolder viewHolder = (NavigationDrawerItemViewHolder)holder;
                viewHolder.text.setText(item.getText());
                if (item.getPhotoUrl() != null)
                    Picasso.with(mContext).load(item.getPhotoUrl()).into(viewHolder.icon);
                break;
            case NavigationDrawerItem.LABEL:
                ((NavigationDrawerLabelViewHolder)holder).text.setText(item.getText());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class NavigationDrawerItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView text;
        protected ImageView icon;

        NavigationDrawerItemViewHolder(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.text);
            icon = (ImageView)v.findViewById(R.id.icon);
        }
    }

    class NavigationDrawerLabelViewHolder extends RecyclerView.ViewHolder{
        protected TextView text;

        NavigationDrawerLabelViewHolder(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.text);
        }
    }

    class NavigationDrawerDividerViewHolder extends RecyclerView.ViewHolder{
        NavigationDrawerDividerViewHolder(View v){
            super(v);
        }
    }
}
