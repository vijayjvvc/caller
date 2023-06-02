package com.JvWorld.jcaller.settingsManagers;

import android.content.Context;
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
import com.JvWorld.jcaller.someVariable.StcVariable;

import java.util.ArrayList;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.VH> {

    private Context context;
    private ArrayList<String> contactsModelArrayList;
    private int a = 0;
    private SpeedDial speedDial;

    public BlockAdapter(Context context, ArrayList<String> contactsModelArrayList) {
        this.context = context;
        this.contactsModelArrayList = contactsModelArrayList;
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
        holder.contact_name.setText(StcVariable.getContactNameByNumber(contactsModelArrayList.get(position), context));
        holder.contact_number.setText(contactsModelArrayList
                .get(position));


    }


    @Override
    public int getItemCount() {
        a = contactsModelArrayList.size();
        return contactsModelArrayList.size();
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

            av.setOnClickListener(view -> Toast.makeText(context, "hello ", Toast.LENGTH_SHORT).show());
        }

    }
}
