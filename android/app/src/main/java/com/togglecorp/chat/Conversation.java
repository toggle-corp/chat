package com.togglecorp.chat;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    public static class Info {
        public String title;
        public List<String> users = new ArrayList<>();

        public Info() {}
    }

    public static class Message {
        public String sender;
        public String message;
        public long time_sent;
        public long time_delivered;
        public int status;

        public Message() {};
    }

    public Info info;
    public List<Message> messages = new ArrayList<>();
}
