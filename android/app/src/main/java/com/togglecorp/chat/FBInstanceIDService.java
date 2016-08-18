package com.togglecorp.chat;

import android.content.Context;
import android.provider.Settings;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

public class FBInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        sendTokenToServer(this);
    }

    public static void sendTokenToServer(final Context context) {
        new Client.NetworkHandler(new Client.Listener() {
            @Override
            public void process() {
                try {
                    Client client = new Client(context);
                    JSONObject data = new JSONObject();

                    String device_id = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String token = FirebaseInstanceId.getInstance().getToken();

                    data.put("device_id", device_id);
                    data.put("token", token);
                    client.post("api/v1/fcm/register/", data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void post() {

            }
        }).execute();
    }
}
