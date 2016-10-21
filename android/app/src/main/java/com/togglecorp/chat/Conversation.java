package com.togglecorp.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conversation {

    public static class Info {
        public String title;
        public List<String> users = new ArrayList<>();

        public Info() {}

        public String getTitle() {
            if (title.length() > 0)
                return title;
            else if (users.size() == 2) {
                if (users.get(0).equals(Database.get().selfId))
                    return Database.get().users.get(users.get(1)).displayName;
                else
                    return Database.get().users.get(users.get(0)).displayName;
            }
            else if (users.size() > 2)
                return "Group";
            return "Unknown";
        }
    }

    public Info info = new Info();
    public Map<String, Message> messages = new HashMap<>();
}
