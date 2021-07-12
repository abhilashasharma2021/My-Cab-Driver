package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mycab.Driver.Activity.Fragment.adapter.ShowVehicleAdapter;
import com.mycab.Driver.Activity.Fragment.model.ShowVehicleModel;
import com.mycab.R;
import com.mycab.databinding.ActivityAddBankDetailBinding;
import com.mycab.utils.Api;
import com.mycab.utils.Appconstant;
import com.mycab.utils.ProgressBarCustom.CustomDialog;
import com.mycab.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddBankDetailActivity extends AppCompatActivity {
   public static ActivityAddBankDetailBinding binding;
    String stAccount = "", stIfsc = "";
    String regID = "";
    public static Dialog dialogVehicle;
    public static ProgressBar progressBar_dialog;
    public static Button btSave;
    RecyclerView rvType;
    public static ArrayList<String> Arr_vehicleId;
    public static ArrayList<String> Arr_vehicleName;
    ArrayList<ShowVehicleModel> showVehiclePojos;
    String getVehicleId = "",getVehicleName="";

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


        binding.txVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogVehicle = new Dialog(AddBankDetailActivity.this);
                dialogVehicle.setContentView(R.layout.dialog_select_vehicle_type_layout);
                dialogVehicle.setCancelable(true);
                btSave = dialogVehicle.findViewById(R.id.btSave);
                rvType = dialogVehicle.findViewById(R.id.rvType);
                progressBar_dialog = dialogVehicle.findViewById(R.id.progressBar_dialog);
                Arr_vehicleId = new ArrayList<>();
                Arr_vehicleName = new ArrayList<>();
                Show_Vehicle();
                dialogVehicle.show();
            }
        });


        regID = SharedHelper.getKey(getApplicationContext(), Appconstant.REG_ID_TOKEN);
        Log.e("fdgbfbhgf", "regID: " + regID);


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
                } else {
                    addBankDetails(stAccount, stIfsc);
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

                                signUp(getVehicleId);

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

    private void signUp(String getVehicleId) {


        Log.e("AddBankDetailActivity", "getVehicleId: " +getVehicleId);
        String UserEmail = SharedHelper.getKey(getApplicationContext(), Appconstant.UserEmail);
        String UserMobile = SharedHelper.getKey(getApplicationContext(), Appconstant.UserMobile);
        String UserID = SharedHelper.getKey(getApplicationContext(), Appconstant.UserID);

        Log.e("dsdsv", "UserEmail: " + UserEmail);
        Log.e("dsdsv", "UserMobile: " + UserMobile);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(Api.BASE_URL + Api.signup)
                .addBodyParameter("email", UserEmail)
                .addBodyParameter("mobile", UserMobile)
                .addBodyParameter("user_id", UserID)
                .addBodyParameter("regid", regID)
                .addBodyParameter("vehicle_id", getVehicleId)
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

    private void Show_Vehicle() {

        progressBar_dialog.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.BASE_URL+Api.show_vehical)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar_dialog.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                showVehiclePojos = new ArrayList<>();
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ShowVehicleModel showVehicleModel = new ShowVehicleModel();
                        showVehicleModel.setVehicleId(jsonObject.getString("id"));
                        showVehicleModel.setVehicleName(jsonObject.getString("name"));
                        showVehiclePojos.add(showVehicleModel);

                    }
                    rvType.setHasFixedSize(true);
                    rvType.setLayoutManager(new GridLayoutManager(AddBankDetailActivity.this, 2));
                    rvType.setAdapter(new ShowVehicleAdapter(AddBankDetailActivity.this, showVehiclePojos));
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar_dialog.setVisibility(View.GONE);
                    Log.e("rdchbvbvsv", e.getMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("rdchbvbvsv", anError.getMessage());
                progressBar_dialog.setVisibility(View.GONE);
            }
        });


    }


    public void GetSingleVehicleId(String id){
         getVehicleId=id;


    }

    public void GetShowVehicle() {

        for (int i = 0; i < Arr_vehicleId.size(); i++) {
            if (getVehicleId.equals("")||(getVehicleName.equals(""))) {
                getVehicleId = Arr_vehicleId.get(i);
                getVehicleName = Arr_vehicleName.get(i);
                binding. txVehicle.setText(getVehicleName);
                binding.ivClear.setVisibility(View.VISIBLE);
                binding.ivClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding. txVehicle.setText("");
                    }
                });
            } else {
                getVehicleId = getVehicleId + "," + Arr_vehicleId.get(i);
                getVehicleName = getVehicleName + "," + Arr_vehicleName.get(i);

               binding. txVehicle.setText(getVehicleName);

                binding.ivClear.setVisibility(View.VISIBLE);
                binding.ivClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding. txVehicle.setText("");
                    }
                });
            }
            Log.e("sfgsfgsf", getVehicleId);
            Log.e("sfgsfgsf", getVehicleName);
        }

    }
}