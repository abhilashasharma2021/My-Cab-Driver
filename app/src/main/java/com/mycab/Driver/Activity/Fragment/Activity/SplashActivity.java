package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.anilokcun.uwmediapicker.UwMediaPicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mycab.MainActivity;
import com.mycab.R;
import com.mycab.utils.Appconstant;
import com.mycab.utils.SharedHelper;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
String userId="",PagerStatus="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userId = SharedHelper.getKey(getApplicationContext(), Appconstant.UserID);
        PagerStatus = SharedHelper.getKey(getApplicationContext(), Appconstant.PagerStatus);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE
                ). withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (PagerStatus.equals("1")) {

                            if (userId.equals("")) {

                                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                                finish();
                            }

                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }


                    }
                }, 3000);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();







    }
}