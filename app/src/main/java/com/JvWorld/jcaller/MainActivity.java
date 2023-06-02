package com.JvWorld.jcaller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.contacts.Demodel;
import com.JvWorld.jcaller.fragment.ContactFragment;
import com.JvWorld.jcaller.fragment.DialFragment;
import com.JvWorld.jcaller.fragment.FavFragment;
import com.JvWorld.jcaller.fragment.RecentFragment;
import com.JvWorld.jcaller.fragment.SettingFragment;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private static final int LOCATION_RC = 101;

    private static final String CHANNEL_IDS = "11";
    private static final String CHANNEL_IDs = "112";

    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 11;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE,
            Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//            Manifest.permission.INSTALL_PACKAGES
    };
    private int CONTACT_SIZE = 0;

    private int flag = 0;
    private LottieAnimationView animationView;

    private void initUI() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        animationView = findViewById(R.id.animationView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            ///////////////////////////////////////////////////////
//            speed dial
            getContactSize();
            ///////////////////////////////////////////////////////
//            contact fragment
//            getContactsSize();
//            StcVariable.contactModelArrayList = getContacts();

        }
        SharedPreferencesFunction();
    }
////////////////////////////////////////////////////////////////////////////////////////////


    @SuppressLint("Range")
    private ArrayList<ContactsModel> getContacts() {

        ArrayList<ContactsModel> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");


        cursor.moveToFirst();

        int CONTACT_POSITION;
        for (CONTACT_POSITION = 0; CONTACT_POSITION <= CONTACT_SIZE - 1; CONTACT_POSITION++) {
            cursor.moveToPosition(CONTACT_POSITION);
            cursor1.moveToPosition(CONTACT_POSITION - 1);
            String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

//            String photo = "";
            if (CONTACT_POSITION >= 1) {
                String aa = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String ab = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (aa.equals(ab)) {
//                    Toast.makeText(getContext(), "same", Toast.LENGTH_SHORT).show();
                } else {
//                    number = number.replaceAll("\\s", "");
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    number = stringSlicer(number);
                    list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME))
                            , number, photo));
                }
            } else {
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number = stringSlicer(number);
                list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.DISPLAY_NAME))
                        , number, photo));
            }
        }
        return list;
    }

    @NonNull
    @SuppressLint("Range")
    private ArrayList<Demodel> getContactsSize() {
        ArrayList<Demodel> demodelArrayList = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {
            CONTACT_SIZE++;
            demodelArrayList.add(new Demodel(cursor.getString(cursor.getColumnIndex(ContactsContract
                    .CommonDataKinds.Phone.DISPLAY_NAME))));
        }
        return demodelArrayList;
    }

    private String stringSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
        return number;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
//    Speed dial
////////////////////////////////////////////////////////////////////////////////////////////

    private String noMoreSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
        return number;
    }

    @SuppressLint("Range")
    private void getContactSize() {

        ArrayList<ContactsModel> demodelArrayList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME.toLowerCase(Locale.ROOT)
                        .toUpperCase(Locale.ROOT) + " ASC");

        if (cursor != null) {
            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();
                do {
                    String ph = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    list.add(noMoreSlicer(ph));
                } while (cursor.moveToNext());

            }
        }
        Set<String> stringSet = new LinkedHashSet<>(list);
        list = new ArrayList<>(stringSet);
        int size = list.size();
        for (int i = 0; i <= size - 1; i++) {
            demodelArrayList.add(new ContactsModel(mainFor(list.get(i))
                    , list.get(i), ""));
            if (i == size - 1) {
                demodelArrayList.add(new ContactsModel(mainFor(list.get(i))
                        , list.get(i), ""));
            }

        }

        StcVariable.contactsModelArrayList = demodelArrayList;
    }

    private String mainFor(String str) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor1 = this.getContentResolver().query(uri, projection, null, null, null);
        String a = "";
        if (cursor1 != null) {
            if (cursor1.getCount() != 0) {
                cursor1.moveToFirst();
                a = cursor1.getString(0);
            } else {
                a = "Unknown";
            }
        }

        return a;
    }

////////////////////////////////////////////////////////////////////////////////////////////

    private void SharedPreferencesFunction() {

        String getActivityValue = "";
        SharedPreferences preferences = getSharedPreferences("activity1", MODE_PRIVATE);
        getActivityValue = preferences.getString("Value", "");

        if (getActivityValue.equals("")) {
            SharedPreferences.Editor editor = getSharedPreferences("activity1", MODE_PRIVATE).edit();
            editor.putString("Value", "dial");
            editor.apply();
            bottomNavigationView.setSelectedItemId(R.id.nav_dial);
            openFragment(new DialFragment(), "", false);
        } else if (getActivityValue.equals("fav")) {
            bottomNavigationView.setSelectedItemId(R.id.nav_fav);
            openFragment(new FavFragment(), "", false);
        } else if (getActivityValue.equals("con")) {
            bottomNavigationView.setSelectedItemId(R.id.nav_contacts);
            openFragment(new ContactFragment(), "", false);
        } else if (getActivityValue.equals("rec")) {
            bottomNavigationView.setSelectedItemId(R.id.nav_recents);
            openFragment(new RecentFragment(), "", false);
        } else if (getActivityValue.equals("set")) {
            bottomNavigationView.setSelectedItemId(R.id.nav_settings);
            openFragment(new SettingFragment(), "", false);
        } else if (getActivityValue.equals("dial")) {
            bottomNavigationView.setSelectedItemId(R.id.nav_dial);
            openFragment(new DialFragment(), "", false);
        }

        handlerBottom();
    }

    private void handlerBottom() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_fav:
                    openFragment(new FavFragment(), "", true);
                    break;
                case R.id.nav_recents:
                    openFragment(new RecentFragment(), "", true);
                    break;
                case R.id.nav_contacts:
                    openFragment(new ContactFragment(), "", true);
                    break;
                case R.id.nav_dial:
                    openFragment(new DialFragment(), "", true);
                    break;
                case R.id.nav_settings:
                    openFragment(new SettingFragment(), "", true);
                    break;
            }

            return true;
        });
        animationView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) {
            initUI();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_RC);
        }
    }

    private void openFragment(Fragment fragment, String tag, boolean addBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.body_container, fragment, tag);

        if (addBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    private boolean checkPermission() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_RC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUI();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void setDefaultDialer() {
        String defaultDialerPackage = "com.JvWorld.jcaller";
        TelecomManager manager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        boolean isAlreadyDefaultDialer;
        String dialerNow = manager.getDefaultDialerPackage();
        isAlreadyDefaultDialer = defaultDialerPackage.equals(dialerNow);
        if (!isAlreadyDefaultDialer) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
                Intent intent;
                intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
//                intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
                startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
            } else {
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                startActivityForResult(intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, defaultDialerPackage)
                        , REQUEST_CODE_SET_DEFAULT_DIALER);
            }
        }
        StcVariable.SPLASH_SCREEN = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        incomingNotification();
        if (StcVariable.SPLASH_SCREEN) {
            setDefaultDialer();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SET_DEFAULT_DIALER) {
            checkSetDefaultDialerResult(resultCode);
        }
    }

    private void checkSetDefaultDialerResult(int resultCode) {
        if (resultCode == RESULT_OK) {

            Toast.makeText(this, "User accepted request to become default dialer", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "User declined request to become default dialer", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unexpected result code" + String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
        }
    }

    private void incomingNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            CharSequence name = "Incoming Calls";
            String description = "This is for Incoming Calls";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_IDS, name, importance);
            channel.setSound(ringtoneUri, new AudioAttributes.Builder().setUsage(AudioAttributes
                    .USAGE_NOTIFICATION_RINGTONE).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.enableVibration(false);
            channel.setDescription(description);
            NotificationManager mgr = getSystemService(NotificationManager.class);
            mgr.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            CharSequence name = "Missed";
//            String description = "This is for Incoming Calls";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_IDs, name, importance);
            channel.setSound(ringtoneUri, new AudioAttributes.Builder().setUsage(AudioAttributes
                    .USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 2000});
            NotificationManager mgrb = getSystemService(NotificationManager.class);
            mgrb.createNotificationChannel(channel);
        }
    }

}