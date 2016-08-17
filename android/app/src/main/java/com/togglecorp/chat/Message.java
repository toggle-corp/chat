package com.togglecorp.chat;

public class Message {
    private int mId;
    private int mTimeStamp;
    private String mText;
    private int mStatus;
    private int mOP;    // OP = Original Poster, message jasley post gareko ho tyo chai original
                        // poster ho
                        // note for Dahal: OP = posted_by
    private int mConversationID;
}
