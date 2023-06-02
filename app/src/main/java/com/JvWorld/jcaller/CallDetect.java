package com.JvWorld.jcaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.JvWorld.jcaller.contacts.EditContactActivity;

public class CallDetect extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            showToast(context, "Call started...");
            Intent intent1 = new Intent(context, EditContactActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent intent1 = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent intent1 = new Intent(context,EditContactActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.addFlags(Integer.parseInt(Intent.EXTRA_DONT_KILL_APP));
            context.startActivity(intent1);
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            showToast(context, "Call ended...");
            Intent intent1 = new Intent(context, EditContactActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent intent1 = new Intent(context,EditContactActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.addFlags(Integer.parseInt(Intent.EXTRA_DONT_KILL_APP));
            context.startActivity(intent1);
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            showToast(context, "Incoming call...");
            Intent intent1 = new Intent(context, EditContactActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent intent1 = new Intent(context,EditContactActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.addFlags(Integer.parseInt(Intent.EXTRA_DONT_KILL_APP));
            context.startActivity(intent1);
        }
    }

    void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
