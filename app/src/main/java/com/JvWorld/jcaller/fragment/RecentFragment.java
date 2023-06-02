package com.JvWorld.jcaller.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.RecyclerViewSwipeDecorator;
import com.JvWorld.jcaller.callHandler.Placecall;
import com.JvWorld.jcaller.calllog.BackgroundLogs;
import com.JvWorld.jcaller.calllog.CallHelper;
import com.JvWorld.jcaller.calllog.CallLogAdapter;
import com.JvWorld.jcaller.calllog.CallLogModel;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RecentFragment extends Fragment {

    private View view;
    //    private ArrayList<CallLogModel> callLogModelArrayList;
    public RecyclerView rv_call_logs;
    public CallLogAdapter callLogAdapter;
    public int CALL_TYPE_VALUE_MISSED = 0;
    public TextView noCalls;
    public String str_number, str_contact_name, str_call_type, str_date, phoneAccount;
    public LottieAnimationView animationView;
    ItemTouchHelper.SimpleCallback simpleCallback = new
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    int pos = viewHolder.getAdapterPosition();

                    switch (direction) {
                        case ItemTouchHelper.LEFT:

                            Snackbar.make(view, "Delete All Call History", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", view -> {

                                    }).show();
                            DeleteNumFromCallLog(noMoreSlicer(CallHelper.callLogModelArrayList.get(pos).getPhNumber()));
                            CallHelper.callLogModelArrayList.remove(pos);
                            callLogAdapter.notifyItemRemoved(pos);
                            break;
                        case ItemTouchHelper.RIGHT:
                            StcVariable.alertBottomSheet("tel",
                                    CallHelper.callLogModelArrayList.get(pos).getPhNumber(), getContext());
                            callLogAdapter.notifyDataSetChanged();
                            break;
                    }
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                        float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                            .addSwipeLeftActionIcon(R.drawable.ic_delete)
                            .addSwipeRightActionIcon(R.drawable.ic_call)
                            .addSwipeRightCornerRadius(10, 70)
                            .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.green))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView call_all, call_missed;
    private TextInputEditText searchRec;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recent, container, false);
        Init();
        btnClick();
        return view;
    }

    private void btnClick() {

        searchRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
//            getCallLogs();
            BackgroundLogs backgroundLogs = new BackgroundLogs(RecentFragment.this);
            backgroundLogs.execute();
            swipeRefreshLayout.setRefreshing(false);
        });
        call_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CALL_TYPE_VALUE_MISSED=0;
//                call_all.setBackgroundColor(Color.WHITE);
//                call_missed.setBackgroundColor(0x319D9999);
//                FetchCallLogs(CALL_TYPE_VALUE_MISSED);
            }
        });
        call_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "In Progress", Toast.LENGTH_SHORT).show();
//                CALL_TYPE_VALUE_MISSED=1;
//                call_missed.setBackgroundColor(Color.WHITE);
//                call_all.setBackgroundColor(0x319D9999);
//                FetchCallLogs(CALL_TYPE_VALUE_MISSED);
            }
        });
    }

    private void filter(String searchQuery) {
        ArrayList<CallLogModel> filterList = new ArrayList<>();
        for (CallLogModel callLogModel : CallHelper.callLogModelArrayList) {
            boolean conName = callLogModel.getContactName().toLowerCase().contains(searchQuery.toLowerCase());
            boolean conNum = callLogModel.getPhNumber().toLowerCase().contains(searchQuery.toLowerCase());
            if (conName || conNum) {
                filterList.add(callLogModel);
            }
        }
        callLogAdapter.filterList(filterList);
    }


//    @SuppressLint("Range")
//    private void getCallLogs() {
//        CallHelper.callLogModelArrayList = new ArrayList<>();
//        callLogAdapter = new CallLogAdapter(getContext(), CallHelper.callLogModelArrayList);
//        ArrayList<String> strings = new ArrayList<>();
//
//
//        CallHelper.callLogModelArrayList.clear();
//
//        ArrayList<String> numberChecker;
//
//        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
//        Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null
//                , sortOrder);
//
//        cursor.moveToFirst();
//        if (cursor != null) {
//            if (cursor.getCount() >= 1) {
//                do {
//                    str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
//                    strings.add(str_number);
//
//                } while (cursor.moveToNext());
//
//                int a;
//                Set<String> str = new LinkedHashSet<>(strings);
//
//                numberChecker = new ArrayList<>(str);
//
//                int size = numberChecker.size();
//
//                for (a = 1; a <= size; a++) {
//                    String value = numberChecker.get(a - 1);
//                    mainStings(value);
//                    CallLogModel callLogItem = new CallLogModel(value, str_contact_name, str_call_type, str_date, CALL_TYPE_VALUE_MISSED);
//                    callLogModelArrayList.add(callLogItem);
//                }
//                if (callLogModelArrayList.size()==0){
//                    noCalls.setVisibility(View.VISIBLE);
//                }
//                animationView.setVisibility(View.INVISIBLE);
//                rv_call_logs.setAdapter(callLogAdapter);
//            } else {
//                noCalls.setVisibility(View.VISIBLE);
//                animationView.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            animationView.setVisibility(View.INVISIBLE);
//        }
//    }

//    private void mainStings(String number) {
////        StcVariable.callSimChecker(getContext());
////        int sim = StcVariable.simSubscription.size();
//        boolean isCheckSubscription = false;
//
////        if (sim == 1) {
////            isCheckSubscription = true;
////        } else if (sim == 2) {
////            isCheckSubscription = true;
////        }
//
//        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
//
//        number = Uri.encode(number);
//
//
//        Uri uriName = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
//        String[] projectionName = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
//
//        Cursor cursorName = getContext().getContentResolver().query(uriName, projectionName, null, null, null);
//        if (cursorName != null) {
//            if (cursorName.getCount() != 0) {
//                cursorName.moveToFirst();
//                str_contact_name = cursorName.getString(0);
//            } else {
//                str_contact_name = "Unknown";
//            }
//        }
//
//        Uri uri = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, number);
//        String[] projection = new String[]{CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.PHONE_ACCOUNT_ID};
//
//        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null
//                , sortOrder);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            str_call_type = cursor.getString(0);
//            switch (Integer.parseInt(str_call_type)) {
//                case CallLog.Calls.INCOMING_TYPE:
//                    str_call_type = "Incoming";
//                    break;
//                case CallLog.Calls.OUTGOING_TYPE:
//                    str_call_type = "Outgoing";
//                    break;
//                case CallLog.Calls.MISSED_TYPE:
//                    str_call_type = "Missed";
//                    break;
//                case CallLog.Calls.VOICEMAIL_TYPE:
//                    str_call_type = "Voicemail";
//                    break;
//                case CallLog.Calls.REJECTED_TYPE:
//                    str_call_type = "Rejected";
//                    break;
//                case CallLog.Calls.BLOCKED_TYPE:
//                    str_call_type = "Blocked";
//                    break;
//                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
//                    str_call_type = "Externally Answered";
//                    break;
//                default:
//                    str_call_type = "NA";
//            }
//
//            String date = cursor.getString(1);
//
//            phoneAccount = cursor.getString(2);
//
//            if (isCheckSubscription) {
//                TelecomManager tm2 = (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//
//                    List<PhoneAccountHandle> accountHandles = new ArrayList<>(tm2.getCallCapablePhoneAccounts());
//                    int simSize = accountHandles.size();
//
//                    if (simSize == 0) {
//                        phoneAccount = "";
//                    } else if (simSize == 1) {
//                        PhoneAccountHandle phoneAccountHandle = accountHandles.get(0);
//                        if (phoneAccount.equals(phoneAccountHandle.getId().substring(0, 20))) {
//                            phoneAccount = StcVariable.sim1Name;
//                        } else {
//                            phoneAccount = "";
//                        }
//                    } else if (simSize == 2) {
//                        PhoneAccountHandle phoneAccountHandle = accountHandles.get(0);
//                        PhoneAccountHandle phoneAccountHandle1 = accountHandles.get(1);
//                        if (phoneAccount.equals(phoneAccountHandle.getId().substring(0, 20))) {
//                            phoneAccount = StcVariable.sim1Name;
//                        } else if (phoneAccount.equals(phoneAccountHandle1.getId().substring(0, 20))) {
//                            phoneAccount = StcVariable.sim2Name;
//                        } else {
//                            phoneAccount = "";
//                        }
//                    }
//
//                }
//            }
//            else {
//                phoneAccount = "";
//            }
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
//            Date dateToday = new Date();
//            String today = dateFormat.format(dateToday);
//            String callDate = dateFormat.format(new Date(Long.parseLong(date)));
//
//            str_date = dateSelector(callDate,today);
//        }
//
//
//    }

//    private String dateSelector(String date,String today){
//        String callDt = "";
//        if (date.substring(6,8).equals(today.substring(6,8))){
//            int month = Integer.parseInt(today.substring(3,5));
//            int callMonth = Integer.parseInt(date.substring(3,5));
//            if (month == callMonth){
//                int day = Integer.parseInt(today.substring(0,2));
//                int callDay = Integer.parseInt(date.substring(0,2));
//                if (day == callDay){
//                    int hr = Integer.parseInt(today.substring(9,11));
//                    int callHr = Integer.parseInt(date.substring(9,11));
//                    if (hr == callHr){
//                        int min = Integer.parseInt(today.substring(12,14));
//                        int callMin = Integer.parseInt(date.substring(12,14));
//                        callDt = String.valueOf(min-callMin)+" minute ago";
//                    }
//                    else {
//                        callDt = String.valueOf(hr-callHr)+" hour ago";
//                    }
//                }else {
//                    callDt = String.valueOf(day-callDay)+" day ago";
//                }
//            }else {
//                callDt = String.valueOf(month-callMonth)+" month ago";
//            }
//        }
//        else {
//            callDt = "a years ago";
//        }
//        return callDt;
//    }

    private void Init() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putString("Value", "rec");
        editor.apply();
        swipeRefreshLayout = view.findViewById(R.id.activity_main_swipe_refresh_layout);
        rv_call_logs = view.findViewById(R.id.activity_main_rv);
        call_all = view.findViewById(R.id.all);
        searchRec = view.findViewById(R.id.search_rec);
        call_missed = view.findViewById(R.id.missed);
        noCalls = view.findViewById(R.id.no_call);
        animationView = view.findViewById(R.id.animationView);

        call_missed.setBackgroundColor(0x319D9999);
        rv_call_logs.setHasFixedSize(true);
        rv_call_logs.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_call_logs);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        BackgroundLogs backgroundLogs = new BackgroundLogs(RecentFragment.this);
        backgroundLogs.execute();
//                getCallLogs();
//            }
//        },500);

    }

    public void DeleteNumFromCallLog(String strNum) {
        try {
            String strUriCalls = "content://call_log/calls";
            Uri UriCalls = Uri.parse(strUriCalls);
            int a = getContext().getContentResolver().delete(UriCalls, CallLog.Calls.NUMBER + "=?", new String[]{strNum});
        } catch (Exception e) {
        }
    }

    private String noMoreSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
        return number;
    }


}