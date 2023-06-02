package com.JvWorld.jcaller.director;


import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.callHandler.Placecall;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.VH> {

    private ArrayList<String> numberModelArrayList;
    private Context context;
    public String hold;

    public NumberAdapter(ArrayList<String> numberModelArrayList, Context context) {
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
        holder.num.setText(numberModelArrayList.get(position));

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
            num.setOnLongClickListener(view -> {
                String a = num.getText().toString();
                StcVariable.alertBottomSheet("tel", a, context);
                return true;
            });

            msg.setOnClickListener(view -> {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.sms_send);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView sendBtn = dialog.findViewById(R.id.send_sms_btn);
                ImageView cancelBtn = dialog.findViewById(R.id.cancel_sms_btn);
                TextInputEditText msgBox = dialog.findViewById(R.id.sms_box);


                sendBtn.setOnClickListener(v -> {
                    String a = msgBox.getText().toString();
                    String number = num.getText().toString();
                    if (!a.equals("")) {
                        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                        PendingIntent in = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, a, null, null);
                        dialog.dismiss();
                        Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Please enter Something", Toast.LENGTH_SHORT).show();
                    }
                });

                cancelBtn.setOnClickListener(v -> dialog.dismiss());

                dialog.show();

            });
        }
    }
}
