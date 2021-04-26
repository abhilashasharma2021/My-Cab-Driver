package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.mycab.Fragment.HomeMapFragment;
import com.mycab.MainActivity;
import com.mycab.R;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {
    public static DrawerLayout drawer;
    RelativeLayout rel_logout, rlNotification, rlEarning, rel_Trip, rlSetting;
    ImageView prf;


    String getUsername = "", getNumber = "";
    TextView name,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        drawer = findViewById(R.id.drawer);
        rel_logout = findViewById(R.id.rel_logout);
        rel_Trip = findViewById(R.id.rel_Trip);
        rlEarning = findViewById(R.id.rlNotification);
        rlNotification = findViewById(R.id.rlEarning);
        rlSetting = findViewById(R.id.rlSetting);
       /* rel_mcworld = findViewById(R.id.rel_mcworld);
        prf = findViewById(R.id.prf);
        rel_profile = findViewById(R.id.rel_profile);
        rel_settings = findViewById(R.id.rel_settings);*/
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
       /* rel_logout.setOnClickListener(this);
        rel_notifications.setOnClickListener(this);
        rel_profile.setOnClickListener(this);
        rel_mcworld.setOnClickListener(this);*/
        rel_Trip.setOnClickListener(this);
        rlNotification.setOnClickListener(this);
        rlEarning.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeMapFragment()).commit();
        }
    }
   /* @Override
    protected void onResume() {
        super.onResume();
        String getImage = SharedHelper.getKey(getApplicationContext(), AppConstats.IMAGE);
        getUsername = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_FULLNAME);
        getNumber = SharedHelper.getKey(getApplicationContext(), AppConstats.MOBILE_NUMBER);

        number.setText(getNumber);
        name.setText(getUsername);

        Glide.with(getApplicationContext()).load(getImage).error(R.drawable.prf2).into(prf);
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rel_logout:
             logout();
                break;


            case R.id.rel_Trip:
                startActivity(new Intent(getApplicationContext(), TripHistoryActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.rlNotification:
                 startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.rlSetting:
               startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.rlEarning:
                startActivity(new Intent(getApplicationContext(), EarningActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

         /*   case R.id.rel_mcworld:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"sales@mcworld25.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...");
                intent.putExtra(Intent.EXTRA_TEXT, "Your Message here...");
                intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                drawer.closeDrawer(GravityCompat.START);
                break;*/
        }
    }

    public void logout() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, "");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();*/
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}
