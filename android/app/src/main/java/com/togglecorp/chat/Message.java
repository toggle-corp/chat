package com.togglecorp.chat;

/**
 * Created by fhx on 10/20/16.
 */
public class Message {
    private String mText;
    private String mSender;
    private String mTime;

    Message(String text, String sender, String time){
        mText = text;
        mSender = sender;
        mTime = time;
    }

    public String getText(){
        return mText;
    }

    public String getSender(){
        return mSender;
    }

    public String getTime(){
        return mTime;
    }
}
