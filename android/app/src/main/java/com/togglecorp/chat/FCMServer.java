package com.togglecorp.chat;

import android.os.AsyncTask;
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

    public static class PushNotificationTask extends AsyncTask<Void, Void, Void> {
        private String mTitle, mBody, mConversation;
        private List<String> mTokens;

        public PushNotificationTask(String title, String body, String conversation,
                                    List<String> tokens) {
            mTitle = title;
            mBody = body;
            mTokens = tokens;
            mConversation = conversation;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                pushNotification(mTitle, mBody, mConversation, mTokens);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static void pushNotification(String title, String body, String conversation,
                                           List<String> tokens) throws Exception {

        Log.d(TAG, "Sending notifications");
        if (tokens.size() == 0)
            return;
        Log.d(TAG, "Sending notifications");

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
        info.put("conversation", conversation);
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
