package com.JvWorld.jcaller.director;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.JvWorld.jcaller.MainActivity;
import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.callHandler.Placecall;
import com.JvWorld.jcaller.contacts.ContactsAdapter;
import com.JvWorld.jcaller.databaseHandler.DatabaseHelper;
import com.JvWorld.jcaller.databaseHandler.DatabaseManagerFav;
import com.JvWorld.jcaller.fragment.ContactFragment;
import com.JvWorld.jcaller.fragment.RecentFragment;
import com.JvWorld.jcaller.someVariable.StcVariable;
import com.bumptech.glide.Glide;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Profile extends AppCompatActivity {

    String name, num, activity;
    private RecyclerView recyclerView;
    boolean ADD_TOO_CONTACT = false;
    ArrayList<String> numberModelArrayList;
    private TextView contact_name, addContact, addToFavCardText, addToBlockCardText;
    private ImageView imageView, back, delImg;
    private CardView callCard, historyCard, whatsCard, shareCard, deleteCard, favCard, ringtoneCard, blockCard;
    private DatabaseManagerFav databaseManagerFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StcVariable.isDarkMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();

    }

    private void initUI() {

        contact_name = findViewById(R.id.con_name);
        callCard = findViewById(R.id.call_view_card);
        whatsCard = findViewById(R.id.whats_view_card);
        historyCard = findViewById(R.id.history);
        recyclerView = findViewById(R.id.con_number_show);
        back = findViewById(R.id.i1);
        addContact = findViewById(R.id.del_card_text);
        delImg = findViewById(R.id.del_card_text_img);
        imageView = findViewById(R.id.profile_pic);
        addToBlockCardText = findViewById(R.id.block_card_text);
        Intent intent = getIntent();
        name = intent.getStringExtra("name_contact");
        num = intent.getStringExtra("num_contact");
        activity = intent.getStringExtra("act_contact");
        contact_name.setText(name);

        shareCard = findViewById(R.id.share_card);
        deleteCard = findViewById(R.id.del_card);
        blockCard = findViewById(R.id.block_card);
        favCard = findViewById(R.id.add_to_fav_card);
        addToFavCardText = findViewById(R.id.add_to_fav_card_text);
        ringtoneCard = findViewById(R.id.ringtone_card);

        if (!num.equals("")) {
            getContactNameByNumber(num);
//            getContactNumberByName(name);
        }


        btn();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (contact_name.getText().equals("Unknown") || contact_name.getText() == null) {
            addContact.setText("Add to Contact");
            addContact.setTextColor(getResources().getColor(R.color.blue));
            delImg.setVisibility(View.INVISIBLE);
            ADD_TOO_CONTACT = true;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NumberAdapter adapter = new NumberAdapter(getContact(), this);
        recyclerView.setAdapter(adapter);

        databaseManagerFav = new DatabaseManagerFav(this);
        try {
            databaseManagerFav.open();
        } catch (SQLiteException | SQLDataException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        impCheck();

    }

    private void impCheck() {
        if (databaseManagerFav.fetchOne(stringSlicer(num))) {
            addToFavCardText.setText(R.string.removeFromFavourites);
            StcVariable.ADD_CARD_TO_FAVOURITE = true;
        } else {
            StcVariable.ADD_CARD_TO_FAVOURITE = false;
        }
        if (databaseManagerFav.fetchOneBlock(stringSlicer(num))) {
            addToBlockCardText.setText("Unblock this Contacts");
            StcVariable.ADD_CARD_TO_BLOCK = true;
        } else {
            StcVariable.ADD_CARD_TO_BLOCK = false;
        }
    }

    @SuppressLint("Range")
    private void btn() {

        deleteCard.setOnClickListener(view -> {
            if (!ADD_TOO_CONTACT) {
                deleteContacts(num);
            }
        });

        blockCard.setOnClickListener(view -> {
            if (StcVariable.ADD_CARD_TO_BLOCK) {
                databaseManagerFav.deleteBlock(StcVariable.BLOCK_ID_CONTACTS);
                addToBlockCardText.setText("Block this Contacts");
                StcVariable.ADD_CARD_TO_BLOCK = false;
            } else {
                Cursor cursor = databaseManagerFav.fetchBlockNumbers();
                int ID = 1;
                if (cursor != null) {
                    cursor.moveToLast();
                    if (cursor.getPosition() == -1) {
                        databaseManagerFav.insertBlockNumbers(stringSlicer(num), ID);
                    } else {
                        int a = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                        databaseManagerFav.insertBlockNumbers(stringSlicer(num), ID + a);
                    }
                    addToBlockCardText.setText("Unblock this Contacts");
                } else {
                    Toast.makeText(this, "null is null", Toast.LENGTH_SHORT).show();
                }
                StcVariable.ADD_CARD_TO_BLOCK = true;
            }
        });

        favCard.setOnClickListener(view -> {

            if (StcVariable.ADD_CARD_TO_FAVOURITE) {
                databaseManagerFav.delete(StcVariable.FAVOURITE_ID_CONTACTS);
                addToFavCardText.setText(R.string.add_to_favourite);
                StcVariable.ADD_CARD_TO_FAVOURITE = false;
            } else {
                Cursor cursor = databaseManagerFav.fetch();
                int ID = 1;
                if (cursor != null) {
                    cursor.moveToLast();
                    if (cursor.getPosition() == -1) {
                        databaseManagerFav.insert(stringSlicer(num), ID);
                        addToFavCardText.setText(R.string.removeFromFavourites);

                    } else {
                        int a = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                        databaseManagerFav.insert(stringSlicer(num), ID + a);
                        addToFavCardText.setText(R.string.removeFromFavourites);
                    }
                } else {
                    Toast.makeText(this, "null is null", Toast.LENGTH_SHORT).show();
                }
                StcVariable.ADD_CARD_TO_FAVOURITE = true;
            }
        });

        back.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            intent.putExtra("value", activity);
            startActivity(intent);

        });

        callCard.setOnClickListener(view -> {
            String dial = num;
            StcVariable.alertBottomSheet("tel", dial, Profile.this);
        });

        historyCard.setOnClickListener(view -> {
            Intent intentData = new Intent(Profile.this, HistoryActivity.class);
            intentData.putExtra("name", contact_name.getText().toString());
            intentData.putExtra("num", num);
            intentData.putStringArrayListExtra("all number", numberModelArrayList);
            startActivity(intentData);
        });

        whatsCard.setOnClickListener(view -> {
            Toast.makeText(Profile.this, num, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + num));
            startActivity(intent);
        });


    }

    @NonNull
    @SuppressLint("Range")
    private ArrayList<String> getContact() {
        numberModelArrayList = new ArrayList<>();
        if (name.equals("Unknown")) {
            numberModelArrayList.add(num);

        } else if (name.equals(num)) {
            numberModelArrayList.add(num);
        } else {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            cursor.moveToFirst();
            do {
                String ab = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (ab.equals(name)) {
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    number = stringSlicer(number);
                    String aa = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    if (aa != null) {
                        Glide.with(this).load(aa).into(imageView);
                    } else {
                        Random random = new Random();
                        int r = random.nextInt(999);
                        imageView.setBackgroundColor(Color.rgb(r, r, r - 40));
                    }
                    numberModelArrayList.add(number);
                }
            } while (cursor.moveToNext());
        }
        ArrayList<String> arrayList = new ArrayList<>(contactsLimiter(numberModelArrayList));
        numberModelArrayList = arrayList;

        return numberModelArrayList;
    }

    private Set<String> contactsLimiter(ArrayList<String> arrayLista) {
        Set<String> set = new LinkedHashSet<String>(arrayLista);
        return set;
    }

    @NonNull
    private String stringSlicer(String strValue) {
        String number = strValue;
        number = number.replaceAll(" ", "");
        number = number.replaceAll("  ", "");
        number = number.replaceAll("-", "");
//        if (number.length() >= 6) {
//            String v = number.substring(0, 3);
//            if (v.equals("+91")) {
//                int a = number.length();
//                number = "+91" + number.substring(3, a);
//            } else if (number.length() == 10) {
//                number = "+91" + number;
//            }
//        }
        return number;
    }

    private void deleteContacts(String number) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        String[] args = new String[]{String.valueOf(getContactID(number))};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private long getContactID(String number) {
        long ID = 1;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                ID = cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ID;
    }

    public void getContactNameByNumber(String number) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contact_name.setText(cursor.getString(0));
                name = cursor.getString(0);
            } else {
                name = "Unknown";
                contact_name.setText("Unknown");
            }
            cursor.close();
        } else {
            name = "Unknown";
            contact_name.setText("Unknown");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}