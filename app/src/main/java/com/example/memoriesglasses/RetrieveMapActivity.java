package com.example.memoriesglasses;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RetrieveMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //final String[] Tag = new String[1];

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_one");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String latitude2 = dataSnapshot.child("Somaya").child("latitude").getValue().toString();
                String longitude2 = dataSnapshot.child("Somaya").child("longitude").getValue().toString();


                Double latCurrent = Double.parseDouble(latitude2);
                Double lngCurrent = Double.parseDouble(longitude2);

                LatLng location = new LatLng(latCurrent, lngCurrent);

                mMap.addMarker(new MarkerOptions().position(location).title(getAddress(location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14F));

                //Tag[0] = "Location is: " + latCurrent + ", " + lngCurrent;
                //Toast.makeText(RetrieveMapActivity.this, Tag[0], Toast.LENGTH_LONG).show();

                String latitude1 = dataSnapshot.child("Home").child("latitude").getValue().toString();
                String longitude1 = dataSnapshot.child("Home").child("longitude").getValue().toString();

                //Double latHome = Double.parseDouble(latitude1);
                //Double lngHome = Double.parseDouble(longitude1);



                //getMetersFromLatLong(latHome, lngHome, latCurrent, lngCurrent);

                //getAddress(location);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



        /*public double getMetersFromLatLong(double lat1, double lng1, double lat2, double lng2) {

            double theta = lng1 - lng2;
            double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.6 * 1000;
            int distBetweenTwoLocations = (int) Math.rint(dist);

            String Tag = String.valueOf(distBetweenTwoLocations);

            if(dist >= 200){
                Toast.makeText(this, "The Distance between them is : "+ Tag + " meters", Toast.LENGTH_LONG).show();
                notificationCall();
            }
            else{
                Toast.makeText(this, Tag, Toast.LENGTH_SHORT).show();
            }

            return dist;

        }



    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/


        private String getAddress(LatLng homeLocation) {
            String homeAddress = "";
            Geocoder geocoder = new Geocoder(RetrieveMapActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(homeLocation.latitude, homeLocation.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                homeAddress = addresses.get(0).getLocality();
                String myAddress = address.toString();

                //mMap.addMarker(new MarkerOptions().position(homeLocation).title(myAddress));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLocation, 14F));


            } catch (IOException e) {
                e.printStackTrace();
            }
            return homeAddress;
        }
    /*public void notificationCall(){
        Intent intent = new Intent(this, RetrieveMapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL).setSmallIcon(R.drawable.pd)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.pd))
                .setContentTitle("Location Changed")
                .setContentText("Tap to get Current Location")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }*/

}
