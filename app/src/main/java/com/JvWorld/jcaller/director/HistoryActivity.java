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
    String num1 = "", num2 = "", num3 = "";
    TextView noCalls;
    public String str_number, str_contact_name, str_call_type;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initUi();
    }

    private void initUi() {
        rc = findViewById(R.id.rc_calog_number);
        noCalls = findViewById(R.id.no_logs);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        num = intent.getStringExtra("num");
        arrayList = intent.getStringArrayListExtra("all number");
        rc.setHasFixedSize(true);
        startingProcess();
        rc.setLayoutManager(new LinearLayoutManager(this));
        modelArrayList = new ArrayList<>();
        adapter = new Adapter(modelArrayList, this);
        rc.setAdapter(adapter);
        FetchCallLogs();
    }

    private void startingProcess() {
        if (arrayList.size() == 1) {
            num1 = arrayList.get(0);
        } else if (arrayList.size() == 2) {
            num1 = arrayList.get(0);
            num2 = arrayList.get(1);
        } else if (arrayList.size() == 3) {
            num1 = arrayList.get(0);
            num2 = arrayList.get(1);
            num3 = arrayList.get(2);
        }


    }

    @SuppressLint({"Range", "NotifyDataSetChanged", "SimpleDateFormat"})
    public void FetchCallLogs() {

        modelArrayList.clear();

        // reading all data in descending order according to DATE
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Cursor cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);

        if (cursor != null) {
            if (cursor.getCount() != -1) {
                cursor.moveToFirst();
                do {
                    str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    str_number = stringSlicer(str_number);
                    str_contact_name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    str_contact_name = str_contact_name == null || str_contact_name.equals("") ? "Unknown" : str_contact_name;
                    String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    String simple = dateFormat.format(new Date(Long.parseLong(date)));
                    str_call_type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    String str_call_duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    str_call_duration = DurationFormat(str_call_duration);

                    switch (Integer.parseInt(str_call_type)) {
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

                    if (name.equals("Unknown")) {
                        if (num.equals(str_number)) {
                            Model callLogItem = new Model(str_call_type, simple, str_call_duration);
                            modelArrayList.add(callLogItem);
                        }
                    } else if (name.equals(str_contact_name) || str_number.equals(num) || str_number
                            .equals(num1) || str_number.equals(num2) || str_number.equals(num3)) {
                        Model callLogItem = new Model(str_call_type, simple, str_call_duration);
                        modelArrayList.add(callLogItem);
                    }

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "cursor is null", Toast.LENGTH_SHORT).show();
        }
        if (modelArrayList.isEmpty()) {
            noCalls.setVisibility(View.VISIBLE);
        } else {
            noCalls.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private String stringSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
//        if (number.length() >= 6) {
//            String v = number.substring(0, 3);
//            if (v.equals("+91")) {
//                int a = number.length();
//                number = "+91 " + number.substring(3, a);
//            } else if (number.length() == 10) {
//                number = "+91 " + number;
//            }
//        }
        return number;
    }

    private String DurationFormat(String duration) {
        String durationFormatted = null;
        if (Integer.parseInt(duration) < 60) {
            durationFormatted = duration + " sec";
        } else if (Integer.parseInt(duration) > 3600) {
            int min = Integer.parseInt(duration) / 60;
            int sec = Integer.parseInt(duration) % 60;
            int hour = min / 60;

            if (hour == 0) {
                if (sec == 0)
                    durationFormatted = min + " min";
                else
                    durationFormatted = min + " min " + sec + " sec";
            } else {
                if (hour >= 1) {
                    min = min - 60;
                }
                if (sec == 0)
                    durationFormatted = hour + " h " + min + " min";
                else
                    durationFormatted = hour + " h " + min + " min " + sec + " sec";
            }


        } else {
            int min = Integer.parseInt(duration) / 60;
            int sec = Integer.parseInt(duration) % 60;

            if (sec == 0)
                durationFormatted = min + " min";
            else
                durationFormatted = min + " min " + sec + " sec";

        }
        return durationFormatted;
    }

}