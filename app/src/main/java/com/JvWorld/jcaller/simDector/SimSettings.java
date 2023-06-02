package com.JvWorld.jcaller.simDector;

import android.content.Context;
import android.content.SharedPreferences;

public class SimSettings {

    public static int defaultSim(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("simDefault", Context.MODE_PRIVATE);
        return preferences.getInt("de", 0);
    }

    public static String quickRes(Context context, int i) {
        SharedPreferences preferences = context.getSharedPreferences("QuickRes", Context.MODE_PRIVATE);
        return preferences.getString(String.valueOf(i), "");
    }

}
