package com.JvWorld.jcaller.callHandler;

import static android.content.Context.TELECOM_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.JvWorld.jcaller.someVariable.StcVariable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Placecall {

    Context context;

    List<PhoneAccountHandle> phoneAccountHandleList;

    public Placecall(Context context) {
        this.context = context;
    }

    public void placeCall(String scheme, String dial, int phone) {
        scheme = "tel";
        TelecomManager manager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
        twoSimHandlerCall(dial, scheme, phone, manager);


    }


    private void call(String dial, int simSlot) {
        TelecomManager manager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);

        String dialerNow = manager.getDefaultDialerPackage();
        String defaultDialerPackage = "com.JvWorld.jcaller";
        boolean isAlreadyDefaultDialer = defaultDialerPackage.equals(dialerNow);
        if (isAlreadyDefaultDialer) {
            Uri uri = Uri.fromParts("tel", dial, null);
            Bundle extras = new Bundle();
//        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,phoneAccountHandle);
//            extras.putString();
//            extras.putString(TelecomManager.ACTION_PHONE_ACCOUNT_REGISTERED,phoneAccount);
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);
            extras.putBoolean(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, true);
            Toast.makeText(context, String.valueOf(extras), Toast.LENGTH_SHORT).show();
            extras.putInt("simSlot", simSlot - 1);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.placeCall(uri, extras);
            context.startActivity(new Intent(context, Outgoingcallui.class));
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            dial = dial.replace("#", Uri.encode("#"));
            callIntent.setData(Uri.parse("tel:" + dial));
//            callIntent.putExtra("simSlot",simSlot-1);
            context.startActivity(callIntent);
        }
//        PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(new ComponentName(context.getPackageName()
//                ,OutgoingService.class.getName()),"0");

    }


    public void twoSimHandlerCall(String number, String scheme, int sim, TelecomManager tm) {
        Uri uri = Uri.fromParts("tel", number, null);
        Bundle extraBundle = new Bundle();
        extraBundle.putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_AUDIO_ONLY);
        extraBundle.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);
        if (newMethod(tm)) {
            PhoneAccountHandle phoneAccountHandle = phoneAccountHandleList.get(sim - 1);
            extraBundle.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
        } else {
//            PhoneAccountHandle ph = 89012938746321;
            PhoneAccountHandle phoneAccountHandle = phoneAccountHandleList.get(sim - 1);
            extraBundle.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        tm.placeCall(uri, extraBundle);
        String dialerNow = tm.getDefaultDialerPackage();
        String defaultDialerPackage = "com.JvWorld.jcaller";
        boolean isAlreadyDefaultDialer = defaultDialerPackage.equals(dialerNow);
        if (isAlreadyDefaultDialer) {
            context.startActivity(new Intent(context, Outgoingcallui.class));
        }


    }

    private boolean newMethod(TelecomManager tm) {

        boolean isDefaultSim = false;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        PhoneAccountHandle outGoingPhoneAccount = tm.getDefaultOutgoingPhoneAccount("tel");

        if (outGoingPhoneAccount == null) {

            phoneAccountHandleList = tm.getCallCapablePhoneAccounts();
            isDefaultSim = true;
        } else {
            phoneAccountHandleList = tm.getCallCapablePhoneAccounts();
            Toast.makeText(context, outGoingPhoneAccount.getId().toString(), Toast.LENGTH_SHORT).show();
        }
        return isDefaultSim;

    }

    private void registerPhoneAccount() {

        TelecomManager tm = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
//        int a = 89012345673281;
        PhoneAccountHandle phoneAccountHandle1 = new PhoneAccountHandle(new ComponentName(context.getApplicationContext().getPackageName(), String.valueOf(Placecall.class)),
                "89012345673281");
        PhoneAccount.Builder builder = PhoneAccount.builder(phoneAccountHandle1, phoneAccountHandle1.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED);
        }
        PhoneAccount phoneAccount = builder.build();

        tm.registerPhoneAccount(phoneAccount);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Toast.makeText(context, tm.getDefaultOutgoingPhoneAccount("tel").toString(), Toast.LENGTH_SHORT).show();
    }


    public void placeCallByDefault(String scheme, String dial) {

        TelecomManager manager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);

        String dialerNow = manager.getDefaultDialerPackage();
        String defaultDialerPackage = "com.JvWorld.jcaller";
        boolean isAlreadyDefaultDialer = defaultDialerPackage.equals(dialerNow);
        if (isAlreadyDefaultDialer) {
            Uri uri = Uri.fromParts("tel", dial, null);
            Bundle extras = new Bundle();
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);
            extras.putBoolean(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, false);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.placeCall(uri, extras);
            context.startActivity(new Intent(context, Outgoingcallui.class));
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            dial = dial.replace("#", Uri.encode("#"));
            callIntent.setData(Uri.parse("tel:" + dial));
            context.startActivity(callIntent);
        }
    }


}
