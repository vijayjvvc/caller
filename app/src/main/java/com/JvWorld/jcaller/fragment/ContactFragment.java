package com.JvWorld.jcaller.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.contacts.Demodel;

import java.util.ArrayList;
import java.util.Currency;


public class ContactFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    int CONTACT_SIZE = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact,container,false);

        initUi();
        return view;
    }



    private void initUi() {
        recyclerView = view.findViewById(R.id.rv_contacts_main);
        getContactsSize();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactsAdapter adapter = new ContactsAdapter(getContext(),getContacts());
        recyclerView.setAdapter(adapter);
    }



    @SuppressLint("Range")
    private ArrayList<ContactsModel> getContacts(){
        ArrayList<ContactsModel> list = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");
        Cursor cursor1 = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");



        int CONTACT_POSITION = 0;
        for (CONTACT_POSITION=0;CONTACT_POSITION<=CONTACT_SIZE-1; CONTACT_POSITION++){
            cursor.moveToPosition(CONTACT_POSITION);
            cursor1.moveToPosition(CONTACT_POSITION-1);
            if (CONTACT_POSITION>=1){
                String aa = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String ab = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (aa.equals(ab)){
//                    Toast.makeText(getContext(), "same", Toast.LENGTH_SHORT).show();
                }else {
                    list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME))
                            ,cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                }
            }else{
                list.add(new ContactsModel(cursor.getString(cursor.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.DISPLAY_NAME))
                        ,cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
            }
        }return list;
    }

    @SuppressLint("Range")
    private ArrayList<Demodel> getContactsSize(){
        ArrayList<Demodel> demodelArrayList = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
        ,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        while (cursor.moveToNext()){
            CONTACT_SIZE++;
            demodelArrayList.add(new Demodel(cursor.getString(cursor.getColumnIndex(ContactsContract
                    .CommonDataKinds.Phone.DISPLAY_NAME))));
        }return demodelArrayList;
    }
}