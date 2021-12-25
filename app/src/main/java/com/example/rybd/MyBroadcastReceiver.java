package com.example.rybd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,MainActivity.class);
        Bundle extra = intent.getExtras();
        String judul = extra.getString("judul");
        Integer jumlah = extra.getInt("jumlah");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login",context.MODE_PRIVATE);
        String mEmail = sharedPreferences.getString("username","");
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("open","true");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivities(context,jumlah, new Intent[]{intent},0);
        PendingIntent pendingIntent1 = PendingIntent.getActivities(context,1, new Intent[]{intent1},PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"alarm")
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setContentTitle("Peringatan!")
                .setContentText(judul).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.icon,"open",pendingIntent1);
        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.start();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
        myref.child(mEmail).child(jumlah.toString()).child("aktif").setValue(0);
        context.startService(i);


    }
}
