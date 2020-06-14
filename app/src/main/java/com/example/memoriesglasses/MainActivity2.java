package com.example.memoriesglasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
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

public class MainActivity2 extends AppCompatActivity {

    TextView home;
    Button btnHome;
    Button btnCurrent;
    Button btnInfo;
    public static final String ADDRESS = "ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnInfo = findViewById(R.id.btnInfo);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(infoIntent);
            }

        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_one");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String latitude2 = dataSnapshot.child("Home").child("latitude").getValue().toString();
                String longitude2 = dataSnapshot.child("Home").child("longitude").getValue().toString();


                Double latCurrent = Double.parseDouble(latitude2);
                Double lngCurrent = Double.parseDouble(longitude2);

                LatLng location = new LatLng(latCurrent, lngCurrent);
                getAddress(location);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    /*public void btnHome(View view){
        startActivity(new Intent(this ,MapsActivity.class));

    }
    public void btnRetrieveLocation(View view){
        startActivity(new Intent(getApplicationContext() , RetrieveMapActivity.class));

    }*/

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
          outState.putString(ADDRESS, String.valueOf(homeAddress));
    }*/
         btnHome = findViewById(R.id.btnHome);
         btnHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity2.this, MapsActivity.class);
                 startActivity(intent);
             }
         });
         btnCurrent = findViewById(R.id.btnCurrent);
         btnCurrent.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity2.this, RetrieveMapActivity.class);
                 startActivity(intent);
             }
         });

    }
    private String getAddress(LatLng homeLocation) {
        String homeAddress = "";
        Geocoder geocoder = new Geocoder(MainActivity2.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(homeLocation.latitude, homeLocation.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            homeAddress = addresses.get(0).getLocality();
            String myAddress = address.toString();
            String ADDRESS = "ADDRESS";
            home = findViewById(R.id.txtAddress);
            home.setText(myAddress);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return homeAddress;
    }
}