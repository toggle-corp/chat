package com.togglecorp.chat;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FCMServer {
    private final static String TAG = "FCMServer";

    public final static String AUTH_KEY_FCM = "AIzaSyD7e2QASMUE1Wnc1rzkxJR5dUtWduMCvnk";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void pushNotification(String title, String body,
                                           List<String> tokens) throws Exception {
        if (tokens.size() == 0)
            return;

        String authKey = AUTH_KEY_FCM;
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject data = new JSONObject();
        data.put("registration_ids", new JSONArray(tokens));
        JSONObject info = new JSONObject();
        info.put("title", title);
        info.put("body", body);
        data.put("data", info);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data.toString());
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.d(TAG, "Response Code : " + responseCode);

//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();

    }
}
