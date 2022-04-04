package com.JvWorld.jcaller.director;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.VH> {

    ArrayList<Model> modelArrayList;
    Context context;

    public Adapter(ArrayList<Model> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.call_history,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (modelArrayList.get(position).getCallType().equals("Missed Call")|| modelArrayList
                .get(position).getCallType().equals("Rejected Call") || modelArrayList.get(position).getCallType().equals("Blocked Call")){
            holder.dates.setText(modelArrayList.get(position).getDate());
            holder.callType.setText(modelArrayList.get(position).getCallType());
            holder.duration.setText("");
        }else if(modelArrayList.get(position).getCallType().equals("Outgoing Call") && modelArrayList
                .get(position).getDuration().equals("0 sec")){
            holder.dates.setText(modelArrayList.get(position).getDate());
            holder.callType.setText("Didn't connect");
            holder.duration.setText("");
        }else if(modelArrayList.get(position).getCallType().equals("Incoming Call") && modelArrayList
                .get(position).getDuration().equals("0 sec")){
            holder.dates.setText(modelArrayList.get(position).getDate());
            holder.callType.setText("Blocked Call");
            holder.duration.setText("");
        }else{
            holder.callType.setText(modelArrayList.get(position).getCallType());
            holder.duration.setText(modelArrayList.get(position).getDuration());
            holder.dates.setText(modelArrayList.get(position).getDate());
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView duration,callType,dates;
        public VH(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.call_duration);
            callType = itemView.findViewById(R.id.call_type);
            dates = itemView.findViewById(R.id.call_date);
        }
    }
}
