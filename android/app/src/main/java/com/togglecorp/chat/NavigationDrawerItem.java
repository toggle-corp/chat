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

    NavigationDrawerItem(int type, String text, String photoUrl, int id){
        mType = type;
        mText = text;
        mPhotoUrl = photoUrl;
        mId = id;
    }

    NavigationDrawerItem(Drawable icon, int type, String text, int id){
        mType = type;
        mText = text;
        mIcon = icon;
        mId = id;
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
}
