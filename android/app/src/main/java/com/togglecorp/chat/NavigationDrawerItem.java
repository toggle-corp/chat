package com.togglecorp.chat;

import android.media.Image;

public class NavigationDrawerItem {
    public static final int LABEL = 0;
    public static final int CONVERSATION = 1;
    public static final int DIVIDER = 2;

    private String mText;
    private int mType;
    private String mPhotoUrl = null;

    NavigationDrawerItem(int type, String text, String photoUrl){
        mType = type;
        mText = text;
        mPhotoUrl = photoUrl;

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
}
