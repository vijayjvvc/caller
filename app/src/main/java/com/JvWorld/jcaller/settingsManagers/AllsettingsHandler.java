package com.JvWorld.jcaller.settingsManagers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.calllog.CallHelper;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.contacts.Demodel;
import com.JvWorld.jcaller.databaseHandler.DatabaseHelper;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.simDector.SimSettings;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.SQLDataException;
import java.util.ArrayList;

public class AllsettingsHandler extends AppCompatActivity implements SpeedDial {

    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 11;
    RelativeLayout speedDial, callBlock;
    int whichLayout;
    private ImageView num1, num2, num3, num4, num5, num6, num7, num8, num9, settingBack;
    private DatabaseManagerFav databaseManagerFav;
    private SettingsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private LinearLayout layoutS;
    private int _ID;
    private TextView settingsEditSave;
    ///////////////////////////////////////////////////////////
    private SwitchCompat callBlockerOnOff, unknownCallBlockerOnOff;
    private RelativeLayout rrb2;
    private RecyclerView recyclerBlockNumbers;
    ///////////////////////////////////////////////////////////
    private ConstraintLayout flashLayout;
    private SeekBar lightOnSeekBar;
    private SwitchCompat flashOnSwitch;
    private TextView textFlashBtn;
    private boolean isFlashOn = false;
    private RelativeLayout lightOnLayout,
    //            lightOffLayout,
    testLayout;
    ///////////////////////////////////////////////////////////

    private ConstraintLayout general;
    private SwitchCompat defaultDialer;
    private ImageView sim1, sim2, noSimSet;


    ///////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    private ConstraintLayout quickResponseLayout;
    private ImageView edit1, edit2, edit3, edit4, edit5;
    private TextView msgReject1, msgReject2, msgReject3, msgReject4, msgReject5;
    ///////////////////////////////////////////////////////////

    private void initUi() {

        settingBack = findViewById(R.id.settings_back);
        settingBack.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        settingsEditSave = findViewById(R.id.settings_edit_save);

        ////////  speed dial ...................
        speedDial = findViewById(R.id.sp4);
        num1 = findViewById(R.id.num_1);
        num2 = findViewById(R.id.num_2);
        num3 = findViewById(R.id.num_3);
        num4 = findViewById(R.id.num_4);
        num5 = findViewById(R.id.num_5);
        num6 = findViewById(R.id.num_6);
        num7 = findViewById(R.id.num_7);
        num8 = findViewById(R.id.num_8);
        num9 = findViewById(R.id.num_9);
        layoutS = findViewById(R.id.sp1);
        recyclerView = findViewById(R.id.recycler_view_speedDial);
        ///////  end ..........................

        ////////  call block  .................
        callBlock = findViewById(R.id.cb5);
        callBlockerOnOff = findViewById(R.id.block_on_off_swt);
        rrb2 = findViewById(R.id.rrb2);
        recyclerBlockNumbers = findViewById(R.id.recycler_view_blocked_numbers);
        unknownCallBlockerOnOff = findViewById(R.id.block_unknown_on_off_swt);
        ///////  end ..........................

        ////////  quick settings  ...........
        msgReject1 = findViewById(R.id.reject_msg1);
        msgReject2 = findViewById(R.id.reject_msg2);
        msgReject3 = findViewById(R.id.reject_msg3);
        msgReject4 = findViewById(R.id.reject_msg4);
        msgReject5 = findViewById(R.id.reject_msg5);

        quickResponseLayout = findViewById(R.id.quick_response_layout);

        edit1 = findViewById(R.id.quick_edit_1);
        edit2 = findViewById(R.id.quick_edit_2);
        edit3 = findViewById(R.id.quick_edit_3);
        edit4 = findViewById(R.id.quick_edit_4);
        edit5 = findViewById(R.id.quick_edit_5);

        ///////  end ..........................


        ////////  general settings  ...........

        general = findViewById(R.id.general_sim);
        sim1 = findViewById(R.id.sim_1_system_default);
        sim2 = findViewById(R.id.sim_2_system_default);
        noSimSet = findViewById(R.id.sim_system_default);
        defaultDialer = findViewById(R.id.default_mode_on_off);

        ///////  end ..........................

        //////// flash start  .................
        flashLayout = findViewById(R.id.flash_layout);
//        lightOffSeekBar = findViewById(R.id.light_off_seek_bar);
        lightOnSeekBar = findViewById(R.id.light_on_seek_bar);
        flashOnSwitch = findViewById(R.id.flash_mode_on_off);
        lightOnLayout = findViewById(R.id.light_on_layout);
//        lightOffLayout = findViewById(R.id.light_off_layout);
        textFlashBtn = findViewById(R.id.text_view_test_flash);
        testLayout = findViewById(R.id.test_flash_layout);
        ///////  end ..........................

        Intent intent = getIntent();
        whichLayout = intent.getIntExtra("settingsIntent", 0);

        switch (whichLayout) {

            case 1:
                break;
            case 2:
                break;
            case 3:
                general.setVisibility(View.VISIBLE);
                generalImpCheck();
                simDefault();
                break;
            case 4:
                speedDial.setVisibility(View.VISIBLE);
                speedDialFunction();
                break;
            case 5:
                callBlock.setVisibility(View.VISIBLE);
                blockChecker();
                blockerOn();
                break;
            case 7:
                quickResponseLayout.setVisibility(View.VISIBLE);
                quickGetResponse();
                break;
            case 8:
                flashLayout.setVisibility(View.VISIBLE);
                flashImportantCheck();
                break;
        }
    }


/////////// Quick  start .................................................................

    private void quickGetResponse() {


        SharedPreferences preferences = getSharedPreferences("QuickRes", MODE_PRIVATE);

        if (!preferences.getBoolean("first", false)) {
            SharedPreferences.Editor editor = getSharedPreferences("QuickRes", MODE_PRIVATE).edit();

            editor.putString(String.valueOf(1), msgReject1.getText().toString()).apply();
            editor.putString(String.valueOf(2), msgReject2.getText().toString()).apply();
            editor.putString(String.valueOf(3), msgReject3.getText().toString()).apply();
            editor.putString(String.valueOf(4), msgReject4.getText().toString()).apply();
            editor.putString(String.valueOf(5), msgReject5.getText().toString()).apply();

            editor.putBoolean("first", true).apply();

        } else {
            msgReject1.setText(preferences.getString(String.valueOf(1), ""));
            msgReject2.setText(preferences.getString(String.valueOf(2), ""));
            msgReject3.setText(preferences.getString(String.valueOf(3), ""));
            msgReject4.setText(preferences.getString(String.valueOf(4), ""));
            msgReject5.setText(preferences.getString(String.valueOf(5), ""));
        }


        edit1.setOnClickListener(v -> {
            dialogBuild(1);
        });
        edit2.setOnClickListener(v -> {
            dialogBuild(2);
        });
        edit3.setOnClickListener(v -> {
            dialogBuild(3);
        });
        edit4.setOnClickListener(v -> {
            dialogBuild(4);
        });
        edit5.setOnClickListener(v -> {
            dialogBuild(5);
        });


    }

    private void dialogBuild(int i) {
        SharedPreferences.Editor editor = getSharedPreferences("QuickRes", MODE_PRIVATE).edit();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_quick);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView editBtn = dialog.findViewById(R.id.quick_btn);
        TextInputEditText edit = dialog.findViewById(R.id.quick_box);
        switch (i) {
            case 1:
                edit.setText(msgReject1.getText());
                break;
            case 2:
                edit.setText(msgReject2.getText());
                break;
            case 3:
                edit.setText(msgReject3.getText());
                break;
            case 4:
                edit.setText(msgReject4.getText());
                break;
            case 5:
                edit.setText(msgReject5.getText());
                break;
        }
        ImageView cancelBtn = dialog.findViewById(R.id.cancel_quick_btn);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        editBtn.setOnClickListener(v -> {
            if (!edit.getText().toString().equals("")) {
                editor.putString(String.valueOf(i), edit.getText().toString()).apply();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
    }

/////////// Quick  end     ...............................................................

    /////////// general  start .................................................................
    private void generalImpCheck() {

//        Intent intent = new Intent(this,Intent.);

        StcVariable.callSimChecker(this);
        int simNo = StcVariable.simCardList.size();
        if (simNo == 0) {
            sim1.setClickable(false);
            sim2.setClickable(false);
            noSimSet.setClickable(false);
        } else if (simNo == 1) {
            sim1.setClickable(true);
            sim2.setClickable(false);
            noSimSet.setClickable(false);
        }

        boolean valueDefaultDialer = CallHelper.isDefaultDialer(this);

        defaultDialer.setChecked(valueDefaultDialer);
        if (!valueDefaultDialer) {
            defaultDialer.setClickable(true);
            defaultDialer.setOnClickListener(v -> {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
                    Intent intent1;
                    intent1 = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
//                intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
                    startActivityForResult(intent1, REQUEST_CODE_SET_DEFAULT_DIALER);
                } else {
                    Intent intent1 = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                    startActivityForResult(intent1.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, "com.JvWorld.jcaller")
                            , REQUEST_CODE_SET_DEFAULT_DIALER);
                }
                defaultDialer.setClickable(false);
            });
        } else {
            defaultDialer.setClickable(false);
        }


    }


    private void simDefault() {
        int simNumber = SimSettings.defaultSim(this);
        if (simNumber == 0) {
            noSimSet.setBackgroundResource(R.drawable.track);
            noSimSet.setBackgroundTintList(getColorStateList(R.color.blue));
        } else if (simNumber == 1) {
            sim1.setBackgroundResource(R.drawable.track);
            sim1.setBackgroundTintList(getColorStateList(R.color.blue));
        } else if (simNumber == 2) {
            sim2.setBackgroundResource(R.drawable.track);
            sim2.setBackgroundTintList(getColorStateList(R.color.blue));
        }

        SharedPreferences.Editor editor = getSharedPreferences("simDefault", MODE_PRIVATE).edit();


        sim1.setOnClickListener(v -> {
            sim1.setBackgroundResource(R.drawable.track);
            sim1.setBackgroundTintList(getColorStateList(R.color.blue));
            noSimSet.setBackgroundResource(R.drawable.circular_back_transpart);
            noSimSet.setBackgroundTintList(getColorStateList(R.color.transparent));
            sim2.setBackgroundResource(R.drawable.circular_back_transpart);
            sim2.setBackgroundTintList(getColorStateList(R.color.transparent));
            editor.putInt("de", 1).apply();
        });

        sim2.setOnClickListener(v -> {
            sim2.setBackgroundResource(R.drawable.track);
            sim2.setBackgroundTintList(getColorStateList(R.color.blue));
            noSimSet.setBackgroundResource(R.drawable.circular_back_transpart);
            noSimSet.setBackgroundTintList(getColorStateList(R.color.transparent));
            sim1.setBackgroundResource(R.drawable.circular_back_transpart);
            sim1.setBackgroundTintList(getColorStateList(R.color.transparent));
            editor.putInt("de", 2).apply();
        });

        noSimSet.setOnClickListener(v -> {
            noSimSet.setBackgroundResource(R.drawable.track);
            noSimSet.setBackgroundTintList(getColorStateList(R.color.blue));
            sim1.setBackgroundResource(R.drawable.circular_back_transpart);
            sim1.setBackgroundTintList(getColorStateList(R.color.transparent));
            sim2.setBackgroundResource(R.drawable.circular_back_transpart);
            sim2.setBackgroundTintList(getColorStateList(R.color.transparent));
            editor.putInt("de", 0).apply();
        });


    }


    /////////// general  end   .................................................................


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (whichLayout == 3 && resultCode == 0) {
            defaultDialer.setClickable(true);
            defaultDialer.setChecked(false);
        }
    }

    /////////// flash  start .................................................................
    private void flashImportantCheck() {
        boolean isFlashToggleOn = CallHelper.flashToggle(this);
        flashOnSwitch.setChecked(isFlashToggleOn);
        if (isFlashToggleOn) {
            lightOnLayout.setVisibility(View.VISIBLE);
//            lightOffLayout.setVisibility(View.VISIBLE);
            testLayout.setVisibility(View.VISIBLE);
            lightOnSeekBar.setProgress(CallHelper.flashSeekOnToggle(this));
//            lightOffSeekBar.setProgress(CallHelper.flashSeekOffToggle(this));
            settingsEditSave.setText("Save");
            settingsEditSave.setVisibility(View.VISIBLE);
        } else {
            lightOnLayout.setVisibility(View.GONE);
//            lightOffLayout.setVisibility(View.GONE);
            testLayout.setVisibility(View.GONE);
        }
        flashOnSwitch.setOnClickListener(v -> {
            if (flashOnSwitch.isChecked()) {
                lightOnLayout.setVisibility(View.VISIBLE);
//                lightOffLayout.setVisibility(View.VISIBLE);
                testLayout.setVisibility(View.VISIBLE);
                lightOnSeekBar.setProgress(CallHelper.flashSeekOnToggle(this));
//                lightOffSeekBar.setProgress(CallHelper.flashSeekOffToggle(this));
                settingsEditSave.setText("Save");
                settingsEditSave.setVisibility(View.VISIBLE);
                CallHelper.flashToggleSet(this, true);
            } else {
                lightOnLayout.setVisibility(View.GONE);
//                lightOffLayout.setVisibility(View.GONE);
                testLayout.setVisibility(View.GONE);
                settingsEditSave.setVisibility(View.GONE);
                CallHelper.flashToggleSet(this, false);
            }
        });

        settingsEditSave.setOnClickListener(v -> {
            CallHelper.flashSeekToggleSet(this, lightOnSeekBar.getProgress());
            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        });

        textFlashBtn.setOnClickListener(v -> {
            blinkFlash();

        });

    }


    private void blinkFlash() {

        long blinkDelay = lightOnSeekBar.getProgress(); //Delay in ms
        blinkDelay = blinkDelay * 100;
        if (blinkDelay < 100) {
            blinkDelay = 500;
        }
        long duration = blinkDelay * 8;
        CountDownTimer timer = new CountDownTimer(duration, blinkDelay) {
            @Override
            public void onTick(long millisUntilFinished) {
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

    /////////// flash  end   .................................................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsettings_handler);

        initUi();

    }

    /////////// Block  start .................................................................
    @SuppressLint("Range")
    private void blockChecker() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("activity1", MODE_PRIVATE);
        if (preferences.getBoolean("contactBlocker", false)) {
            callBlockerOnOff.setChecked(true);
            rrb2.setBackgroundColor(getResources().getColor(R.color.transparent));
            recyclerBlockNumbers.setBackgroundColor(getResources().getColor(R.color.transparent));
            unknownCallBlockerOnOff.setClickable(true);
            recyclerBlockNumbers.setClickable(true);

        } else {
            callBlockerOnOff.setChecked(false);
            rrb2.setBackgroundColor(getResources().getColor(R.color.darkGrey));
            recyclerBlockNumbers.setBackgroundColor(getResources().getColor(R.color.darkGrey));
            unknownCallBlockerOnOff.setClickable(false);
            recyclerBlockNumbers.setClickable(false);
        }

        unknownCallBlockerOnOff.setChecked(preferences.getBoolean("unknownBlocker", false));
        databaseManagerFav = new DatabaseManagerFav(this);

        try {
            databaseManagerFav.open();
//            databaseManagerFav.insertSpeedDial("",1);

        } catch (SQLiteException | SQLDataException throwables) {
            throwables.printStackTrace();
        }
        recyclerBlockNumbers.setHasFixedSize(true);
        recyclerBlockNumbers.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();

        Cursor cursor = databaseManagerFav.fetchBlockNumbers();
//        int ID = 1;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String a = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NUMBER_BLOCK));
                arrayList.add(a);
            } while (cursor.moveToNext());

        }
        BlockAdapter blockAdapter = new BlockAdapter(this, arrayList);
        recyclerBlockNumbers.setAdapter(blockAdapter);
    }

    private void blockerOn() {
        callBlockerOnOff.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
            editor.putBoolean("contactBlocker", callBlockerOnOff.isChecked());
            editor.apply();
            blockChecker();

        });
        unknownCallBlockerOnOff.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
            editor.putBoolean("unknownBlocker", unknownCallBlockerOnOff.isChecked());
            editor.apply();
            blockChecker();
        });
    }


/////////// Block  end     ...............................................................

/////////// speed dial start .................................................................


    private void speedDialFunction() {
        databaseManagerFav = new DatabaseManagerFav(this);

        try {
            databaseManagerFav.open();
//            databaseManagerFav.insertSpeedDial("",1);

        } catch (SQLiteException | SQLDataException throwables) {
            throwables.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new SettingsAdapter(this, StcVariable.contactsModelArrayList, this);

        recyclerView.setAdapter(contactsAdapter);

        num1.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(1);
            if (aa.equals("")) {
                getSet("Set speed dial number", 1);
            } else {
                setGet("Speed dial for " + aa, 1);
            }
        });
        num2.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(2);
            if (aa.equals("")) {
                getSet("Set speed dial number", 2);
            } else {
                setGet("Speed dial for " + aa, 2);
            }
        });
        num3.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(3);
            if (aa.equals("")) {
                getSet("Set speed dial number", 3);
            } else {
                setGet("Speed dial for " + aa, 3);
            }
        });
        num4.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(4);
            if (aa.equals("")) {
                getSet("Set speed dial number", 4);
            } else {
                setGet("Speed dial for " + aa, 4);
            }
        });
        num5.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(5);
            if (aa.equals("")) {
                getSet("Set speed dial number", 5);
            } else {
                setGet("Speed dial for " + aa, 5);
            }
        });
        num6.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(6);
            if (aa.equals("")) {
                getSet("Set speed dial number", 6);
            } else {
                setGet("Speed dial for " + aa, 6);
            }
        });
        num7.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(7);
            if (aa.equals("")) {
                getSet("Set speed dial number", 7);
            } else {
                setGet("Speed dial for " + aa, 7);
            }
        });
        num8.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(8);
            if (aa.equals("")) {
                getSet("Set speed dial number", 8);
            } else {
                setGet("Speed dial for " + aa, 8);
            }
        });
        num9.setOnClickListener(view -> {
            String aa = databaseManagerFav.fetchSpeedDial(9);
            if (aa.equals("")) {
                getSet("Set speed dial number", 9);
            } else {
                setGet("Speed dial for " + aa, 9);
            }
        });
    }

    private void setGet(String s, long b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.dialer)
                .setTitle("JCaller").setMessage(s)
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    databaseManagerFav.deleteSpeedDial(b);
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }


    private void getSet(String s, int a) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.dialer)
                .setTitle("JCaller").setMessage(s)
                .setPositiveButton("Set", (dialogInterface, i) -> {
                    _ID = a;
                    layoutS.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void numberId(String a) {
        a = stringSlicer(a);
        databaseManagerFav.insertSpeedDial(a, _ID);
        layoutS.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private String stringSlicer(String strValue) {
        String number = strValue;
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
        return number;
    }

/////////// speed dial end     ................................................................

/////////// permimum  start .................................................................
/////////// permimum  end     ...............................................................


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}