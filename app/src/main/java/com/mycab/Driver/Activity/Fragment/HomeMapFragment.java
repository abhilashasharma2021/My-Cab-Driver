package com.mycab.Driver.Activity.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.muddzdev.styleabletoast.StyleableToast;
import com.mycab.Driver.Activity.Fragment.Activity.NavigationActivity;
import com.mycab.Driver.Activity.Fragment.Activity.ProfileActivity;
import com.mycab.Driver.Activity.Fragment.Activity.TripHistoryActivity;
import com.mycab.R;
import com.mycab.utils.Api;
import com.mycab.utils.Appconstant;
import com.mycab.utils.GPSTracker;
import com.mycab.utils.ProgressBarCustom.CustomDialog;
import com.mycab.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback{
ImageView menu,prf;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    double lat, lng;
    LatLng latLng;
    RelativeLayout goOnline,goOffline;
    public  static TextView txt_Deny,txPickAddress,txDropAddress,txtClientname,txt_Accept,txDistance,txtNumber;
    public static CircleImageView profilePic;
    String getUserId="";
    Timer carousalTimer;
    ImageView  onlinebtn;
    /////////////////////////////////////////////

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean isLocationPermission = false;
   String OnlineStatus="",REQUESTID="";
    CameraPosition cameraPosition;
    Location lastLocation;
    //////////////////////////////////////////////



    BroadcastReceiver  mRegistrationBroadcastReceiver;
    String driverStatus="";
   /*0=user_book,1=driver_confirm 2=driver_cancle 3=user_cancle 4=userc_confirm , 6=complete ride 5= start ride*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        menu = view.findViewById(R.id.menu);
        prf = view.findViewById(R.id.prf);
        goOnline = view.findViewById(R.id.goOnline);
        onlinebtn = view.findViewById(R.id.onlinebtn);
        goOffline = view.findViewById(R.id.goOffline);

        getUserId = SharedHelper.getKey(getActivity(), Appconstant.UserID);

        OnlineStatus = SharedHelper.getKey(getActivity(), Appconstant.OnlineStatus);
        REQUESTID = SharedHelper.getKey(getActivity(), Appconstant.REQUESTID);

        Log.e("fdbrf", "OnlineStatus: " +OnlineStatus);
        Log.e("fhtht", "OnlineStatus: " +REQUESTID);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }



        if (getUserId.equals("")) {
            goOnline.setVisibility(View.GONE);
            goOffline.setVisibility(View.VISIBLE);
        }

        else {
            if (OnlineStatus.equals("1")){
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
                onlinebtn.setVisibility(View.VISIBLE);
            }else   if (OnlineStatus.equals("0")){
                onlinebtn.setVisibility(View.GONE);
                goOnline.setVisibility(View.GONE);
                goOffline.setVisibility(View.VISIBLE);

            }else {
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
            }



        }

        goOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserId = SharedHelper.getKey(getActivity(), Appconstant.UserID);

                Log.e("MapFragment", "offline: " +getUserId);
                onlineStatus(getUserId, "1");
            }
        });

        goOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MapFragment", "online: " +getUserId);
                // Toast.makeText(getActivity(), "checkOnline", Toast.LENGTH_SHORT).show();
                getUserId = SharedHelper.getKey(getActivity(), Appconstant.UserID);
                onlineStatus(getUserId, "0");
            }
        });



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity.drawer.openDrawer(GravityCompat.START);
            }
        });
        prf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById( R.id.frag_map );
        mapFragment.getMapAsync( this );

        gpsTracker = new GPSTracker(getActivity());
        lat = gpsTracker.getLatitude();
        lng = gpsTracker.getLongitude();
        Log.e("sdfdsv", lat + "");
        Log.e("sdfdsv", lng + "");


    mRegistrationBroadcastReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("chchchchchchch", "onReceive: " );


                String result = intent.getStringExtra("title");
                String requestId = intent.getStringExtra("id");


                Log.e("tgyhtghyyt", result);
                Log.e("tgyhtghyyt", requestId);

                if (result.equals("New Delivery Request Found")) {
                    showrequest(requestId);
                }


            }
        };



        if (!REQUESTID.equals("")){
             showrequest(REQUESTID);
         }




        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        ////set direction circle marker bootom right on map
        mMap.setPadding(10, 650, 10, 10);


        //Place current location marker
        latLng = new LatLng(lat, lng);

        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();

        ////SHOW LOCATION OF MARKER AND COLOUR

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("Check"));
    }

    private void showrequest(String id){
        Toast.makeText(getActivity(), "checkDialog", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Api.BASE_URL+Api.show_user_to_dirver)
                .addBodyParameter("id",id)
                 .setTag("Show Notification")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("djfdjkgvjkf", "onResponse: " +response);

                        try {
                            if (response.getString("result").equals("Successfully")){
                                String booking_Id=response.getString("id");
                                String user_data=response.getString("user_data");

                                if (!user_data.isEmpty()){

                                    JSONObject jsonObject=new JSONObject(user_data);

                                    Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.dialog_accepted_layout);
                                    dialog.setCancelable(true);
                                    txt_Deny = dialog.findViewById(R.id.txt_Deny);
                                    txPickAddress =  dialog.findViewById(R.id.txPickAddress);
                                    txDropAddress =  dialog.findViewById(R.id.txDropAddress);
                                    txtNumber =  dialog.findViewById(R.id.txtNumber);
                                    txt_Accept =  dialog.findViewById(R.id.txt_Accept);
                                    txDistance = dialog.findViewById(R.id.txDistance);
                                    txtClientname =  dialog.findViewById(R.id.txtClientname);
                                    profilePic =  dialog.findViewById(R.id.profilePic);



                                    Log.e("djfdjkgvjkf", "onResponse: " +response.getString("pickup_location"));
                                    Log.e("djfdjkgvjkf", "onResponse: " +response.getString("drop_location"));
                                    Log.e("djfdjkgvjkf", "onResponse: " +jsonObject.getString("name"));

                                    txPickAddress.setText(response.getString("pickup_location"));
                                    txDropAddress.setText(response.getString("drop_location"));
                                    txtClientname.setText(jsonObject.getString("name"));
                                    txtNumber.setText(jsonObject.getString("phone_number"));
                                    txDistance.setText(response.getString("total_distance"));

                             /*   try {
                                    Glide.with(getActivity()).load(response.getString("path")+jsonObject.getString("image")).error(R.drawable.profile).into(profilePic);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/


                                    txt_Accept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            driverStatus="1";
                                            accept_Request(driverStatus,booking_Id);
                                            dialog.dismiss();


                                        }
                                    });

                                    txt_Deny.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            driverStatus="2";
                                            cancelRequest(driverStatus,booking_Id);
                                            dialog.dismiss();

                                        }
                                    });

                                    dialog.show();







                                }






                            }
                        } catch (JSONException e) {
                            Log.e("dvfdvb", "e: " +e);
                        }




                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdsgvfd", "onError: " +anError);
                    }
                });

    }

    private void onlineStatus(String getUserId, String status){
        AndroidNetworking.post(Api.BASE_URL+Api.driver_online_status)
                .addBodyParameter("driver_id",getUserId)
                .addBodyParameter("status",status)
                .setTag("Accept Request")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dsfvgfd", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("Successfully")){

                                String data=response.getString("data");

                                JSONObject jsonObject=new JSONObject(data);

                                /*login_status =0 means driver offline and login_status =1 means driver login*/

                                String driver_online_status = jsonObject.getString("login_status");

                                Log.e("hdfhd", driver_online_status);


                                SharedHelper.putKey(getActivity(), Appconstant.OnlineStatus, driver_online_status);

                                if (driver_online_status.equals("0")) {

                                    new StyleableToast
                                            .Builder(getActivity())
                                            .text("Offline")
                                            .textColor(Color.WHITE)
                                            .backgroundColor(Color.parseColor("#d50000"))
                                            .show();

                                    goOffline.setVisibility(View.VISIBLE);
                                    goOnline.setVisibility(View.GONE);
                                    onlinebtn.setVisibility(View.GONE);

                                } else {
                                    new StyleableToast
                                            .Builder(getActivity())
                                            .text("Online")
                                            .textColor(Color.WHITE)
                                            .backgroundColor(Color.parseColor("#0092df"))
                                            .show();

                                    goOnline.setVisibility(View.VISIBLE);
                                    goOffline.setVisibility(View.GONE);
                                    onlinebtn.setVisibility(View.VISIBLE);



                                }





                            }
                        } catch (JSONException e) {
                            Log.e("fdvfdv", "onResponse: " +e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdvfdv", "anError: " +anError);
                    }
                });

    }







    private void updateLatLong(String latitude, String longitude){
        try {
            getUserId = SharedHelper.getKey(getActivity(), Appconstant.UserID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Api.BASE_URL+Api.driver_online_offline)
                .addBodyParameter("driver_id",getUserId)
                .addBodyParameter("lat",latitude)
                .addBodyParameter("long",longitude)
                .setTag("driver_online_offline")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tyuiyijuy", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("Successfully")){
                              //  Toast.makeText(getActivity(), response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("hfgtjhfg", "onResponse: " +e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("hfgtjhfg", "anError: " +anError);
                    }
                });

    }


    public void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isLocationPermission = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermission = true;
            }
        }

        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (isLocationPermission) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void getDeviceLocation() {

        if (isLocationPermission) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();

                        if (lastLocation != null) {
                            lastLocation.getLatitude();
                            lastLocation.getLongitude();


                            carousalTimer = new Timer(); // At this line a new Thread will be created
                            carousalTimer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {


                                    Log.e("sdavsdvds", "check: " + lastLocation.getLatitude());
                                    Log.e("sdavsdvds", "check: " + lastLocation.getLongitude());

                                    updateLatLong(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));
                                }
                            }, 0, 5 * 1000); // delay


                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 12));


                        } else {
                            LatLng latLng = new LatLng(33.8688, 151.2093);
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                }
            });


        }
    }


    private  void accept_Request(String driverStatus, String booking_Id){

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());
        AndroidNetworking.post(Api.BASE_URL+Api.driver_confirm)
                .addBodyParameter("booking_id",booking_Id)
                .addBodyParameter("cnf_status",driverStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("HomeMapFragment", "response: " +response);
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("successfully")){


                                String booking_id=response.getString("id");
                                String user_id=response.getString("user_id");
                                String pickup_location=response.getString("pickup_location");
                                String pickup_lat=response.getString("pickup_lat");
                                String pickup_long=response.getString("pickup_long");
                                String drop_location=response.getString("drop_location");
                                String drop_lat=response.getString("drop_lat");
                                String drop_long=response.getString("drop_long");

                                SharedHelper.putKey(getActivity(), Appconstant.User_PickUpLat, pickup_lat);
                                SharedHelper.putKey(getActivity(), Appconstant.User_PickUpLong, pickup_long);
                                SharedHelper.putKey(getActivity(), Appconstant.User_DropLat, drop_lat);
                                SharedHelper.putKey(getActivity(), Appconstant.User_DropLong, drop_long);
                                SharedHelper.putKey(getActivity(), Appconstant.booking_id, booking_id);

                                SharedHelper.putKey(getActivity(), Appconstant.REQUESTID,"");
                                Toast.makeText(getActivity(), "Successfull Accept", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("dfdsfd", "onResponse: " +e);
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfdsfd", "anError: " +anError);
                        dialog.hideDialog();
                    }
                });

    }



    private  void cancelRequest(String driverStatus, String booking_Id){

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());
        AndroidNetworking.post(Api.BASE_URL+Api.driver_confirm)
                .addBodyParameter("booking_id",booking_Id)
                .addBodyParameter("cnf_status",driverStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ddfdf", "response: " +response);
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("successfully")){

                                new StyleableToast
                                        .Builder(getActivity())
                                        .text("successfully")
                                        .textColor(Color.WHITE)
                                        .backgroundColor(Color.parseColor("#0092df"))
                                        .show();
                            }

                            else {
                                Toast.makeText(getActivity(),response.getString("result") , Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }

                        } catch (JSONException e) {
                            Log.e("vbfdvbgfd", "onResponse: " +e);
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fbfdbfd", "anError: " +anError);
                        dialog.hideDialog();
                    }
                });

    }

}