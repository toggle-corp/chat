package com.togglecorp.chat;

import android.content.Context;
import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FCMInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        sendTokenToServer(this);
    }

    public static void sendTokenToServer(Context context) {
        String userId = Database.get().selfId;
        if (userId == null || userId.length() == 0)
            return;

        String device_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String token = FirebaseInstanceId.getInstance().getToken();

        if (token == null || device_id == null)
            return;

        DatabaseReference reference = MainActivity.getDatabase();
        reference.child("users").child(userId).child("tokens")
                .child(device_id).setValue(token);
    }
}