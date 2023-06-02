package com.JvWorld.jcaller.someVariable;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.callHandler.Placecall;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.director.NumberAdapter;
import com.JvWorld.jcaller.simDector.SimAdapter;
import com.JvWorld.jcaller.simDector.SimModel;
import com.JvWorld.jcaller.simDector.SimSettings;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.ArrayList;
import java.util.List;

public class StcVariable {

    public static boolean ADD_CARD_TO_FAVOURITE = false;

    public static boolean SPLASH_SCREEN = false;

    public static boolean ADD_CARD_TO_BLOCK = false;

    public static boolean CONTACTS_VISIBLE_TEXT = false;

    public static long FAVOURITE_ID_CONTACTS = 0;

    public static long BLOCK_ID_CONTACTS = 0;

    public static boolean isDarkModeOn;
    /////////////////////////////////////////////////////////////////
//    for other contact list
    public static ArrayList<ContactsModel> contactsModelArrayList;

    //////////////////////////////////////////////////////////////
//    for contacts fragment
    public static ArrayList<ContactsModel> contactModelArrayList;

    public static ArrayList<SimModel> simCardList;

    public static ArrayList<SubscriptionInfo> simSubscription;

    public static Dialog dialog;

    public static String sim1Name, sim2Name;


    public static boolean isDarkMode(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        isDarkModeOn = sharedPreferences.getBoolean("mode", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        return isDarkModeOn;
    }

    public static String getContactNameByNumber(String number, @NonNull Context context) {
        String name = "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
            } else {
                name = "Unknown";
            }
            cursor.close();
        } else {
            name = "Unknown";
        }
        return name;
    }

    public static void callSimChecker(@NonNull Context context) {
        simCardList = new ArrayList<>();
        simSubscription = new ArrayList<>();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String abv = telephonyManager.getSimCountryIso() + telephonyManager.getSimOperatorName();

        if (!abv.equals("")) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            int i;
            for (i = 1; subscriptionInfoList.size() >= i; i++) {
                SubscriptionInfo subscriptionInfo = subscriptionInfoList.get(i - 1);
                simSubscription.add(subscriptionInfo);
                String simSlot = String.valueOf(subscriptionInfo.getSimSlotIndex());
                String number = subscriptionInfo.getNumber();
                String carrierName = String.valueOf(subscriptionInfo.getCarrierName());
                simCardList.add(new SimModel(simSlot, number, carrierName));
            }

            if (simCardList.size() == 1) {
                sim1Name = simCardList.get(0).getNameSim();
            } else if (simCardList.size() == 2) {
                sim1Name = simCardList.get(0).getNameSim();
                sim2Name = simCardList.get(1).getNameSim();
            }

        }


    }

    public static void alertBottomSheet(String scheme, String dial, Context context) {

        callSimChecker(context);


        if (simCardList.size() == 0) {

            buildDialog(context, scheme, dial);

        } else if (simCardList.size() == 1) {
            Placecall placecall = new Placecall(context);
            placecall.placeCallByDefault(scheme, dial);

        } else if (simCardList.size() >= 2) {
            if (SimSettings.defaultSim(context) == 0) {
                buildDialog(context, scheme, dial);
            } else {
                int i = SimSettings.defaultSim(context);
                Placecall placecall = new Placecall(context);
                placecall.placeCall(scheme, dial, i);
            }

        }
    }

    private static void buildDialog(Context context, String scheme, String dial) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.cutom_dialog_number_of_sim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_sim);
        ImageView imageView = dialog.findViewById(R.id.imageView2);
        TextView textv = dialog.findViewById(R.id.insert);
        if (simCardList.size() >= 1) {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textv.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            SimAdapter adapter = new SimAdapter(context, simCardList, dial, scheme, dialog);
            recyclerView.setAdapter(adapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textv.setVisibility(View.VISIBLE);
            CountDownTimer timer = new CountDownTimer(1000, 3000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }
            }.start();
        }
        dialog.show();
    }

}
