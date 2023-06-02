package com.JvWorld.jcaller.calllog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;

import java.util.ArrayList;

public class CallHelper {
////////////////////////////////////////////////////////////////

    //    call fragment
    public static ArrayList<CallLogModel> callLogModelArrayList;

////////////////////////////////////////////////////////////////

    public static boolean isDefaultDialer(Context context) {

        boolean isDef = false;

        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        String isPackage = tm.getDefaultDialerPackage();
        String isMyPackage = "com.JvWorld.jcaller";
        if (isMyPackage.equals(isPackage)) {
            isDef = true;
        }

        return isDef;
    }

    public static boolean flashToggle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("activity1", MODE_PRIVATE);
        return preferences.getBoolean("flashOnOff", false);
    }

    public static void flashToggleSet(Context context, boolean bool) {
        SharedPreferences.Editor editor = context.getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putBoolean("flashOnOff", bool);
        editor.apply();
    }

    public static int flashSeekOnToggle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("flash", MODE_PRIVATE);
        return preferences.getInt("flashOnTime", 0);
    }

    //    public static int flashSeekOffToggle(Context context){
//        SharedPreferences preferences = context.getSharedPreferences("flash", MODE_PRIVATE);
//        return preferences.getInt("flashOffTime", 0);
//    }
    public static void flashSeekToggleSet(Context context, int on) {
        SharedPreferences.Editor editor = context.getSharedPreferences("flash", MODE_PRIVATE).edit();
        editor.putInt("flashOnTime", on);
//        editor.putInt("flashOffTime" , off);
        editor.apply();
    }


}
