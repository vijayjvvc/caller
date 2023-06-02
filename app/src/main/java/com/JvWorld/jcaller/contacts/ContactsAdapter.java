package com.JvWorld.jcaller.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.director.Profile;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList;
    private int a = 0;


    public ContactsAdapter(Context context, ArrayList<ContactsModel> contactsModelArrayList) {
        this.context = context;
        this.contactsModelArrayList = contactsModelArrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contacts_call_layout_contact_fragment, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.contact_name.setText(contactsModelArrayList
                .get(position).getContacts_name());
        holder.contact_number.setText(contactsModelArrayList
                .get(position).getContacts_number());

        if (StcVariable.CONTACTS_VISIBLE_TEXT) {
            if (position == 0) {
                String ab = contactsModelArrayList.get(position).getContacts_name();
                ab = ab.substring(0, 1).toLowerCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                holder.contactStarter.setText(ab);
                holder.contactStarter.setVisibility(View.VISIBLE);
            } else if (position >= 1) {
                String ab = contactsModelArrayList.get(position).getContacts_name();
                ab = ab.substring(0, 1).toLowerCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String ba = contactsModelArrayList.get(position - 1).getContacts_name();
                ba = ba.substring(0, 1).toLowerCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                if (!ab.equals(ba)) {
                    holder.contactStarter.setText(ab);
                    holder.contactStarter.setVisibility(View.VISIBLE);
                }
            }
//            else if (position >=2) {
//                String ab = contactsModelArrayList.get(position).getContacts_name();
//                ab = ab.substring(0, 1).toLowerCase(Locale.ROOT).toUpperCase(Locale.ROOT);
//                String ba = contactsModelArrayList.get(position - 1).getContacts_name();
//                ba = ba.substring(0, 1).toLowerCase(Locale.ROOT).toUpperCase(Locale.ROOT);
//                if (!ab.equals(ba)) {
//                    holder.contactStarter.setText(ab);
//                    holder.contactStarter.setVisibility(View.VISIBLE);
//                }
//            }
        }

        String aa = contactsModelArrayList.get(position).getContacts_photo();
        if (aa != null) {
            Glide.with(context).load(aa).into(holder.contact_photo);
        } else {
            holder.contact_photo.setImageResource(R.drawable.ic_contacts);
        }

    }

    @Override
    public int getItemCount() {
        a = contactsModelArrayList.size();
        return contactsModelArrayList.size();
    }

    public void filterList(ArrayList<ContactsModel> filteredList) {
        contactsModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder {
        RelativeLayout av;
        TextView contact_name, contact_number, contactStarter;
        ImageView contact_photo;

        public VH(@NonNull View itemView) {
            super(itemView);
            contact_name = itemView.findViewById(R.id.contacts_name);
            contact_number = itemView.findViewById(R.id.contacts_number);
            contact_photo = itemView.findViewById(R.id.profile_pic_con);
            contactStarter = itemView.findViewById(R.id.text_name_starter);
            av = itemView.findViewById(R.id.av_card);
            av.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String con_name = contact_name.getText().toString();
                    String con_number = contact_number.getText().toString();
                    Intent intent = new Intent(context, Profile.class);
                    intent.putExtra("name_contact", con_name);
                    intent.putExtra("num_contact", con_number);
                    intent.putExtra("act_contact", "C");
                    context.startActivity(intent);
                }
            });
        }
    }
}
