package com.togglecorp.chat;

import android.graphics.drawable.Drawable;

public class NavigationDrawerItem {
    public static final int LABEL = 0;
    public static final int ITEM = 1;
    public static final int DIVIDER = 2;

    private String mText;
    private int mType;
    private String mPhotoUrl = null;
    private Drawable mIcon = null;
    private int mId;
    private boolean mSelected;

    NavigationDrawerItem(int type, String text, String photoUrl, int id, boolean selected){
        mType = type;
        mText = text;
        mPhotoUrl = photoUrl;
        mId = id;
        mSelected = selected;
    }

    NavigationDrawerItem(Drawable icon, int type, String text, int id, boolean selected){
        mType = type;
        mText = text;
        mIcon = icon;
        mId = id;
        mSelected = selected;
    }

    public String getText(){
        return mText;
    }
    public int getType(){
        return mType;
    }
    public String getPhotoUrl() {
        return mPhotoUrl;
    }
    public Drawable getIcon() { return mIcon; }
    public int getId() { return mId; }
    public boolean isSelected() { return mSelected; }
}
