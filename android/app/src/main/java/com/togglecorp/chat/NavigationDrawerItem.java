package com.togglecorp.chat;

import android.media.Image;

/**
 * Created by fhx on 10/19/16.
 */
public class NavigationDrawerItem {
    public static final int LABEL = 0;
    public static final int CONVERSATION = 1;
    public static final int DIVIDER = 2;

    private String mText;
    private Image mIcon;
    private int mType;

    NavigationDrawerItem(int type, String text, Image icon){
        mType = type;
        mText = text;
        mIcon = icon;

    }

    public String getText(){
        return mText;
    }

    public int getType(){
        return mType;
    }
}
