package com.JvWorld.jcaller.director;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;

import java.util.ArrayList;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.VH> {

    private ArrayList<NumberModel> numberModelArrayList;
    private Context context;
    public String hold;

    public NumberAdapter(ArrayList<NumberModel> numberModelArrayList, Context context) {
        this.numberModelArrayList = numberModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NumberAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.show_numbers,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdapter.VH holder, int position) {
        holder.num.setText(numberModelArrayList.get(position).getNumber_list());

    }

    @Override
    public int getItemCount() {
        return numberModelArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView num;
        private ImageView msg;
        public VH(@NonNull View itemView) {
            super(itemView);
            num =itemView.findViewById(R.id.num_show);
            msg =itemView.findViewById(R.id.msg_number);
            btn_clicker();

        }

        private void btn_clicker() {
            num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String a = num.getText().toString();
                    Toast.makeText(context, a, Toast.LENGTH_SHORT).show();

                }
            });
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String a = num.getText().toString();
                    Toast.makeText(context, "Message delivered to "+a, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
