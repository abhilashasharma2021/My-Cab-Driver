package com.mycab.Driver.Activity.Fragment.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mycab.Driver.Activity.Fragment.Activity.NavigationActivity;
import com.mycab.R;
import com.mycab.utils.Appconstant;
import com.mycab.utils.Config;
import com.mycab.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Notification.PRIORITY_HIGH;

import static com.mycab.Driver.Activity.Fragment.FCM.App.FCM_CHANNEL_ID;

public class FCMMessageRecieverService extends FirebaseMessagingService {

    private NotificationUtils notificationUtils;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("shdfj", "message recieved called");
       // SharedHelper.putKey(getApplicationContext(), AppConstats.VendorRouteStatus, "");

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.e("dsgdfhf", "onMessageReceived: " +title);
            Log.e("dsgdfhf", "onMessageReceived: " +body);
            Notification notification = new NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(PRIORITY_HIGH)
                    .setColor(Color.MAGENTA)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002, notification);
        }


        if (remoteMessage.getData().size() > 0) {

            Log.e("yuyuy", remoteMessage.getData().size() + "");
            Log.e("yuyuy", remoteMessage.getData().toString());

            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                  handleDataMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("shdfj", "message delete called");
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("shdfj", "newToken called");

        SharedHelper.putKey(getApplicationContext(), Appconstant.REG_ID_TOKEN, s);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleDataMessage(JSONObject json) {
        Log.e("cnvnsvkn", "push" + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");

            String payload = data.getString("payload");
            JSONObject jsonObject = new JSONObject(payload);

            Log.e("rytrujyt", payload);
            Log.e("rytrujyt", title);

            Intent intent=new Intent("Check");
            intent.putExtra("title",title);
            intent.putExtra("payload",payload);
            sendBroadcast(intent);




            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
               /* Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);*/

                // play notification sound
                Log.e("handleDataMessage", "If");
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                SharedHelper.putKey(getApplicationContext(), Appconstant.REQUESTED_TITLE, title);


                Intent resultIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                resultIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);

                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(),
                            title, message.split("/")[0], timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(),
                            title, message.split("/")[0], timestamp, resultIntent, imageUrl);
                }
            } else {
                Log.e("handleDataMessage", "else");
                // app is in background, show the notification in notification tray
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                Log.e("fdskfdsk", message + "");
               /* Intent resultIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                resultIntent.putExtra("message", message);*/


                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message.split("/")[0], timestamp, pushNotification);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message.split("/")[0], timestamp, pushNotification, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e("dndacj", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("dndacj", "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
