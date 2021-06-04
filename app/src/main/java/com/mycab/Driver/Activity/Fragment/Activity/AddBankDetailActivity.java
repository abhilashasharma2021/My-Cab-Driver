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
import com.mycab.databinding.ActivityAddBankDetailBinding;
import com.mycab.utils.Api;
import com.mycab.utils.Appconstant;
import com.mycab.utils.ProgressBarCustom.CustomDialog;
import com.mycab.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBankDetailActivity extends AppCompatActivity {
    ActivityAddBankDetailBinding binding;
    String stAccount = "", stIfsc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBankDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stAccount = binding.etAccount.getText().toString().trim();
                stIfsc = binding.etIfsc.getText().toString().trim();

                if (stAccount.isEmpty()) {
                    Toast.makeText(AddBankDetailActivity.this, "Please enter valid account number", Toast.LENGTH_SHORT).show();
                } else if (stAccount.length() < 18) {
                    Toast.makeText(AddBankDetailActivity.this, "Please enter atleast 18 digit account number", Toast.LENGTH_SHORT).show();
                } else if (stIfsc.length() < 11) {
                    Toast.makeText(AddBankDetailActivity.this, "Please enter atleast 11 digit Ifsc code", Toast.LENGTH_SHORT).show();
                } else if (stIfsc.isEmpty()) {
                    Toast.makeText(AddBankDetailActivity.this, "Please enter valid ifsc code", Toast.LENGTH_SHORT).show();
                }
                else {
                    addBankDetails(stAccount,stIfsc);
                }


            }
        });
    }


    private void addBankDetails(String stAccount, String stIfsc) {
        String UserID = SharedHelper.getKey(getApplicationContext(), Appconstant.UserID);

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(Api.BASE_URL + Api.addBankDetails)
                .addBodyParameter("user_id", UserID)
                .addBodyParameter("account_number", stAccount)
                .addBodyParameter("ifsc", stIfsc)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("fghfh", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("successfully")) {

                                signUp();

                            } else {
                                Toast.makeText(AddBankDetailActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("rgrtfh", e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("trhtrj", anError.getMessage());
                        dialog.hideDialog();

                    }
                });


    }

    private void signUp() {

        String UserEmail = SharedHelper.getKey(getApplicationContext(), Appconstant.UserEmail);
        String UserMobile = SharedHelper.getKey(getApplicationContext(), Appconstant.UserMobile);
        String UserID = SharedHelper.getKey(getApplicationContext(), Appconstant.UserID);

        Log.e("dsdsv", "UserEmail: " +UserEmail);
        Log.e("dsdsv", "UserMobile: " +UserMobile);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(Api.BASE_URL + Api.signup)
                .addBodyParameter("email", UserEmail)
                .addBodyParameter("mobile", UserMobile)
                .addBodyParameter("user_id", UserID)
                .addBodyParameter("type", "0")/* type=0 Driver type= 1 user*/
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("vxcvcxcx", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("Signup Successfully")) {

                                 SharedHelper.putKey(getApplicationContext(), Appconstant.UserEmail, response.getString("email"));
                                SharedHelper.putKey(getApplicationContext(), Appconstant.UserID, response.getString("id"));
                                SharedHelper.putKey(getApplicationContext(), Appconstant.UserMobile, response.getString("phone_number"));



                                startActivity(new Intent(AddBankDetailActivity.this, NavigationActivity.class));
                                Toast.makeText(AddBankDetailActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(AddBankDetailActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("dsvfdsvb", e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dsvgbfdb", anError.getMessage());
                        dialog.hideDialog();

                    }
                });


    }
}