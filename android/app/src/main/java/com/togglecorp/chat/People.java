package com.togglecorp.chat;

import android.media.Image;

/**
 * Created by fhx on 10/20/16.
 */
public class People {
    private String mName;
    private String mExtra;
    private Image mAvatar;

    People(String name, String extra, Image avatar){
        mName = name;
        mExtra = extra;
        mAvatar = avatar;
    }

    public String getName(){
        return mName;
    }

    public String getExtra(){
        return mExtra;
    }
}
