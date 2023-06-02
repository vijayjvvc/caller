package com.JvWorld.jcaller.settingsManagers;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.contacts.ContactsModel;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.director.Profile;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.bumptech.glide.Glide;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Locale;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.VH> {

    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList;
    private SpeedDial speedDial;

    public SettingsAdapter(Context context, ArrayList<ContactsModel> contactsModelArrayList, SpeedDial speedDial) {
        this.context = context;
        this.contactsModelArrayList = contactsModelArrayList;
        this.speedDial = speedDial;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contacts_call_layout, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
//        209
        holder.contact_name.setText(contactsModelArrayList
                .get(position).getContacts_name() + position);
        holder.contact_number.setText(contactsModelArrayList
                .get(position).getContacts_number());


    }

    @Override
    public int getItemCount() {
//        a = contactsModelArrayList.size();
        return contactsModelArrayList.size();
    }

    public void filterList(ArrayList<ContactsModel> filteredList) {
        contactsModelArrayList = filteredList;
        notifyDataSetChanged();
    }


    public class VH extends RecyclerView.ViewHolder {
        RelativeLayout av;
        TextView contact_name, contact_number, contactStarter;

        //        ImageView contact_photo;
        public VH(@NonNull View itemView) {
            super(itemView);
            contact_name = itemView.findViewById(R.id.contacts_name);
            contact_number = itemView.findViewById(R.id.contacts_number);
//            contact_photo = itemView.findViewById(R.id.profile_pic_con);
            contactStarter = itemView.findViewById(R.id.text_name_starter);
            av = itemView.findViewById(R.id.av_card);
            contact_number.setVisibility(View.VISIBLE);

            av.setOnClickListener(view -> speedDial.numberId(contact_number.getText().toString()));
        }

    }
}
