package com.togglecorp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NavigationDrawerItem> mItems;
    private Context mContext;
    private NavigationListener mListener;

    NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> items,
                            NavigationListener listener){
        mContext = context;
        mItems = items;
        mListener = listener;
    }

//    public void setItems(List<NavigationDrawerItem> items){
//        mItems = items;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case NavigationDrawerItem.ITEM:
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
            case NavigationDrawerItem.ITEM:
                NavigationDrawerItemViewHolder viewHolder = (NavigationDrawerItemViewHolder)holder;
                viewHolder.text.setText(item.getText());
                if (item.getPhotoUrl() != null)
                    Picasso.with(mContext).load(item.getPhotoUrl()).into(viewHolder.icon);
                else if (item.getIcon() != null)
                    viewHolder.icon.setImageDrawable(item.getIcon());
                viewHolder.id = item.getId();
                break;
            case NavigationDrawerItem.LABEL:
                ((NavigationDrawerLabelViewHolder)holder).text.setText(item.getText());
                ((NavigationDrawerLabelViewHolder)holder).id = item.getId();
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
        protected int id;

        NavigationDrawerItemViewHolder(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.text);
            icon = (ImageView)v.findViewById(R.id.icon);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(id);
                }
            });
        }
    }

    class NavigationDrawerLabelViewHolder extends RecyclerView.ViewHolder{
        protected TextView text;
        protected int id;

        NavigationDrawerLabelViewHolder(View v){
            super(v);
            text = (TextView)v.findViewById(R.id.text);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(id);
                }
            });
        }
    }

    class NavigationDrawerDividerViewHolder extends RecyclerView.ViewHolder{
        protected int id;
        NavigationDrawerDividerViewHolder(View v){
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(id);
                }
            });
        }
    }
}
