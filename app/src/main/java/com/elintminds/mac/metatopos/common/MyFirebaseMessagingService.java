package com.elintminds.mac.metatopos.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.elintminds.mac.metatopos.activities.AdvertisementActivity;
import com.elintminds.mac.metatopos.activities.ChatActivity;
import com.elintminds.mac.metatopos.activities.EventsActivity;
import com.elintminds.mac.metatopos.activities.FollowersActivity;
import com.elintminds.mac.metatopos.activities.PostActivity;
import com.elintminds.mac.metatopos.activities.RatingFeedbackActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences preferences;
    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onNewToken(String token) {
        token = FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
        Log.d(TAG, "Refreshed_token: " + token);
        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getNotification().getBody() + "\n" + remoteMessage.getData());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message_Data_Payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData(), remoteMessage.getNotification().getTitle());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message_Notification_Body: " + remoteMessage.getNotification().getBody());
        }
        super.onMessageReceived(remoteMessage);
    }

    private void handleNow(Map<String, String> data, String title) {
        if (data.get("notificationtype").equalsIgnoreCase("7")) {
            AppConstants.anyNotification = true;
            if (!ChatActivity.isUserOnChat) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("PostID", data.get("targetid"));
                intent.putExtra("ReceiverID", data.get("senderid"));
                Log.e("POSTID", "" + data.get("targetid") + "RECEIVERID" + data.get("senderid"));
                NotificationUtils notificationUtils = new NotificationUtils(this);
                notificationUtils.showNotificationMessage(title, "New Message From", "", intent);
            } else {
                Intent intent = new Intent("New Message");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

        } else if (data.get("notificationtype").equalsIgnoreCase("5")) {
            if (AppConstants.IS_APP_OPEN) {
                Intent intent = new Intent(this, RatingFeedbackActivity.class);
                intent.putExtra("USER_ID", data.get("senderid"));
                intent.putExtra("IS_FROM", "NOTIFICATIONS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, RatingFeedbackActivity.class);
                intent.putExtra("USER_ID", data.get("senderid"));
                intent.putExtra("IS_FROM", "NOTIFICATIONS");
                NotificationUtils notificationUtils = new NotificationUtils(this);
                notificationUtils.showNotificationMessage(title, data.get("alert"), "", intent);
            }
        } else if (data.get("notificationtype").equalsIgnoreCase("4")) {
            if (AppConstants.IS_APP_OPEN) {
                Intent intent = new Intent(this, FollowersActivity.class);
                intent.putExtra("userid", data.get("receiverid"));
                intent.putExtra("IS_FROM", "NOTIFICATIONS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, FollowersActivity.class);
                intent.putExtra("userid", data.get("receiverid"));
                intent.putExtra("IS_FROM", "NOTIFICATIONS");
                NotificationUtils notificationUtils = new NotificationUtils(this);
                notificationUtils.showNotificationMessage(title, data.get("alert"), "", intent);
            }
        } else if (data.get("notificationtype").equalsIgnoreCase("1")) {
            if (data.get("post_type").equalsIgnoreCase("post")) {
                if (AppConstants.IS_APP_OPEN) {
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    NotificationUtils notificationUtils = new NotificationUtils(this);
                    notificationUtils.showNotificationMessage(title, data.get("alert"), "", intent);
                }
            } else if (data.get("post_type").equalsIgnoreCase("event")) {
                if (AppConstants.IS_APP_OPEN) {
                    Intent intent = new Intent(this, EventsActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, EventsActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    NotificationUtils notificationUtils = new NotificationUtils(this);
                    notificationUtils.showNotificationMessage(title, data.get("alert"), "", intent);
                }
            } else if (data.get("post_type").equalsIgnoreCase("advertisement")) {
                if (AppConstants.IS_APP_OPEN) {
                    Intent intent = new Intent(this, AdvertisementActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, AdvertisementActivity.class);
                    intent.putExtra("PostID", data.get("targetid"));
                    intent.putExtra("IS_FROM", "NOTIFICATIONS");
                    NotificationUtils notificationUtils = new NotificationUtils(this);
                    notificationUtils.showNotificationMessage(title, data.get("alert"), "", intent);
                }
            }
        } else {
            AppConstants.anyNotification = true;
            NotificationUtils notificationUtils = new NotificationUtils(this);
            String message = data.get("message");
            notificationUtils.showNotificationMessage(this, "New Message from ", message);
        }

    }

    private void sendRegistrationToServer(String token) {

    }

}
