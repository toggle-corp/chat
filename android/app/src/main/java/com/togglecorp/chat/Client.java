package com.togglecorp.chat;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public Client(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public JSONObject post(String path, JSONObject data) throws JSONException, IOException, NetworkException {
        if (data == null)
            data = new JSONObject("{}");

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



}
