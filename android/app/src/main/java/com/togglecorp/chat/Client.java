package com.togglecorp.chat;

import android.content.Context;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static class NetworkException extends Exception {
        public NetworkException(int code, String message) {
            super("Network error - Code: " + code + " Error message: " + message);
        }
    }

    public final static String BASE_URL = "http://192.168.100.12:8000/";

    private final String mUsername;
    private final String mPassword;
    private final Context mContext;

    public Client(Context context, String username, String password) {
        mContext = context;
        mUsername = username;
        mPassword = password;
    }

    public JSONObject post(String path, JSONObject data) throws JSONException, IOException, NetworkException {
        if (data == null)
            data = new JSONObject();

        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoInput (true);
        connection.setDoOutput (true);

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        String authorizationString = "Basic " + Base64.encodeToString(
                (mUsername + ":" + mPassword).getBytes(),
                Base64.NO_WRAP);
        connection.setRequestProperty("Authorization", authorizationString);

        connection.setFixedLengthStreamingMode(data.toString(). length());
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(data.toString().getBytes());
        out.flush();
        out.close();

        int code = connection.getResponseCode();

        // Get the response and disconnect when done
        if (code >= 400) {
            InputStream in = new BufferedInputStream(connection.getErrorStream());
            throw new NetworkException(code, new Scanner(in).useDelimiter("\\A").next());
        }
        else {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            return new JSONObject(new Scanner(in).useDelimiter("\\A").next());
        }
    }

    public JSONObject post(String path) throws IOException, NetworkException, JSONException {
        return post(path, null);
    }


    public void getUsers() throws JSONException, NetworkException, IOException {
        JSONObject response = post("api/v1/user/get/");

        DatabaseHelper helper = new DatabaseHelper(mContext);
        User.deleteAll(helper);

        JSONArray users = response.getJSONArray("users");
        for (int i=0; i<users.length(); ++i) {
            JSONObject user = users.getJSONObject(i);
            User.add(helper, user);
        }
    }

    public void getConversations() throws JSONException, NetworkException, IOException {
        JSONObject response = post("api/v1/conversation/get/");

        DatabaseHelper helper = new DatabaseHelper(mContext);
        Conversation.deleteAll(helper);

        JSONArray conversations = response.getJSONArray("conversations");
        for (int i=0; i<conversations.length(); ++i) {
            JSONObject conversation = conversations.getJSONObject(i);
            Conversation.add(helper, conversation);
        }
    }

    public Conversation getConversation(long id) throws JSONException, NetworkException, IOException {
        JSONObject response = post("api/v1/conversation/get/" + id + "/");
        DatabaseHelper helper = new DatabaseHelper(mContext);
        return Conversation.add(helper, response);
    }

    public Conversation addConversation(String title, List<Long> users) throws JSONException, NetworkException, IOException {
        JSONObject data = new JSONObject();
        if (title != null)
            data.put("title", title);
        if (users != null) {
            JSONArray us = new JSONArray();
            for (Long u: users)
                us.put(u);
            data.put("users", us);
        }

        JSONObject response = post("api/v1/conversation/add/", data);
        return Conversation.add(new DatabaseHelper(mContext), response);
    }

    public void getMessages(long conversationId, Long startTime, Long endTime, Integer count) throws JSONException, NetworkException, IOException {
        JSONObject data = new JSONObject();
        if (startTime != null)
            data.put("start_time", startTime);
        if (endTime != null)
            data.put("end_time", endTime);
        if (count != null)
            data.put("count", count);

        JSONObject response = post("api/v1/message/get/"+conversationId+"/", data);
        DatabaseHelper helper = new DatabaseHelper(mContext);

        JSONArray messages = response.getJSONArray("messages");
        for (int i=0; i<messages.length(); ++i) {
            JSONObject message = messages.getJSONObject(i);
            Message.add(helper, message);
        }
    }

    public Message addMessage(long conversationId, String message, long posted_at, long posted_by) throws IOException, NetworkException, JSONException {
        JSONObject data = new JSONObject();
        data.put("message", message);
        data.put("posted_at", posted_at);
        data.put("posted_by", posted_by);

        JSONObject response = post("api/v1/message/add/"+conversationId+"/", data);
        DatabaseHelper helper = new DatabaseHelper(mContext);
        return Message.add(helper, response);
    }
}
