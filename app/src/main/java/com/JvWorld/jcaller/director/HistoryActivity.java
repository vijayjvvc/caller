package com.JvWorld.jcaller.director;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.calllog.CallLogAdapter;
import com.JvWorld.jcaller.calllog.CallLogModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rc;
    ArrayList<Model> modelArrayList;
    Adapter adapter;
    String name, num;
    TextView noCalls;
    public String str_number, str_contact_name, str_call_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rc = findViewById(R.id.rc_calog_number);
        noCalls = findViewById(R.id.no_logs);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        num = intent.getStringExtra("num");
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        modelArrayList = new ArrayList<>();
        adapter = new Adapter(modelArrayList, this);
        rc.setAdapter(adapter);
        FetchCallLogs();
    }
    @SuppressLint({"Range", "NotifyDataSetChanged", "SimpleDateFormat"})
    public void FetchCallLogs() {

        //clearing the arraylist
        modelArrayList.clear();

        // reading all data in descending order according to DATE
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Cursor cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);


        //looping through the cursor to add data into arraylist

            while (cursor.moveToNext()){
                str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                str_contact_name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                str_contact_name = str_contact_name==null || str_contact_name.equals("") ? "Unknown" : str_contact_name;
                String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                String simple = dateFormat.format(new Date(Long.parseLong(date)));
                str_call_type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
               String str_call_duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                str_call_duration = DurationFormat(str_call_duration);

                switch(Integer.parseInt(str_call_type)){
                    case CallLog.Calls.INCOMING_TYPE:
                        str_call_type = "Incoming Call";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        str_call_type = "Outgoing Call";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        str_call_type = "Missed Call";
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                        str_call_type = "Voicemail";
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        str_call_type = "Rejected Call";
                        break;
                    case CallLog.Calls.BLOCKED_TYPE:
                        str_call_type = "Blocked Call";
                        break;
                    case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                        str_call_type = "Externally Answered";
                        break;
                    case CallLog.Calls.FEATURES_VOLTE:
                        str_call_type = "VOLTE";
                        break;
                    case CallLog.Calls.FEATURES_WIFI:
                        str_call_type = "WIFI";
                        break;
                    default:
                        str_call_type = "NA";
                }

                if (name.equals("Unknown")){
                    if (num.equals(str_number)){
                        Model callLogItem = new Model(str_call_type,simple,str_call_duration);
                        modelArrayList.add(callLogItem);
                    }
                }else if (name.equals(str_contact_name)||str_number.equals(num)){
                    Model callLogItem = new Model(str_call_type,simple,str_call_duration);
                    modelArrayList.add(callLogItem);
                }

            }
            if (modelArrayList.isEmpty()){
                noCalls.setVisibility(View.VISIBLE);
            }else {
                noCalls.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
    }
    private String DurationFormat(String duration) {
        String durationFormatted=null;
        if(Integer.parseInt(duration) < 60){
            durationFormatted = duration+" sec";
        }else if(Integer.parseInt(duration)>3600){
            int min = Integer.parseInt(duration)/60;
            int sec = Integer.parseInt(duration)%60;
            int hour = min/60;

            if (hour==0){
                if(sec==0)
                    durationFormatted = min + " min" ;
                else
                    durationFormatted = min + " min " + sec + " sec";
            }else {
                if (hour>=1){
                    min = min-60;
                }
                if(sec==0)
                    durationFormatted = hour+" h "+ min + " min" ;
                else
                    durationFormatted = hour+" h "+ min + " min " + sec + " sec";
            }


        }else{
            int min = Integer.parseInt(duration)/60;
            int sec = Integer.parseInt(duration)%60;

            if(sec==0)
                durationFormatted = min + " min" ;
            else
                durationFormatted = min + " min " + sec + " sec";

        }
        return durationFormatted;
    }

}