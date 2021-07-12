package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mycab.R;
import com.mycab.databinding.ActivityOTPVerifyBinding;
import com.mycab.utils.Api;
import com.mycab.utils.Appconstant;
import com.mycab.utils.ProgressBarCustom.CustomDialog;
import com.mycab.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPVerifyActivity extends AppCompatActivity {
ActivityOTPVerifyBinding binding;
String getEmail="",getOTP="",getMobile="",pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPVerifyBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        /*binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPVerifyActivity.this,ChooseTypeActivity.class));
            }
        });
*/

        getEmail = SharedHelper.getKey(getApplicationContext(), Appconstant.UserEmail);
        getOTP = SharedHelper.getKey(getApplicationContext(), Appconstant.GetOtp);
        getMobile = SharedHelper.getKey(getApplicationContext(), Appconstant.UserMobile);

        Log.e("OTPVerifyActivity", "onCreate: " +getOTP);

       /* binding.txtResend.setVisibility(View.GONE);
       */
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.pinView.setAnimationEnable(true);


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                pin = binding.pinView.getText().toString();



                if (pin.length() != 4) {

                    Toast.makeText(OTPVerifyActivity.this, "Please enter 4 digit otp", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("ksjldxks",pin);
                    if (getOTP.equals(pin)) {

                        verify_otp();


                    } else {
                        Toast.makeText(OTPVerifyActivity.this, "You have entered wrong otp", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

    }

    private void verify_otp(){

        Log.e("check", "getEmail: " +getEmail);
        Log.e("check", "getMobile: " +getMobile);
        Log.e("check", "pin: " +pin);
       CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(Api.BASE_URL+Api.verify_otp)
                .addBodyParameter("email",getEmail)
                .addBodyParameter("mobile",getMobile)
                .addBodyParameter("type", "0")/* type=0 Driver type= 1 user*/
                .addBodyParameter("otp",pin)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dsvbfdzb ",response.toString());
                           dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("sign_in  Successfully")){

                                String data=response.getString("data");

                                Log.e("dvbdfvb", "data: " +data);

                                 JSONObject object=new JSONObject(data);

                                   String otp_varify_status=object.getString("otp_varify_status");

                                Log.e("checkstatus", "checkstatus: " +otp_varify_status);


                                   if (otp_varify_status.equals("0")){

                                       startActivity(new Intent(OTPVerifyActivity.this,UploadDocumnetActivity.class));
                                   }
                                   else {

                                       SharedHelper.putKey(getApplicationContext(), Appconstant.UserID, object.getString("id"));
                                       SharedHelper.putKey(getApplicationContext(), Appconstant.UserName, object.getString("name"));
                                       SharedHelper.putKey(getApplicationContext(), Appconstant.UserEmail, object.getString("email"));
                                       SharedHelper.putKey(getApplicationContext(), Appconstant.UserMobile, object.getString("phone_number"));
                                       startActivity(new Intent(OTPVerifyActivity.this,NavigationActivity.class));
                                   }

                                Toast.makeText(OTPVerifyActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();


                            }
                            else {
                                Toast.makeText(OTPVerifyActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("fbhvfgnb",e.getMessage());
                           dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfnjyhgn",anError.getMessage());
                       dialog.hideDialog();

                    }
                });



    }



}