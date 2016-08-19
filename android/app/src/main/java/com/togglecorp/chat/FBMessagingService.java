package com.togglecorp.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class FBMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Fcm", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> message = remoteMessage.getData();
            if (message.get("type").equals("new_message")) {
                User user = User.get(MainActivity.getDatabaseHelper(this),
                        Long.parseLong(message.get("posted_by")));

                if (user != null) {
                    sendNotification(user.full_name, message.get("message"));
                    Intent intent = new Intent(MessengerFragment.BROADCAST_INTENT);
                    sendBroadcast(intent);

                    // Add to database as well.
                    Message m = new Message();
                    m.id = Long.parseLong(message.get("id"));
                    m.conversation_id = Long.parseLong(message.get("conversation_id"));
                    m.posted_at = Long.parseLong(message.get("posted_at"));
                    m.posted_by = Long.parseLong(message.get("posted_by"));
                    m.message = message.get("message");
                    m.save(MainActivity.getDatabaseHelper(this));
                }
            }
        }

    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
