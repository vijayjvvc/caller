package com.JvWorld.jcaller.callHandler;

import static com.JvWorld.jcaller.callHandler.GsmCall.Status.UNKNOWN;
import static com.JvWorld.jcaller.callHandler.IncallService.CHANNEL_IDS;
import static com.JvWorld.jcaller.callHandler.IncallService.notificationIncomingId;
import static com.JvWorld.jcaller.callHandler.IncallService.notificationOngoingId;
import static com.JvWorld.jcaller.callHandler.IncallService.notificationOutGoingId;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class Outgoingcallui extends AppCompatActivity {

    boolean merge = false;
    String name1 = "", number2 = "";
    TextView callMergeAdd;
    private ImageView endbtn, holdbtn, profile, muteBtn, conShowBtn, recordBtn, speakerOnBtn, dialPadBtn;
    private Disposable timerDisposable, disposable;
    private TextView textDuration, textStatus, textDisplayName, textNumber;
    private AudioManager audioManager;
    private MediaRecorder mediaRecorder;


    private boolean timerStart = false, holdStart = false,
            muteStart = false, speakerStart = false, recStart = false, dialStart = false,
            conStart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoingcallui);
        hideBottomNavigationBar();
        inItUI();
        btnHandler();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void inItUI() {


        ////////////////         ongoing call  ...........////////
        endbtn = findViewById(R.id.end_active_call_btn);
        textDuration = findViewById(R.id.textDuration);
        textStatus = findViewById(R.id.textStatus);
        textDisplayName = findViewById(R.id.textDisplayName);
        textNumber = findViewById(R.id.text_number);
        holdbtn = findViewById(R.id.hold_active_call_btn);
        muteBtn = findViewById(R.id.mute_active_call_btn);
        speakerOnBtn = findViewById(R.id.speak_active_call_btn);
        recordBtn = findViewById(R.id.record_active_call_btn);
        dialPadBtn = findViewById(R.id.dial_active_call_btn);
        conShowBtn = findViewById(R.id.con_active_call_btn);
        profile = findViewById(R.id.incoming_call_ui_profile);
        callMergeAdd = findViewById(R.id.add_call_merge);

        conShowBtn.setClickable(false);
        conShowBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
        holdbtn.setClickable(false);
        holdbtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
        muteBtn.setClickable(false);
        muteBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
        recordBtn.setClickable(false);
        recordBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));


        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        //////////////// Visibility layout ......//////////
    }


    private void btnHandler() {

        endbtn.setOnClickListener(view -> {
            CallManager.cancelCall();
        });

        holdbtn.setOnClickListener(view -> {
            if (!holdStart) {
                CallManager.holdCall();
                if (CallManager.holdCall()) {
                    holdbtn.setBackgroundResource(R.drawable.circular_back);
                    holdbtn.setImageTintList(getColorStateList(R.color.black));
                    holdbtn.setImageResource(R.drawable.ic_in_call_touch_round_hold);
                    holdStart = true;
                }
            } else {
                CallManager.unHoldCall();
                if (CallManager.unHoldCall()) {
                    holdbtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    holdbtn.setImageTintList(getColorStateList(R.color.white));
                    holdbtn.setImageResource(R.drawable.ic_in_call_touch_round_hold);
                    holdStart = false;
                }
            }

        });

        muteBtn.setOnClickListener(view -> {

            if (!muteStart) {
                audioManager.setMicrophoneMute(true);
                if (audioManager.isMicrophoneMute()) {
                    muteBtn.setBackgroundResource(R.drawable.circular_back);
                    muteBtn.setImageTintList(getColorStateList(R.color.black));
                    muteBtn.setImageResource(R.drawable.ic_in_call_touch_mute);
                    muteStart = true;
                }

            } else {
                audioManager.setMicrophoneMute(false);
                if (!audioManager.isMicrophoneMute()) {
                    muteBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    muteBtn.setImageTintList(getColorStateList(R.color.white));
                    muteBtn.setImageResource(R.drawable.ic_in_call_touch_mute);
                    muteStart = false;
                }
            }

        });


        speakerOnBtn.setOnClickListener(view -> {
            audioManager.isWiredHeadsetOn();
            if (audioManager.isWiredHeadsetOn() || audioManager.isBluetoothScoOn()) {

                //////////////// this is for all connected devices available for audio route


            } else {
                if (!speakerStart) {
                    CallManager.speakerCallOn("speaker");
//                if (audioManager.isSpeakerphoneOn()){
                    speakerOnBtn.setBackgroundResource(R.drawable.circular_back);
                    speakerOnBtn.setImageTintList(getColorStateList(R.color.black));
                    speakerOnBtn.setImageResource(R.drawable.global_speaker_off_pressed);
                    speakerStart = true;
//                }
                } else {
                    CallManager.speakerCallOn("earphone");
//                if (!audioManager.isSpeakerphoneOn()){
                    speakerOnBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    speakerOnBtn.setImageTintList(getColorStateList(R.color.white));
                    speakerOnBtn.setImageResource(R.drawable.global_speaker_off_pressed);
                    speakerStart = false;
//                }

                }
            }


        });

        recordBtn.setOnClickListener(view -> {

            if (!recStart) {
                mediaRecorder = new MediaRecorder();
                try {

//                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION); //no crash no record
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);   // crash no record

//                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
//                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
//                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_PERFORMANCE);//high
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);//crash
//                    }else {
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                    }
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(getFilePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();

                    recordBtn.setBackgroundResource(R.drawable.circular_back);
                    recordBtn.setImageTintList(getColorStateList(R.color.black));
                    recordBtn.setImageResource(R.drawable.ic_in_call_touch_start_record);
                    Toast.makeText(Outgoingcallui.this, "Recording on", Toast.LENGTH_SHORT).show();
                    recStart = true;
                } catch (IOException e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    ;
                }
            } else {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                recordBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                recordBtn.setImageTintList(getColorStateList(R.color.white));
                recordBtn.setImageResource(R.drawable.ic_in_call_touch_start_record);
                Toast.makeText(Outgoingcallui.this, "Recording off", Toast.LENGTH_SHORT).show();
                recStart = false;
            }

        });

        conShowBtn.setOnClickListener(view -> {
            if (!merge) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                conShowBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                conShowBtn.setImageResource(R.drawable.ic_in_call_touch_add_call_disable);
                merge = false;
            } else {
                CallManager.mergeCall();
                if (CallManager.mergeCall()) {
                    conShowBtn.setBackgroundResource(R.drawable.circular_back);
                    conShowBtn.setImageResource(R.drawable.ic_in_call_touch_merge_pressed);
                    callMergeAdd.setText("Merged");
                    textDisplayName.setText("Conference Call");
                    textNumber.setVisibility(View.INVISIBLE);
                    conShowBtn.setClickable(false);
                    merge = false;
                    createNotificationActive();
                } else {
                    conShowBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    conShowBtn.setImageResource(R.drawable.ic_in_call_touch_merge_normal);
                }
            }

        });

        dialPadBtn.setOnClickListener(view -> {
            if (!dialStart) {
                dialPadBtn.setBackgroundResource(R.drawable.circular_back);
                dialPadBtn.setImageResource(R.drawable.global_dialpad_pressed);
                Toast.makeText(Outgoingcallui.this, "DialPad pressed", Toast.LENGTH_SHORT).show();
                dialStart = true;
            } else {
                dialPadBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                dialPadBtn.setImageResource(R.drawable.global_dialpad_normal);
                Toast.makeText(Outgoingcallui.this, "DialPad off", Toast.LENGTH_SHORT).show();
                dialStart = false;
            }

        });

    }

    private String getFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File recordingFiles = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        Date date = new Date();
        String A = name1 + number2 + String.valueOf(date);
        File file = new File(recordingFiles, A + ".mp3");
        return file.getPath();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int a = CallListHelper.callList.size();
        if (audioManager.isMicrophoneMute()) {
            muteBtn.setBackgroundResource(R.drawable.circular_back);
            muteBtn.setImageTintList(getColorStateList(R.color.black));
            muteBtn.setImageResource(R.drawable.ic_in_call_touch_mute);
            muteStart = true;
        } else {
            muteBtn.setBackgroundResource(R.drawable.circular_back_transpart);
            muteBtn.setImageTintList(getColorStateList(R.color.white));
            muteBtn.setImageResource(R.drawable.ic_in_call_touch_mute);
            muteStart = false;
        }
        if (!audioManager.isSpeakerphoneOn()) {
            screenProximity();
            speakerOnBtn.setBackgroundResource(R.drawable.circular_back_transpart);
        } else {
            speakerOnBtn.setBackgroundResource(R.drawable.circular_back);
            speakerOnBtn.setImageTintList(getColorStateList(R.color.black));
        }
        speakerOnBtn.setImageResource(R.drawable.global_speaker_off_pressed);
        if (a > 1) {
            conShowBtn.setBackgroundResource(R.drawable.circular_back_transpart);
            conShowBtn.setImageResource(R.drawable.ic_in_call_touch_merge_normal);
            callMergeAdd.setText("Merge calls");
            merge = true;
        } else {
            merge = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposable = CallManager.updates().doOnError(throwable -> {
        }).subscribe(it -> {
            updateView((GsmCall) it);
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void updateView(GsmCall gsmCall) {

        Toast.makeText(this, String.valueOf(gsmCall.getStatus()), Toast.LENGTH_SHORT).show();

        switch (gsmCall.getStatus()) {
            case CONNECTING:
                textStatus.setText("Connecting…");
                break;
            case DIALING:
                conShowBtn.setClickable(false);
                holdbtn.setClickable(false);
                muteBtn.setClickable(false);
                recordBtn.setClickable(false);

                conShowBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
                holdbtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
                muteBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));
                recordBtn.setForeground(getResources().getDrawable(R.drawable.circular_disable));

                textStatus.setText("Calling…");
                break;
            case ACTIVE:
                conShowBtn.setClickable(true);
                holdbtn.setClickable(true);
                muteBtn.setClickable(true);
                recordBtn.setClickable(true);

                conShowBtn.setForeground(getResources().getDrawable(R.drawable.circular_enable));
                holdbtn.setForeground(getResources().getDrawable(R.drawable.circular_enable));
                muteBtn.setForeground(getResources().getDrawable(R.drawable.circular_enable));
                recordBtn.setForeground(getResources().getDrawable(R.drawable.circular_enable));

                int a = CallListHelper.callList.size();

                if (a > 1) {
                    conShowBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    conShowBtn.setImageResource(R.drawable.ic_in_call_touch_merge_normal);
                    callMergeAdd.setText("Merge calls");
                    merge = true;
                } else {
//                    if (!merge){
//                        Call call = CallListHelper.callList.get(a - 1);
//                        call.unhold();
//                    }
                    callMergeAdd.setText("add call");
                    conShowBtn.setBackgroundResource(R.drawable.circular_back_transpart);
                    conShowBtn.setImageResource(R.drawable.ic_in_call_touch_add_call_disable);
                    merge = false;
                }
                textStatus.setText("OngoingCall");
                if (!timerStart) {
                    timerStart = true;
                    startTimer();
                }
                break;
            case DISCONNECTED:
                if (!merge) {
                    textStatus.setText("Finished call");
                    if (timerStart) {
                        timerStart = false;
                        timerDisposable.dispose();
//                    stopTimer();
                    }
                    endbtn.postDelayed(this::finish, 2000);
                } else {
                    textStatus.setText("Conference Call");
                    textDisplayName.setText("Conference Call");
                }
                break;
            case UNKNOWN:
                textStatus.setText(String.valueOf(UNKNOWN));
                break;
        }


        if (gsmCall.getDisplayName() == null) {
            textDisplayName.setText("Unknown");
            textNumber.setText("Private");
        } else {
            number2 = gsmCall.getDisplayName().toString();
            name1 = getContactName(gsmCall.getDisplayName());
            textNumber.setText(gsmCall.getDisplayName());
            textDisplayName.setText(getContactName(gsmCall.getDisplayName()));
        }
    }

    private void hideBottomNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void startTimer() {
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(longTimeInterval -> {
                    textDuration.setText(toDurationString(longTimeInterval));
                });
    }

//    private void stopTimer() {
//        timerDisposable.dispose();
//    }


    private String toDurationString(Long l) {
        return String.format("%02d:%02d:%02d", l / 3600, (l % 3600) / 60, (l % 60));
    }

    @SuppressLint("Range")
    private String getContactName(String Number) {
        String number = Number;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
        if (number.length() >= 6) {
            String v = number.substring(0, 3);
            if (v.equals("+91")) {
                int a = number.length();
                number = "+91 " + number.substring(3, a);
            } else if (number.length() == 10) {
                number = "+91 " + number;
            }
        }
        String name = "Private";
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {
            String no = cursor.getString(cursor.getColumnIndex(ContactsContract
                    .CommonDataKinds.Phone.NUMBER));
            no = no.replaceAll(" ", "");
            no = no.replaceAll("  ", "");
            no = no.replaceAll("-", "");
            if (no.length() >= 6) {
                String va = no.substring(0, 3);
                if (va.equals("+91")) {
                    int aa = no.length();
                    no = "+91 " + no.substring(3, aa);
                } else if (no.length() == 10) {
                    no = "+91 " + no;
                }
            }
            if (no.equals(number)) {
                String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                if (photo != null) {
                    Glide.with(getApplicationContext()).load(photo).into(profile);
                }
                name = cursor.getString(cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.DISPLAY_NAME));
                break;
            }
        }
        return name;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "Press Home Button to resume call.", Toast.LENGTH_SHORT).show();

    }

    public void createNotificationActive() {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, Outgoingcallui.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_IDS);
        builder.setSmallIcon(R.drawable.ic_call)
                .setContentTitle("Conference")
//                .setSound(ringtoneUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
                .setFullScreenIntent(pendingIntent, true)
                .setContentIntent(pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.cancel(notificationIncomingId);
//        notificationManager.cancel(notificationOngoingId);
//        notificationManager.cancel(notificationOutGoingId);
        notificationManager.cancelAll();
        notificationManager.notify(1311, builder.build());
    }

    private void screenProximity() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        SensorEventListener proximitySensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {

                    if (sensorEvent.values[0] == 0) {
//                        textView.setText("Near");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                            setShowWhenLocked(false);
                            setTurnScreenOn(false);
                        } else {
                            getWindow().addFlags(WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
                        }

                    } else {
//                        textView.setText("Away");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                            setShowWhenLocked(true);
                            setTurnScreenOn(true);
                        } else {
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                        }
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        if (sensor == null) {
            Toast.makeText(this, "No proximity", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(proximitySensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}