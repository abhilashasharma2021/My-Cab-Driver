package com.mycab.Driver.Activity.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycab.Driver.Activity.Fragment.Activity.NavigationActivity;
import com.mycab.Driver.Activity.Fragment.Activity.ProfileActivity;
import com.mycab.R;
import com.mycab.utils.Api;
import com.mycab.utils.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback{
ImageView menu,prf;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    double lat, lng;
    LatLng latLng;
    RelativeLayout goOnline;
    public  static TextView txt_Deny,txPickAddress,txDropAddress,txtClientname,txt_Accept,txDistance,txtNumber;
    public static CircleImageView profilePic;

BroadcastReceiver mRegistrationBroadcastReceiver =new BroadcastReceiver() {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        menu = view.findViewById(R.id.menu);
        prf = view.findViewById(R.id.prf);
        goOnline = view.findViewById(R.id.goOnline);

        goOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


        ////SHOW LOCATION OF MARKER AND COLOUR

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("Check"));
    }

    private void showrequest(String id){
        AndroidNetworking.post(Api.BASE_URL+Api.show_user_to_dirver)
                .addBodyParameter("id",id)
                 .setTag("Show Notification")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("HomeMapFragment", "onResponse: " +response);

                        try {
                            if (response.getString("result").equals("Successfully")){

                                JSONObject jsonObject=new JSONObject(response.getString("user_data"));

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



                                Log.e("HomeMapFragment", "onResponse: " +response.getString("pickup_location"));
                                Log.e("HomeMapFragment", "onResponse: " +response.getString("drop_location"));
                                Log.e("HomeMapFragment", "onResponse: " +jsonObject.getString("name"));

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

                                txt_Deny.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();






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

    private void accept_request(){
        AndroidNetworking.post(Api.BASE_URL+Api.show_user_to_dirver)
                .addBodyParameter("")
                .setTag("Accept Request")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dsfvgfd", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("")){

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

}