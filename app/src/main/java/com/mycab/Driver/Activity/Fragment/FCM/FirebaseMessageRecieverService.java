package com.mycab.Driver.Activity.Fragment.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mycab.Driver.Activity.Fragment.Activity.NavigationActivity;
import com.mycab.MainActivity;
import com.mycab.R;
import com.mycab.utils.Appconstant;
import com.mycab.utils.SharedHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Notification.PRIORITY_HIGH;

public class FirebaseMessageRecieverService extends FirebaseMessagingService {

    String body = "";

    PendingIntent pendingIntent;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("check", "onMessageRecieved Called");



        if (remoteMessage.getData().size() > 0) {
            Log.e("check", "Data received");
            Log.e("check", remoteMessage.getData().toString());

            try {
                String Data = remoteMessage.getData().toString();
                String newData = "";
                if (Data.contains("=")) {
                    newData = Data.replace("=", ":");
                }

                Log.e("check", "newData: " +newData);
                JSONObject jsonObject=new JSONObject(newData);
                JSONObject data =new JSONObject(jsonObject.getString("data"));
                Log.e("check", "data: " +data);
                String title = data.getString("title");
                String message = data.getString("message");
                String payload = data.getString("payload");
                String id="";
                JSONObject jsonObject1=new JSONObject(payload);
                JSONArray jsonArray=new JSONArray(jsonObject1.getString("driver_ride"));
                for (int i = 0; i <jsonArray.length() ; i++) {

                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                     id=jsonObject2.getString("id");
                }



                Log.e("check", "title: " +title);
                Log.e("check", "message: " +message);
                Log.e("check", "id: " +id);

                Intent myIntent = new Intent("Check");
                myIntent.putExtra("title", title);
                myIntent.putExtra("id", id);
                this.sendBroadcast(myIntent);


                Intent resultIntent = new Intent(this, NavigationActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                startActivity(resultIntent);

                Notification notification = new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                        .setSmallIcon(R.drawable.cancle)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(PRIORITY_HIGH)
                        .setColor(Color.BLACK)
                        .setSound(null)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(1002, notification);


                SharedHelper.putKey(getApplicationContext(), Appconstant.REQUESTID,id);




                //  handleDataMessage(jsonObject);
            } catch (JSONException e) {

                Log.e("rrgbvbhgn", "onMessageReceived: " +e); } } }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("hfhghdggdhhgdhgd", "onDeleteMessage Called");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("check", "onNewToken Called");
        SharedHelper.putKey(getApplicationContext(), Appconstant.REG_ID_TOKEN, s);
    }






}


