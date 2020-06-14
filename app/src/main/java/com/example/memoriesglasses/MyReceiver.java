package com.example.memoriesglasses;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MyReceiver extends BroadcastReceiver {
    static MainActivity m = new MainActivity();
    Uri notification;
    Ringtone r;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

       m.databaseReference.child("Alert").setValue(1);

        /*try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(context, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                r.play();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {



                        r.stop();


                    }
                }, 8000);

            }
        }, 210000);
    }*/




}
}
