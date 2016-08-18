package com.togglecorp.chat;

public class Message {
    public int id;
    public int timeStamp;
    public String text;
    public int status;
    public int OP;    // OP = Original Poster, message jasley post gareko ho tyo chai original
                        // poster ho
                        // note for Dahal: OP = posted_by
    private int mConversationID;

    public String getOPName(){
        return "Mr. Frozen Helium";
    }

    public String getTime(){
        return "1:00 PM";
    }

    public String getMessageText(){
        return text;
    }
}
