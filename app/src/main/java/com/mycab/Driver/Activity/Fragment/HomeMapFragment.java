package com.mycab.Driver.Activity.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
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
import com.mycab.utils.GPSTracker;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback{
ImageView menu,prf;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    double lat, lng;
    LatLng latLng;
    RelativeLayout goOnline;

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
                dialogAccept();
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
    private void dialogAccept() {


        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_accepted_layout);
        dialog.setCancelable(true);
        final TextView txt_Deny = (TextView) dialog.findViewById(R.id.txt_Deny);
        final TextView txt_Accept = (TextView) dialog.findViewById(R.id.txt_Accept);

        txt_Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
               /* strComment = edt_comment.getText().toString().trim();
                strRating = String.valueOf(rating_Star.getRating());*/

                //add_review();

            }
        });
        dialog.show();
    }

}