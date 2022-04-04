package com.JvWorld.jcaller.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.calllog.CallLogAdapter;
import com.JvWorld.jcaller.calllog.CallLogModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecentFragment extends Fragment {

    private View view;
    private ArrayList<CallLogModel> callLogModelArrayList;
    private RecyclerView rv_call_logs;
    private CallLogAdapter callLogAdapter;
    private TextView call_all,call_missed;
    private int CALL_TYPE_VALUE_MISSED = 0;

    public String str_number, str_contact_name, str_call_type;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recent, container, false);
        Init();


            FetchCallLogs(CALL_TYPE_VALUE_MISSED);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               FetchCallLogs(CALL_TYPE_VALUE_MISSED);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        call_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALL_TYPE_VALUE_MISSED=0;
                call_all.setBackgroundColor(Color.WHITE);
                call_missed.setBackgroundColor(0x319D9999);
                FetchCallLogs(CALL_TYPE_VALUE_MISSED);
            }
        });
        call_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALL_TYPE_VALUE_MISSED=1;
                call_missed.setBackgroundColor(Color.WHITE);
                call_all.setBackgroundColor(0x319D9999);
                FetchCallLogs(CALL_TYPE_VALUE_MISSED);
            }
        });
        return view;
    }


    private void Init() {
        swipeRefreshLayout = view.findViewById(R.id.activity_main_swipe_refresh_layout);
        rv_call_logs = view.findViewById(R.id.activity_main_rv);
        call_all = view.findViewById(R.id.all);
        call_missed = view.findViewById(R.id.missed);
        call_all.setBackgroundColor(Color.WHITE);
        call_missed.setBackgroundColor(0x319D9999);
        rv_call_logs.setHasFixedSize(true);
        rv_call_logs.setLayoutManager(new LinearLayoutManager(getContext()));
        callLogModelArrayList = new ArrayList<>();
        callLogAdapter = new CallLogAdapter(getContext(), callLogModelArrayList);
        rv_call_logs.setAdapter(callLogAdapter);
    }



    @SuppressLint({"Range", "NotifyDataSetChanged", "SimpleDateFormat"})
    public void FetchCallLogs(int CALL_TYPE_VALUE_MISSED) {

        //clearing the arraylist
        callLogModelArrayList.clear();

        // reading all data in descending order according to DATE
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Cursor cursor = getActivity().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);


        //looping through the cursor to add data into arraylist

        if (CALL_TYPE_VALUE_MISSED==1){
            while (cursor.moveToNext()){
                CALL_TYPE_VALUE_MISSED = 1;
                str_call_type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));

                if (Integer.parseInt(str_call_type) == CallLog.Calls.MISSED_TYPE) {
                    str_call_type = "Missed";
                    str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    str_contact_name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    str_contact_name = str_contact_name==null || str_contact_name.equals("") ? "Unknown" : str_contact_name;

                    String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                    String simple = dateFormat.format(new Date(Long.parseLong(date)));
                    CallLogModel callLogItem = new CallLogModel(str_number, str_contact_name, str_call_type,simple,CALL_TYPE_VALUE_MISSED);

                    callLogModelArrayList.add(callLogItem);
                }
            }
            callLogAdapter.notifyDataSetChanged();
        }else{
            while (cursor.moveToNext()){
                CALL_TYPE_VALUE_MISSED = 0;
                str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                str_contact_name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                str_contact_name = str_contact_name==null || str_contact_name.equals("") ? "Unknown" : str_contact_name;
                String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                String simple = dateFormat.format(new Date(Long.parseLong(date)));
                str_call_type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));

                switch(Integer.parseInt(str_call_type)){
                    case CallLog.Calls.INCOMING_TYPE:
                        str_call_type = "Incoming";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        str_call_type = "Outgoing";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        str_call_type = "Missed";
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                        str_call_type = "Voicemail";
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        str_call_type = "Rejected";
                        break;
                    case CallLog.Calls.BLOCKED_TYPE:
                        str_call_type = "Blocked";
                        break;
                    case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                        str_call_type = "Externally Answered";
                        break;
                    default:
                        str_call_type = "NA";
                }

                CallLogModel callLogItem = new CallLogModel(str_number, str_contact_name, str_call_type, simple, CALL_TYPE_VALUE_MISSED);

                callLogModelArrayList.add(callLogItem);
            }
            callLogAdapter.notifyDataSetChanged();
        }
    }
}