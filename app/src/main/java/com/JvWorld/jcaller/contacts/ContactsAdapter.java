package com.JvWorld.jcaller.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.director.Profile;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList;


    public ContactsAdapter(Context context, ArrayList<ContactsModel> contactsModelArrayList) {
        this.context = context;
        this.contactsModelArrayList = contactsModelArrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contacts_call_layout,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.contact_name.setText(contactsModelArrayList
                    .get(position).getContacts_name());
            holder.contact_number.setText(contactsModelArrayList
                    .get(position).getContacts_number());


    }

    @Override
    public int getItemCount() {
        return contactsModelArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        CardView av;
        TextView contact_name,contact_number;
        public VH(@NonNull View itemView) {
            super(itemView);

            contact_name = itemView.findViewById(R.id.contacts_name);
            contact_number = itemView.findViewById(R.id.contacts_number);
            av = itemView.findViewById(R.id.av_card);
            av.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String con_name = contact_name.getText().toString();
//                    String con_number = contact_number.getText().toString();
                    Intent intent = new Intent(context, Profile.class);
                    intent.putExtra("name_contact",con_name);
//                    intent.putExtra("num_contact",con_number);
                    context.startActivity(intent);
                }
            });
        }
    }
}
