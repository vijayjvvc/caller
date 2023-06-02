package com.JvWorld.jcaller.callHandler;

import static com.JvWorld.jcaller.callHandler.GsmCall.Status.UNKNOWN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.simDector.SimSettings;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class Incomingcallui extends AppCompatActivity {


    String name, number;
    private ImageView profile;
    private Disposable updatesDisposable;
    private SeekBar callBtn, declinebtn;
    private TextView incomingName, incomingNumber, incomingStatus;
    private TextView msgReject1, msgReject2, msgReject3, msgReject4, msgReject5, msgText;
    private RelativeLayout rejectLayout, callUi;

    //    boolean isMissedCall = false;
    private ImageView cancelUi, msgBtn;

    private void InitUi() {

        callBtn = findViewById(R.id.pick_call_btn);
        declinebtn = findViewById(R.id.end_call_btn);
        incomingName = findViewById(R.id.name_ui_show);
        incomingNumber = findViewById(R.id.number_ui_show);
        profile = findViewById(R.id.incoming_call_ui_profile);
        incomingStatus = findViewById(R.id.status_ui_show);

        ////////////////// Reject msg ui variable .................
        msgReject1 = findViewById(R.id.reject_msg1);
        msgReject2 = findViewById(R.id.reject_msg2);
        msgReject3 = findViewById(R.id.reject_msg3);
        msgReject4 = findViewById(R.id.reject_msg4);
        msgReject5 = findViewById(R.id.reject_msg5);
        String qu1 = SimSettings.quickRes(this, 1);
        String qu2 = SimSettings.quickRes(this, 2);
        String qu3 = SimSettings.quickRes(this, 3);
        String qu4 = SimSettings.quickRes(this, 4);
        String qu5 = SimSettings.quickRes(this, 5);

        if (!qu1.equals("")) {
            msgReject1.setText(qu1);
        }
        if (!qu2.equals("")) {
            msgReject2.setText(qu2);
        }
        if (!qu3.equals("")) {
            msgReject3.setText(qu3);
        }
        if (!qu4.equals("")) {
            msgReject4.setText(qu4);
        }
        if (!qu5.equals("")) {
            msgReject5.setText(qu5);
        }

        msgText = findViewById(R.id.msg);
        rejectLayout = findViewById(R.id.reject_msg_layout);
        callUi = findViewById(R.id.incoming_call_ui);
        cancelUi = findViewById(R.id.cancel_msg_reject);
        msgBtn = findViewById(R.id.reject_call_msg);

        rejectHandler();
        ///////////////////////////////////////////////////////////


        updatesDisposable = CallManager.updates()
                .doOnError(throwable -> {
                    Log.e("LOG_TAg", "ERROR");
                })
                .subscribe(it -> {
                    updateView((GsmCall) it);
                });

    }

    private void rejectHandler() {
        msgBtn.setOnClickListener(view -> {
            rejectLayout.setVisibility(View.VISIBLE);
            callUi.setVisibility(View.INVISIBLE);
        });
        cancelUi.setOnClickListener(view -> {
            rejectLayout.setVisibility(View.INVISIBLE);
            callUi.setVisibility(View.VISIBLE);
        });
        msgReject1.setOnClickListener(view -> {
            String msgRejectIs = msgReject1.getText().toString();
            CallManager.rejectCallMsg(msgRejectIs);
        });
        msgReject2.setOnClickListener(view -> {
            String msgRejectIs = msgReject2.getText().toString();
            CallManager.rejectCallMsg(msgRejectIs);
        });
        msgReject3.setOnClickListener(view -> {
            String msgRejectIs = msgReject3.getText().toString();
            CallManager.rejectCallMsg(msgRejectIs);
        });
        msgReject4.setOnClickListener(view -> {
            String msgRejectIs = msgReject4.getText().toString();

            CallManager.rejectCallMsg(msgRejectIs);
        });
        msgReject5.setOnClickListener(view -> {
            String msgRejectIs = msgReject5.getText().toString();

            CallManager.rejectCallMsg(msgRejectIs);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomingcallui);

        hideBottomNavigationBar();
        InitUi();
        btnHandler();


    }


    private void btnHandler() {
        callBtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int a = seekBar.getProgress();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = seekBar.getProgress();
                if (a >= 69) {
                    CallManager.acceptCall();
                    startActivity(new Intent(Incomingcallui.this, Outgoingcallui.class));
                    finish();

                } else {
                    seekBar.setProgress(5);
                }
            }
        });

        declinebtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = seekBar.getProgress();
                if (a <= 31) {
                    CallManager.cancelCall();
                    NotificationHelper.toShowMissedNotification = false;
                } else {
                    seekBar.setProgress(95);
                }
            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void updateView(@NonNull GsmCall gsmCall) {
        switch (gsmCall.getStatus()) {
            case RINGING:
                incomingStatus.setText("Incoming call..");
                break;
            case DISCONNECTED:

                incomingStatus.setText("Disconnecting....");
                finish();
                break;
            case ACTIVE:

            case DIALING:
                break;
        }
        if (gsmCall.getStatus() == GsmCall.Status.RINGING) {
            incomingStatus.setText("Incoming call");
        }

        if (gsmCall.getDisplayName() == null) {
            incomingName.setText("Unknown");
            incomingNumber.setText("Private");
        } else {
            number = gsmCall.getDisplayName();
            name = getContactName(gsmCall.getDisplayName());
            incomingName.setText(getContactName(gsmCall.getDisplayName()));
            incomingNumber.setText(gsmCall.getDisplayName());
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // only for gingerbread and newer versions
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
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
//
//        while (cursor.moveToNext()){
//            String no = cursor.getString(cursor.getColumnIndex(ContactsContract
//                    .CommonDataKinds.Phone.NUMBER));
//            no = no.replaceAll(" ","");
//            no = no.replaceAll("  ","");
//            no = no.replaceAll("-","");
//            if (no.equals(number)){
//                String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//                if (photo!=null){
//                    Glide.with(getApplicationContext()).load(photo).into(profile);
//                }
//                name = cursor.getString(cursor.getColumnIndex(ContactsContract
//                        .CommonDataKinds.Phone.DISPLAY_NAME));
//                break;
//            }
//        }
        return StcVariable.getContactNameByNumber(number, this);
    }

}
