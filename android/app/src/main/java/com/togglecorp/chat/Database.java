package com.togglecorp.chat;

import java.util.HashMap;

public class Database {
    private Database() {}
    private static Database mDatabase;

    public static Database get() {
        if (mDatabase == null)
            mDatabase = new Database();
        return mDatabase;
    }

    public HashMap<String, User> users = new HashMap<>();
    public HashMap<String, Conversation> conversations = new HashMap<>();

    public Conversation getConversation(String id) {
        Conversation conversation;
        if (!conversations.containsKey(id)) {
            conversation = new Conversation();
            conversations.put(id, conversation);
        } else
            conversation = conversations.get(id);
        return conversation;
    }
}
