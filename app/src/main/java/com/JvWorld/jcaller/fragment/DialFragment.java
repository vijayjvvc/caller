package com.JvWorld.jcaller.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.callHandler.Placecall;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.sql.SQLDataException;
import java.util.ArrayList;

public class DialFragment extends Fragment {

    private TextView shownum, showmatch;
    private View view;
    private String number;
    private boolean long_click = false;
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_CALL_CONTACTS = 11;
    private DatabaseManagerFav databaseManagerFav;
    private ImageView num1, num2, num3, num4, num5, num6, num7, num8, num9, num0, numcall, numhash, numstar, numcancel;

    private void initui() {
        StcVariable.isDarkMode(getContext());
        shownum = view.findViewById(R.id.phone_number_typed);
        showmatch = view.findViewById(R.id.phone_number_matched);
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

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CALL_CONTACTS);
        } else {
            shownum.addTextChangedListener(new TextWatcher() {
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dial, container, false);
        SharedPreferences.Editor editor = getContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putString("Value", "dial");
        editor.apply();
        initui();
        onclick();
        onLongClick();
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

        num1.setOnClickListener(view -> getSet("1"));

        num2.setOnClickListener(view -> getSet("2"));

        num3.setOnClickListener(view -> getSet("3"));

        num4.setOnClickListener(view -> getSet("4"));
        num5.setOnClickListener(view -> getSet("5"));
        num6.setOnClickListener(view -> getSet("6"));
        num7.setOnClickListener(view -> getSet("7"));
        num8.setOnClickListener(view -> getSet("8"));
        num9.setOnClickListener(view -> getSet("9"));
        num0.setOnClickListener(view -> {
            if (!long_click) {
                getSet("0");
            } else {
                long_click = false;
            }
        });
        num0.setOnLongClickListener(view -> {
            getSet("+");
            long_click = true;
            return true;
        });
        numhash.setOnClickListener(view -> getSet("#"));
        numstar.setOnClickListener(view -> getSet("*"));

        numcancel.setOnClickListener(view -> {
            StringBuilder stringBuilder = new StringBuilder(shownum.getText());
            stringBuilder.deleteCharAt(shownum.getText().length() - 1);
            String setnum = stringBuilder.toString();
            if (setnum.isEmpty()) {
                shownum.setText("");
                numcancel.setVisibility(View.INVISIBLE);
            } else {
                shownum.setText(setnum);
            }
        });
        numcall.setOnClickListener(view -> makeCall());

    }

    private void onLongClick() {
        databaseManagerFav = new DatabaseManagerFav(getContext());
        try {
            databaseManagerFav.open();

        } catch (SQLiteException | SQLDataException throwables) {
            throwables.printStackTrace();
        }
        num1.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(1);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num2.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(2);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num3.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(3);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num4.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(4);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num5.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(5);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num6.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(6);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num7.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(7);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num8.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(8);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });

        num9.setOnLongClickListener((View.OnLongClickListener) view -> {
            String aa = databaseManagerFav.fetchSpeedDial(9);
            if (aa.equals("")) {
                Toast.makeText(getContext(), "Speed dial not set.\n To set goto Settings", Toast.LENGTH_SHORT).show();
            } else {
                makeCallLong(aa);
            }
            return true;
        });
    }

    private void makeCallLong(String aa) {
        if (!aa.equals("")) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                StcVariable.alertBottomSheet("tel", aa, getContext());
            }
        }
    }

    private void makeCall() {
        String numc = shownum.getText().toString();
        if (!numc.equals("")) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                StcVariable.alertBottomSheet("tel", numc, getContext());
            }
        }
    }

    private void filter(String searchQuery) {
        ArrayList<ContactsModel> filterList = new ArrayList<>();
        if (!searchQuery.equals("")) {
            for (ContactsModel contactsModel : StcVariable.contactsModelArrayList) {
                boolean conName = contactsModel.getContacts_name().toLowerCase().contains(searchQuery.toLowerCase());
                boolean conNum = contactsModel.getContacts_number().toLowerCase().contains(searchQuery.toLowerCase());
                if (conNum) {
                    filterList.add(contactsModel);
                }
            }
            if (filterList.size() >= 1) {
                showmatch.setVisibility(View.VISIBLE);
                showmatch.setText(filterList.get(0).getContacts_name());
            } else {
                showmatch.setVisibility(View.VISIBLE);
                showmatch.setText("");
            }

        } else {
            showmatch.setVisibility(View.INVISIBLE);
        }

        showmatch.setOnClickListener(view1 -> {
            if (filterList.size() >= 1) {
                shownum.setText(filterList.get(0).getContacts_number());
            }
        });


//        adapter.filterList(filterList);
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
        if (requestCode == REQUEST_CALL_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Accepted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}