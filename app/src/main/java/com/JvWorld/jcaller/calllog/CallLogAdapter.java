package com.JvWorld.jcaller.calllog;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.director.Profile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.MyViewHolder> {

    Context context;
    ArrayList<CallLogModel> callLogModelArrayList;
    CallLogModel currentLog;
    private int aa = 0;


    public CallLogAdapter(Context context, ArrayList<CallLogModel> callLogModelArrayList) {
        this.context = context;
        this.callLogModelArrayList = callLogModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.recent_call_layout, parent, false);
        viewHolder = new MyViewHolder(viewItem);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            currentLog = callLogModelArrayList.get(position);

            if (aa==0){
                if (currentLog.getCallType().equals("Missed")){
                    holder.tv_contact_name.setTextColor(Color.RED);
                    holder.tv_ph_num.setTextColor(Color.RED);
                }
            }else{
                    holder.tv_contact_name.setTextColor(Color.RED);
                    holder.tv_ph_num.setTextColor(Color.RED);
                    holder.today.setVisibility(View.VISIBLE);
                    holder.today.setText(currentLog.getCallDateToday());
            }
            holder.tv_ph_num.setText(currentLog.getPhNumber());
            holder.tv_contact_name.setText(currentLog.getContactName());

    }


    @Override
    public int getItemCount() {
        aa = callLogModelArrayList.get(0).getCallTYpe();
        int a = callLogModelArrayList.size();
        int v = 0;

        if (aa!=1){
            if (a>=70){
                v=a-50;
            }
        }
        return callLogModelArrayList.size()-v;
//        return callLogModelArrayList==null ? 0 : callLogModelArrayList.size()-v;
        }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        LinearLayout callCard, info;
        TextView tv_ph_num, tv_contact_name, today;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ph_num = itemView.findViewById(R.id.layout_call_log_ph_no);
            tv_contact_name = itemView.findViewById(R.id.layout_call_log_contact_name);
            callCard = itemView.findViewById(R.id.call_card);
            info = itemView.findViewById(R.id.info_contact);
            today = itemView.findViewById(R.id.layout_call_today);
            cardView = itemView.findViewById(R.id.layout_call_log_cardview);
            allBtnHandler();

        }

        private void allBtnHandler() {

            callCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dial = "tel:" +tv_ph_num.getText().toString();
                    context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String con_name = tv_contact_name.getText().toString();
                    String con_number = tv_ph_num.getText().toString();
                    Intent intent = new Intent(context, Profile.class);
                    intent.putExtra("name_contact",con_name);
                    intent.putExtra("num_contact",con_number);
                    context.startActivity(intent);
                }
            });
        }

    }

}
