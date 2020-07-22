package com.example.memoriesglasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //BroadcastReceiver receiver;
    //Uri notification;
    //Ringtone r;
    Button btnStart;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo;
    TextView slogan;

    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_one");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.txtLogo);
        slogan = findViewById(R.id.txtSolgan);

        image.setAnimation(bottomAnim);
        logo.setAnimation(topAnim);
        slogan.setAnimation(topAnim);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });



        /*receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                databaseReference.child("Alert").setValue(1);
               try {
                   Toast.makeText(context, "PATIENT ISN'T HOME", Toast.LENGTH_SHORT).show();
               }catch (Exception e) {
                   e.printStackTrace();
               }
            }
        };*/

        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String latitude2 = dataSnapshot.child("Somaya").child("latitude").getValue().toString();
                String longitude2 = dataSnapshot.child("Somaya").child("longitude").getValue().toString();


                Double latCurrent = Double.parseDouble(latitude2);
                Double lngCurrent = Double.parseDouble(longitude2);

                String latitude1 = dataSnapshot.child("Home").child("latitude").getValue().toString();
                String longitude1 = dataSnapshot.child("Home").child("longitude").getValue().toString();

                Double latHome = Double.parseDouble(latitude1);
                Double lngHome = Double.parseDouble(longitude1);

                getMetersFromLatLong(latHome, lngHome, latCurrent, lngCurrent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public double getMetersFromLatLong(double lat1, double lng1, double lat2, double lng2) {

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

        if(distBetweenTwoLocations >= 50){
            databaseReference.child("Alert").setValue(1);
            //Intent i = new Intent(MainActivity.this, MyReceiver.class);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, i, 0);
            notificationCall();
        }
        else{
            databaseReference.child("Alert").setValue(0);
        }

        return distBetweenTwoLocations;

    }

    /*private void sendBroadcast(PendingIntent pendingIntent) {
        databaseReference.child("Alert").setValue(1);
    }*/


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void notificationCall(){
        Intent intent = new Intent(this, RetrieveMapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL).setSmallIcon(R.drawable.imageedit_35_6864498887__1_)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.imageedit_35_6864498887__1_))
                .setContentTitle("Location Changed")
                .setContentText("Tap to get Current Location")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

}