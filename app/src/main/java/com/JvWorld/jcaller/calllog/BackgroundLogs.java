package com.JvWorld.jcaller.calllog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.JvWorld.jcaller.fragment.RecentFragment;
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BackgroundLogs extends AsyncTask<Void, Void, Void> {
    WeakReference<RecentFragment> fragmentWeakReference;

    public BackgroundLogs(RecentFragment fragment) {
        fragmentWeakReference = new WeakReference<RecentFragment>(fragment);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getCallLogs();
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        RecentFragment activity = fragmentWeakReference.get();
        if (activity == null) {
            return;
        }
        activity.animationView.setVisibility(View.INVISIBLE);
        activity.rv_call_logs.setAdapter(activity.callLogAdapter);
    }

    @SuppressLint("Range")
    private void getCallLogs() {
        RecentFragment activity = fragmentWeakReference.get();
        CallHelper.callLogModelArrayList = new ArrayList<>();
        activity.callLogAdapter = new CallLogAdapter(activity.getContext(), CallHelper.callLogModelArrayList);
        ArrayList<String> strings = new ArrayList<>();


        CallHelper.callLogModelArrayList.clear();

        ArrayList<String> numberChecker;

        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = activity.getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null
                , sortOrder);


        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() >= 1) {
                do {
                    activity.str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    strings.add(activity.str_number);

                } while (cursor.moveToNext());

                int a;
                Set<String> str = new LinkedHashSet<>(strings);

                numberChecker = new ArrayList<>(str);

                int size = numberChecker.size();

                for (a = 1; a <= size; a++) {
                    String value = numberChecker.get(a - 1);
                    mainStings(value);
                    CallLogModel callLogItem = new CallLogModel(value, activity.str_contact_name, activity.str_call_type,
                            activity.str_date, activity.CALL_TYPE_VALUE_MISSED);
                    CallHelper.callLogModelArrayList.add(callLogItem);
                }
                if (CallHelper.callLogModelArrayList.size() == 0) {
                    activity.noCalls.setVisibility(View.VISIBLE);
                }
//                activity.animationView.setVisibility(View.INVISIBLE);
//                activity.rv_call_logs.setAdapter(activity.callLogAdapter);
            } else {
                activity.noCalls.setVisibility(View.VISIBLE);
                activity.animationView.setVisibility(View.INVISIBLE);
            }
        } else {
            activity.animationView.setVisibility(View.INVISIBLE);
        }
    }

    private void mainStings(String number) {
        RecentFragment activity = fragmentWeakReference.get();
//        StcVariable.callSimChecker(getContext());
//        int sim = StcVariable.simSubscription.size();
        boolean isCheckSubscription = false;

//        if (sim == 1) {
//            isCheckSubscription = true;
//        } else if (sim == 2) {
//            isCheckSubscription = true;
//        }

        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";

        number = Uri.encode(number);


        Uri uriName = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
        String[] projectionName = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursorName = activity.getContext().getContentResolver().query(uriName, projectionName, null, null, null);
        if (cursorName != null) {
            if (cursorName.getCount() != 0) {
                cursorName.moveToFirst();
                activity.str_contact_name = cursorName.getString(0);
            } else {
                activity.str_contact_name = "Unknown";
            }
        }

        Uri uri = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, number);
        String[] projection = new String[]{CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.PHONE_ACCOUNT_ID};

        Cursor cursor = activity.getContext().getContentResolver().query(uri, projection, null, null
                , sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
//            activity.str_call_type = "cursor.getString(0)";
            activity.str_call_type = cursor.getString(0);
            switch (Integer.parseInt(activity.str_call_type)) {
                case CallLog.Calls.INCOMING_TYPE:
                    activity.str_call_type = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    activity.str_call_type = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    activity.str_call_type = "Missed";
                    break;
                case CallLog.Calls.VOICEMAIL_TYPE:
                    activity.str_call_type = "Voicemail";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    activity.str_call_type = "Rejected";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    activity.str_call_type = "Blocked";
                    break;
                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    activity.str_call_type = "Externally Answered";
                    break;
                default:
                    activity.str_call_type = "NA";
            }

            String date = cursor.getString(1);

            activity.phoneAccount = cursor.getString(2);

            if (isCheckSubscription) {
                TelecomManager tm2 = (TelecomManager) activity.getContext().getSystemService(Context.TELECOM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(activity.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    List<PhoneAccountHandle> accountHandles = new ArrayList<>(tm2.getCallCapablePhoneAccounts());
                    int simSize = accountHandles.size();

                    if (simSize == 0) {
                        activity.phoneAccount = "";
                    } else if (simSize == 1) {
                        PhoneAccountHandle phoneAccountHandle = accountHandles.get(0);
                        if (activity.phoneAccount.equals(phoneAccountHandle.getId().substring(0, 20))) {
                            activity.phoneAccount = StcVariable.sim1Name;
                        } else {
                            activity.phoneAccount = "";
                        }
                    } else if (simSize == 2) {
                        PhoneAccountHandle phoneAccountHandle = accountHandles.get(0);
                        PhoneAccountHandle phoneAccountHandle1 = accountHandles.get(1);
                        if (activity.phoneAccount.equals(phoneAccountHandle.getId().substring(0, 20))) {
                            activity.phoneAccount = StcVariable.sim1Name;
                        } else if (activity.phoneAccount.equals(phoneAccountHandle1.getId().substring(0, 20))) {
                            activity.phoneAccount = StcVariable.sim2Name;
                        } else {
                            activity.phoneAccount = "";
                        }
                    }

                }
            } else {
                activity.phoneAccount = "";
            }
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            Date dateToday = new Date();
            String today = dateFormat.format(dateToday);
            String callDate = dateFormat.format(new Date(Long.parseLong(date)));

            activity.str_date = dateSelector(callDate, today);
        }


    }

    private String dateSelector(String date, String today) {
        String callDt = "";
        if (date.substring(6, 8).equals(today.substring(6, 8))) {
            int month = Integer.parseInt(today.substring(3, 5));
            int callMonth = Integer.parseInt(date.substring(3, 5));
            if (month == callMonth) {
                int day = Integer.parseInt(today.substring(0, 2));
                int callDay = Integer.parseInt(date.substring(0, 2));
                if (day == callDay) {
                    int hr = Integer.parseInt(today.substring(9, 11));
                    int callHr = Integer.parseInt(date.substring(9, 11));
                    if (hr == callHr) {
                        int min = Integer.parseInt(today.substring(12, 14));
                        int callMin = Integer.parseInt(date.substring(12, 14));
                        callDt = String.valueOf(min - callMin) + " minute ago";
                    } else {
                        callDt = String.valueOf(hr - callHr) + " hour ago";
                    }
                } else {
                    callDt = String.valueOf(day - callDay) + " day ago";
                }
            } else {
                callDt = String.valueOf(month - callMonth) + " month ago";
            }
        } else {
            callDt = "a years ago";
        }
        return callDt;
    }


}
