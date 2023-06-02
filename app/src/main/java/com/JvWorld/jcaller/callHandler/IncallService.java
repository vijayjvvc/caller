package com.JvWorld.jcaller.callHandler;

import static com.JvWorld.jcaller.callHandler.GsmCall.Status.RINGING;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.calllog.CallHelper;
import com.JvWorld.jcaller.databaseHandler.DatabaseHelper;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.sql.SQLDataException;

import io.reactivex.disposables.Disposable;

public class IncallService extends InCallService {
    public static final String CHANNEL_IDS = "11";
    //    private static final String CHANNEL_IDSM = "12";
    public static final int notificationIncomingId = 11;
    public static final int notificationOutGoingId = 12;
    public static final int notificationOngoingId = 13;
    public static final int notificationMissed = 14;
    public CountDownTimer timerFlash;
    public NotificationManagerCompat notificationManager;
    boolean isFlashOn = false;
    //    Vibrator vibrator;
    boolean isFlasherStarted = false, timerOne = false,
            vibration = false;
    String name = "", number = "";
    //    String textDuration;
    boolean isCallActive = false;

    //    private Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    private Disposable updatesDisposable;
    private Call.Callback callCallback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            CallManager.updateCall(call, state);
        }
    };


    public IncallService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        startActivity(new Intent(this,CallActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCallAudioStateChanged(CallAudioState audioState) {
        super.onCallAudioStateChanged(audioState);
    }

    @Override
    public void onBringToForeground(boolean showDialpad) {
        super.onBringToForeground(showDialpad);
    }

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        CallListHelper.callList.add(call);
        CallManager.incallService = this;
        timerOne = true;
        CallManager.NUMBER_OF_CALLS = (CallListHelper.callList.size());
        call.registerCallback(callCallback);
        updatesDisposable = CallManager.updates()
                .subscribe(it -> {
                    createNotification((GsmCall) it, "added");
                });
        Call call1 = CallListHelper.callList.get(CallListHelper.callList.size() - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            CallManager.updateCall(call1, call1.getDetails().getState());
        } else {
            CallManager.updateCall(call1, call1.getState());
        }


    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        CallListHelper.callList.remove(call);
        if (vibration) {
            NotificationHelper.vibrationService(this).cancel();
            vibration = false;
        }
        if (NotificationHelper.toShowMissedNotification) {
            NotificationHelper.createMissedNotification(this, name, number);
        }
        if (isFlasherStarted) {
            flasher(false);
            timerFlash.cancel();
            isFlasherStarted = false;
        }
        if (CallListHelper.callList.size() > 0) {
            Call call1 = CallListHelper.callList.get(CallListHelper.callList.size() - 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                CallManager.updateCall(call1, call1.getDetails().getState());
            } else {
                CallManager.updateCall(call1, call1.getState());
            }
            createNotificationActive();
        } else {
            call.unregisterCallback(callCallback);
            CallManager.updateCall(null, 0);
            delAllNoti();
        }
    }

    private void createNotification(@NonNull GsmCall gsmCall, String call) {

        if (gsmCall.getDisplayName() != null) {
            name = getContactName(gsmCall.getDisplayName());
            number = gsmCall.getDisplayName();

            if (gsmCall.getStatus() == RINGING) {

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("activity1", MODE_PRIVATE);
                if (preferences.getBoolean("contactBlocker", false)) {
                    DatabaseManagerFav databaseManagerFav = new DatabaseManagerFav(this);
                    try {
                        databaseManagerFav.open();
                    } catch (SQLiteException | SQLDataException throwables) {
                        throwables.printStackTrace();
                    }

                    if (preferences.getBoolean("unknownBlocker", false) && name.equals("Unknown")) {
                        int ID = 1;
                        Cursor cursor = databaseManagerFav.fetchBlockNumbers();
                        if (cursor != null && cursor.moveToLast()) {
                            if (cursor.getPosition() == -1) {
                                databaseManagerFav.insertBlockNumbers(number, ID);
                            } else {
                                @SuppressLint("Range")
                                int a = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                                databaseManagerFav.insertBlockNumbers(number, ID + a);
                            }
                        } else {
                            databaseManagerFav.insertBlockNumbers(number, ID);
                        }
                        CallManager.disconnectCall();
                    }


                    if (databaseManagerFav.fetchOneBlock(number)) {
                        CallManager.disconnectCall();
                    }

                }
            }

        }

        switch (gsmCall.getStatus()) {
            case ACTIVE:
                isCallActive = true;
                NotificationHelper.toShowMissedNotification = false;
                createNotificationActive();
                break;
            case DIALING:
                NotificationHelper.toShowMissedNotification = false;
                createNotificationDialing();
                break;
            case RINGING:
                NotificationHelper.toShowMissedNotification = true;
                if (isCallActive) {
                    NotificationHelper.createNotificationNewCall(this, CHANNEL_IDS, name, number, notificationManager, notificationIncomingId);
                } else {
                    createNotificationRinging(gsmCall);
                }
                break;
        }

    }


    public void createNotificationActive() {

        if (isFlasherStarted) {
            flasher(false);
            timerFlash.cancel();
            isFlasherStarted = false;
        }
        if (vibration) {
            NotificationHelper.vibrationService(this).cancel();
            vibration = false;
        }

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, Outgoingcallui.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_IDS);
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle(name)
                .setContentText(number + "      ")
//                .setSound(ringtoneUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
//                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(false);
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(notificationIncomingId);
        notificationManager.cancel(notificationOngoingId);
        notificationManager.notify(notificationOutGoingId, builder.build());
    }

    private void createNotificationDialing() {

        if (vibration) {
            NotificationHelper.vibrationService(this).cancel();
            vibration = false;
        }

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, Outgoingcallui.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_IDS);
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle(name)
                .setContentText(number + "  Dialing.....")
//                .setSound(ringtoneUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
//                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationOngoingId, builder.build());
    }

    private void createNotificationRinging(@NonNull GsmCall gsmCall) {

        if (timerOne) {
            CountDownTimer timer = new CountDownTimer(2000, 1500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    NotificationHelper.vibrationService(IncallService.this);
//                    long[] vbPattern = new long[]{0,1000,1500,1000,1500,1000,1500,1000,1500,1000,1500
//                            ,1000,1500,1000,1500,1000,1500,1000,1500,1000,1500};
//                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    if (vibrator.hasVibrator()){
//                        vibrator.vibrate(vbPattern,-1);
                    vibration = true;
//                    }
                }
            }.start();
            if (CallHelper.flashToggle(getApplicationContext())) {
                flasherOnCall();
                isFlasherStarted = true;
                timerOne = false;
            }
        }


        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, Incomingcallui.class);
        PendingIntent pending = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_IDS);
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle(name)
//                .addAction(R.drawable.ic_call_end,"",CallManager.acceptCall())
                .setContentText(number + " Incoming....").setSound(ringtoneUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setLights(Color.WHITE, 3000, 3000)
                .setFullScreenIntent(pending, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pending)
                .setAutoCancel(false);
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationIncomingId, builder.build());

        switch (gsmCall.getStatus()) {
            case ACTIVE:
                notificationManager.cancel(notificationOutGoingId);
                break;
            case RINGING:
                notificationManager.cancel(notificationOngoingId);
                notificationManager.cancel(notificationOutGoingId);
                break;
        }


    }

    private void flasherOnCall() {

        int toFlashMode = CallHelper.flashSeekOnToggle(getApplicationContext()) * 100;
        timerFlash = new CountDownTimer(30000, toFlashMode) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFlasherStarted) {
                    flasher(false);
                    timerFlash.cancel();
                }
                if (isFlashOn) {
                    flasher(false);
                    isFlashOn = false;
                } else {
                    flasher(true);
                    isFlashOn = true;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void flasher(boolean bool) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, bool);
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, e.getReason(), Toast.LENGTH_SHORT).show();
        }
    }

    public void delAllNoti() {
        notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.cancelAll();
        notificationManager.cancel(notificationIncomingId);
        notificationManager.cancel(notificationOngoingId);
        notificationManager.cancel(notificationOutGoingId);
    }

    @SuppressLint("Range")
    private String getContactName(String Number) {
        String number = Number;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");

//        String name = "Unknown";
//        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
//                ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");
////        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(number));
////        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
////        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
//
//        while (cursor.moveToNext()){
//            String no = cursor.getString(cursor.getColumnIndex(ContactsContract
//                    .CommonDataKinds.Phone.NUMBER));
//            no = no.replaceAll(" ","");
//            no = no.replaceAll("  ","");
//            no = no.replaceAll("-","");
//            if (no.length()>=6){
//                String va = no.substring(0,3);
//                if (va.equals("+91")) {
//                    int aa = no.length();
//                    no = "+91 " + no.substring(3, aa);
//                }else if (no.length()==10){
//                    no = "+91 " + no;
//                }
//            }
//            if (no.equals(number)){
//                name = cursor.getString(cursor.getColumnIndex(ContactsContract
//                        .CommonDataKinds.Phone.DISPLAY_NAME));
//                break;
//            }
//        }
        return StcVariable.getContactNameByNumber(number, this);
    }

    @Override
    public void onDestroy() {
        isFlasherStarted = false;
        super.onDestroy();
    }
}
