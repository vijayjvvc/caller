package com.JvWorld.jcaller.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;

public class DialFragment extends Fragment {

    private TextView shownum, showmatch;
    private View view;
    private String number;
    private boolean long_click = false;
    private static final int REQUEST_CALL = 1;
    private ImageView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0,numcall,numhash,numstar,numcancel;

    private void initui() {
        shownum  = view.findViewById(R.id.phone_number_typed);
        showmatch  = view.findViewById(R.id.phone_number_matched);
        num0 = view.findViewById(R.id.num_0);
        num1 = view.findViewById(R.id.num_1);
        num2 = view.findViewById(R.id.num_2);
        num3 = view.findViewById(R.id.num_3);
        num4 = view.findViewById(R.id.num_4);
        num5 = view.findViewById(R.id.num_5);
        num6 = view.findViewById(R.id.num_6);
        num7 = view.findViewById(R.id.num_7);
        num8 = view.findViewById(R.id.num_8);
        num9 = view.findViewById(R.id.num_9);
        numcall = view.findViewById(R.id.num_call);
        numhash = view.findViewById(R.id.num_hash);
        numcancel = view.findViewById(R.id.num_cancel);
        numstar = view.findViewById(R.id.num_star);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dial,container,false);
        initui();
        onclick();
        return view;
    }

    private void getSet(String a){
        number = shownum.getText().toString();
        if (number.isEmpty()){
            numcancel.setVisibility(View.INVISIBLE);
        }
        else{
            numcancel.setVisibility(View.VISIBLE);
        }
        shownum.setText(number+a);

    }
    private void onclick() {
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                number = shownum.getText().toString();
//                shownum.setText(number+"1");
                getSet("1");

            }
        });
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                number = shownum.getText().toString();
//                shownum.setText(number+"2");
                getSet("2");
            }
        });
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                number = shownum.getText().toString();
//                shownum.setText(number+"2");
               getSet("3");
               }
        });
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("4");
            }
        });
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSet("5");
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("6");
            }
        });
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("7");
            }
        });
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("8");
            }
        });
        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("9");
            }
        });
        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!long_click){
                    getSet("0");
                }else {
                    long_click = false;
                }
            }
        });
        num0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getSet("+");
                long_click = true;
                return true;
            }
        });
        numhash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("#");
            }
        });
        numstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSet("*");
            }
        });
        numcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder(shownum.getText());
                stringBuilder.deleteCharAt(shownum.getText().length()-1);
                String setnum = stringBuilder.toString();
                if (setnum.isEmpty()){
                    shownum.setText("");
                    numcancel.setVisibility(View.INVISIBLE);
                }
                else{
                    shownum.setText(setnum);
                }
            }
        });
        numcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall();

            }
        });

    }

    private void makeCall() {
        String numc = shownum.getText().toString();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + numc;
//            Toast.makeText(getContext(), dial, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}