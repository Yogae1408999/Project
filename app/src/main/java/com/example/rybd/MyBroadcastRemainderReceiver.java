package com.example.rybd;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastRemainderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,MainActivity.class);
        Bundle extra = intent.getExtras();
        String judul = extra.getString("judul");
        Integer jumlah = extra.getInt("jumlah");
        String remainder = extra.getString("waktu");
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("open","true");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivities(context,jumlah, new Intent[]{intent},0);
        PendingIntent pendingIntent1 = PendingIntent.getActivities(context,1, new Intent[]{intent1},PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"alarm")
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setContentTitle(judul)
                .setContentText("Kegiatan Akan dimulai dalam "+remainder).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.icon,"open",pendingIntent1);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
        context.startService(i);
    }
}
