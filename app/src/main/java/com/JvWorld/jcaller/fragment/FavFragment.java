package com.JvWorld.jcaller.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.databaseHandler.DatabaseHelper;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.sql.SQLDataException;
import java.util.ArrayList;


public class FavFragment extends Fragment {

    RecyclerView recyclerView;
    private View view;
    private String name, photo;
    private TextView textFav;
    private ArrayList<ContactsModel> contactsModelArrayList;
    private ContactsAdapter contactsAdapter;
    private DatabaseManagerFav databaseManagerFav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        StcVariable.CONTACTS_VISIBLE_TEXT = false;
        SharedPreferences.Editor editor = getContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putString("Value", "fav");
        editor.apply();
        recyclerView = view.findViewById(R.id.rv_fav_main);
        textFav = view.findViewById(R.id.just_fav);

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseManagerFav = new DatabaseManagerFav(getContext());
        try {
            databaseManagerFav.open();
        } catch (SQLiteException | SQLDataException throwables) {
            throwables.printStackTrace();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsAdapter = new ContactsAdapter(getContext(), getFavContacts());
        recyclerView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    private ArrayList<ContactsModel> getFavContacts() {

        contactsModelArrayList = new ArrayList<>();
        contactsModelArrayList.clear();

        Cursor cursor = databaseManagerFav.fetch();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NUMBER));
                String name = getContactNameByNumber(number);
                contactsModelArrayList.add(new ContactsModel(name, number, photo));
            } while (cursor.moveToNext());
        }
        if (contactsModelArrayList.size() == 0) {
            textFav.setVisibility(View.VISIBLE);
        }
        return contactsModelArrayList;
    }


    @NonNull
    private String stringSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
        return number;
    }

    public String getContactNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI};

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
                photo = cursor.getString(1);
            } else {
                name = "Unknown";
                photo = null;
            }
            cursor.close();
        } else {
            name = "Unknown";
        }
        return name;
    }
}