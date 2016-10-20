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
    }

    public Info info = new Info();
    public Map<String, Message> messages = new HashMap<>();
}
