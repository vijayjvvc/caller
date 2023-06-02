package com.JvWorld.jcaller.simDector;

import android.app.Dialog;
import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.callHandler.Placecall;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SimAdapter extends RecyclerView.Adapter<SimAdapter.VH> {

    Context context;
    ArrayList<SimModel> sim;
    private String dial, scheme;
    private Dialog dialog;

    public SimAdapter(Context context, ArrayList<SimModel> sim, String dial, String scheme, Dialog dialog) {
        this.context = context;
        this.sim = sim;
        this.dial = dial;
        this.scheme = scheme;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_of_sim, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        int simss = Integer.parseInt(sim.get(position).getSim());
        holder.simNumber.setText(sim.get(position).getNumberSi());
        holder.operatorName.setText(sim.get(position).getNameSim());
        holder.simCard.setText(String.valueOf(simss + 1));

    }

    @Override
    public int getItemCount() {
        return sim.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView operatorName, simNumber, simCard;

        public VH(@NonNull View itemView) {
            super(itemView);
            operatorName = itemView.findViewById(R.id.operator_name);
            simCard = itemView.findViewById(R.id.text_sim);
            simNumber = itemView.findViewById(R.id.sim_number);
            itemView.setOnClickListener(view -> {
                if (operatorName.getText().equals("No service")) {
                    Toast.makeText(context, "Cann't Place call", Toast.LENGTH_SHORT).show();
                } else {
                    int i = Integer.parseInt(simCard.getText().toString());
                    Placecall placecall = new Placecall(context);
                    placecall.placeCall(scheme, dial, i);
                }
                dialog.dismiss();

            });
        }
    }
}
