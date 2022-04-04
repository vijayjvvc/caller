package com.JvWorld.jcaller.director;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private TextView contact_name;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private CardView callCard,historyCard;
    String name, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();

    }

    private void initUI() {
        contact_name = findViewById(R.id.con_name);
        callCard = findViewById(R.id.call_view_card);
        historyCard = findViewById(R.id.history);
        recyclerView = findViewById(R.id.con_number_show);
        imageView = findViewById(R.id.profile_pic);
        Intent intent = getIntent();
        name = intent.getStringExtra("name_contact");
        num = intent.getStringExtra("num_contact");
        contact_name.setText(name);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NumberAdapter adapter = new NumberAdapter(getContact(),this);
        recyclerView.setAdapter(adapter);
        btn();

    }

    private void btn() {
       callCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String dial = "tel:" +num;
               startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
           }
       });
       historyCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intentData  = new Intent(Profile.this,HistoryActivity.class);
               intentData.putExtra("name",name);
               intentData.putExtra("num",num);
               startActivity(intentData);
           }
       });
    }

    @SuppressLint("Range")
    private ArrayList<NumberModel> getContact(){
        ArrayList<NumberModel> numberModelArrayList = new ArrayList<>();
        if (name.equals("Unknown")){
            numberModelArrayList.add(new NumberModel(num));
//            imageView.setBackground(0xF44336);

        }else{
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");

            while (cursor.moveToNext()){
                String ab = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (ab.equals(name)){
                    String aa = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    if (aa!=null){
                        Glide.with(this).load(aa).into(imageView);
                    }
                    numberModelArrayList.add(new NumberModel(cursor.getString(cursor.getColumnIndex(ContactsContract.
                            CommonDataKinds.Phone.NUMBER))));
                }
            }
        }

        return numberModelArrayList;
    }
}