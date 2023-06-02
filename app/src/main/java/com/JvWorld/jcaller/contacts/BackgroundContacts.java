package com.JvWorld.jcaller.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.JvWorld.jcaller.calllog.CallHelper;
import com.JvWorld.jcaller.calllog.CallLogAdapter;
import com.JvWorld.jcaller.calllog.CallLogModel;
import com.JvWorld.jcaller.fragment.ContactFragment;
import com.JvWorld.jcaller.fragment.RecentFragment;
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BackgroundContacts extends AsyncTask<Void, Void, Void> {
    WeakReference<ContactFragment> fragmentWeakReference;
    private int CONTACT_SIZE = 0;

    public BackgroundContacts(ContactFragment fragment) {
        fragmentWeakReference = new WeakReference<ContactFragment>(fragment);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        ContactFragment activity = fragmentWeakReference.get();
//        if (activity == null){
//            return;
//        }
//        activity.conLoader.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getContactsSize();
        StcVariable.contactModelArrayList = getContacts();
        ContactFragment activity = fragmentWeakReference.get();
        if (activity == null) {
            return null;
        }
        activity.adapter = new ContactsAdapter(activity.getContext(), StcVariable.contactModelArrayList);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        ContactFragment activity = fragmentWeakReference.get();
        if (activity == null) {
            return;
        }
        CountDownTimer timer = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                activity.recyclerView.setAdapter(activity.adapter);
                if (StcVariable.contactModelArrayList.size() == 0) {
                    activity.textNoCon.setVisibility(View.VISIBLE);
                } else {
                    activity.search.setHint("Search from " + StcVariable.contactModelArrayList.size());
                }
                activity.conLoader.setVisibility(View.INVISIBLE);
            }
        }.start();


    }

    @SuppressLint("Range")
    private ArrayList<ContactsModel> getContacts() {

        ContactFragment activity = fragmentWeakReference.get();
        activity.conLoader.setVisibility(View.VISIBLE);
        ArrayList<ContactsModel> list = new ArrayList<>();
        Cursor cursor = activity.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor cursor1 = activity.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
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
        ContactFragment activity = fragmentWeakReference.get();
        ArrayList<Demodel> demodelArrayList = new ArrayList<>();
        Cursor cursor = activity.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
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

}
