package com.JvWorld.jcaller.settingsManagers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.Toast;

import com.JvWorld.jcaller.R;

public class Premium extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        ImageView process = findViewById(R.id.process);
        process.setOnClickListener(v -> {
            paymentFunction();
            Toast.makeText(this, "Payment Started", Toast.LENGTH_SHORT).show();
        });

    }

    private void paymentFunction() {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "7015954990@okbizaxis")
//                .appendQueryParameter("pa", vpa)
//                .appendQueryParameter("pn", "VIJAY")
                .appendQueryParameter("pn", "JvWorld")
                .appendQueryParameter("tn", "Premium Payment for JCaller")
//                .appendQueryParameter("mc", mcc)
                .appendQueryParameter("mc", "7299")
                .appendQueryParameter("tr", "BRCD9890AD56")
                .appendQueryParameter("am", "100.00")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
//        intent.setPackage("com.google.android.apps.nbu.paise.user");
        Intent ch = Intent.createChooser(intent, "pay with");
        if (null != ch.resolveActivity(getPackageManager())) {
            startActivityForResult(intent, 10);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}