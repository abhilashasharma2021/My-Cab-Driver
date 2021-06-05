package com.mycab.Driver.Activity.Fragment.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mycab.R;
import com.mycab.utils.Appconstant;
import com.mycab.utils.SharedHelper;


import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Notification.PRIORITY_HIGH;

public class FirebaseMessageRecieverService extends FirebaseMessagingService {

    String body = "";


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("check", "onMessageRecieved Called");

        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();

            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }else {
                body="MyCab";
            }


            Log.e("sjjcbs", "ii" + title);
            Log.e("sjjcbs", "ii" + body);

            Intent myIntent = new Intent("Check");
            myIntent.putExtra("action", title);
            myIntent.putExtra("message", body);
            this.sendBroadcast(myIntent);

            if (!body.equals("null")) {

                if (body.equals("Message from Support")) {
                    Log.e("ppowsaq", "Message from support");

                } else {
                    Notification notification = new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setPriority(PRIORITY_HIGH)
                            .setColor(Color.BLACK)
                            .setSound(null)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(1002, notification); } } }

        if (remoteMessage.getData().size() > 0) {
            Log.e("check", "Data received");
            Log.e("check", remoteMessage.getData().toString());
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                //  handleDataMessage(jsonObject);
            } catch (JSONException e) {

                Log.e("rrgbvbhgn", "onMessageReceived: " +e); } } }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("check", "onDeleteMessage Called");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("check", "onNewToken Called");
        SharedHelper.putKey(getApplicationContext(), Appconstant.REG_ID_TOKEN, s);
    }






}


