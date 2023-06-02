package com.JvWorld.jcaller.callHandler;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;

import java.net.ContentHandler;

public class NotificationHelper {

    public static boolean toShowMissedNotification = false;


    public static Vibrator vibrationService(Context context) {

        long[] vbPattern = new long[]{0, 1000, 1500, 1000, 1500, 1000, 1500, 1000, 1500, 1000, 1500
                , 1000, 1500, 1000, 1500, 1000, 1500, 1000, 1500, 1000, 1500};
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(vbPattern, -1);
        }
        return vibrator;
    }


    public static void createMissedNotification(Context context, String name, String number) {
        NotificationManagerCompat notificationManager;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "11");
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle(name)
                .setContentText(number + "      Missed")
                .setSound(notificationUri)
                .setLights(Color.GREEN, 3000, 3000)
                .setVibrate(new long[]{1000, 1000})
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(false)
//                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(false);
        notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.cancel(11);
//        notificationManager.cancel(13);
        notificationManager.notify(14, builder.build());
    }


    public static void createNotificationNewCall(Context context, String CHANNEL_IDS, String name, String number, NotificationManagerCompat notificationManager, int notificationIncomingId) {

//        long[] vbPattern = new long[]{0,1000,1500,1000,1500,1000,1500,1000,1500,1000,1500
//                ,1000,1500,1000,1500,1000,1500,1000,1500,1000,1500};
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        if (vibrator.hasVibrator()){
//            vibrator.vibrate(vbPattern,-1);
//        }

        vibrationService(context);

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, Incomingcallui.class);
        PendingIntent pending = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_IDS);
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle(name)
//                .addAction(R.drawable.ic_call_end,"",CallManager.acceptCall())
                .setContentText(number).setSound(notificationUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setLights(Color.WHITE, 3000, 3000)
                .setFullScreenIntent(pending, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pending)
                .setAutoCancel(false);
        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationIncomingId, builder.build());
    }


}
