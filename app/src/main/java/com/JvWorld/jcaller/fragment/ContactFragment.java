package com.JvWorld.jcaller.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.contacts.BackgroundContacts;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.contacts.Demodel;
import com.JvWorld.jcaller.contacts.EditContactActivity;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;


public class ContactFragment extends Fragment {

    private View view;

    public RecyclerView recyclerView;
    public ContactsAdapter adapter;
    public LottieAnimationView conLoader;
    public TextInputEditText search;
    public TextView textNoCon;
    private int CONTACT_SIZE = 0;
    //    private ArrayList<ContactsModel> list;
    private ImageView createContact;
//    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);

        initUi();

        return view;
    }

    private void buttonHandler() {
        search.addTextChangedListener(new TextWatcher() {
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
        createContact.setOnClickListener(view -> getActivity().startActivity(new Intent(getContext(), EditContactActivity.class)));
    }

    private void filter(String searchQuery) {
        ArrayList<ContactsModel> filterList = new ArrayList<>();
        for (ContactsModel contactsModel : StcVariable.contactModelArrayList) {
            boolean conName = contactsModel.getContacts_name().toLowerCase().contains(searchQuery.toLowerCase());
            boolean conNum = contactsModel.getContacts_number().toLowerCase().contains(searchQuery.toLowerCase());
            if (conName || conNum) {
                filterList.add(contactsModel);
            }
        }
        adapter.filterList(filterList);
    }


    private void initUi() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putString("Value", "con");
        editor.apply();
        StcVariable.CONTACTS_VISIBLE_TEXT = true;
        createContact = view.findViewById(R.id.create_con);
        recyclerView = view.findViewById(R.id.rv_contacts_main);
        conLoader = view.findViewById(R.id.con_loader);
        conLoader.setVisibility(View.VISIBLE);

        search = view.findViewById(R.id.search_con);
        textNoCon = view.findViewById(R.id.just_contact);
//        getContactsSize();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

//            adapter = new ContactsAdapter(getContext(), StcVariable.contactModelArrayList);
//            recyclerView.setAdapter(adapter);
//            if (StcVariable.contactModelArrayList.size() == 0) {
//                textNoCon.setVisibility(View.VISIBLE);
//            }else {
//                search.setHint("search from "+StcVariable.contactModelArrayList.size());
//            }
            BackgroundContacts backgroundContacts = new BackgroundContacts(ContactFragment.this);
            backgroundContacts.execute();
            buttonHandler();

//            conLoader.setVisibility(View.GONE);
        }

    }


    //    @SuppressLint("Range")
//    private ArrayList<ContactsModel> getContacts(){
//
//        list = new ArrayList<>();
//        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
//        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");
//        Cursor cursor1 = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
//        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");
//
//
//
//        int CONTACT_POSITION = 0;
//        for (CONTACT_POSITION=0;CONTACT_POSITION<=CONTACT_SIZE-1; CONTACT_POSITION++){
//            cursor.moveToPosition(CONTACT_POSITION);
//            cursor1.moveToPosition(CONTACT_POSITION-1);
//            String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//            if (CONTACT_POSITION>=1){
//                String aa = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String ab = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                if (aa.equals(ab)){
////                    Toast.makeText(getContext(), "same", Toast.LENGTH_SHORT).show();
//                }else {
////                    number = number.replaceAll("\\s", "");
//                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    number = stringSlicer(number);
//                    list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
//                            .CommonDataKinds.Phone.DISPLAY_NAME))
//                            ,number,photo));
//                }
//            }else{
//                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                number = stringSlicer(number);
//                list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
//                        .CommonDataKinds.Phone.DISPLAY_NAME))
//                        ,number,photo));
//            }
//        }
//        recyclerView.setAdapter(adapter);
//        return list;
//    }
//
//    @NonNull
//    @SuppressLint("Range")
//    private ArrayList<Demodel> getContactsSize(){
//        ArrayList<Demodel> demodelArrayList = new ArrayList<>();
//        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
//        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");
//
//        while (cursor.moveToNext()){
//            CONTACT_SIZE++;
//            demodelArrayList.add(new Demodel(cursor.getString(cursor.getColumnIndex(ContactsContract
//                    .CommonDataKinds.Phone.DISPLAY_NAME))));
//        }return demodelArrayList;
//    }
//
//    private String stringSlicer(String strValue){
//        String number = strValue;
//        number = number.replaceAll(" ","");
//        number = number.replaceAll("  ","");
//        number = number.replaceAll("-","");
//        if (number.length()>=6){
//            String v = number.substring(0,3);
//            if (v.equals("+91")) {
//                int a = number.length();
//                number = "+91 " + number.substring(3, a);
//            }else if (number.length()==10){
//                number = "+91 " + number;
//            }
//        }
//        return number;
//    }


//    AsyncTask<> asyncTask = new AsyncTask() {
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            return null;
//        }
//    };
}